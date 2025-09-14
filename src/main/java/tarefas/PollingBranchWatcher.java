package tarefas;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * PollingBranchWatcher
 *
 * Serviço de polling que verifica mudanças em uma branch Git e executa pull automático.
 *
 * Instruções de execução:
 *
 * Windows
 * java -cp target/classes tarefas.PollingBranchWatcher ^
 *   --repo="C:\\Projetos\\RotinaMaisDesktop" ^
 *   --branch=desenvolvimento ^
 *   --interval=60 ^
 *   --git="C:\\Program Files\\Git\\bin\\git.exe" ^
 *   --stashOnDirty=true
 *
 * Linux
 * java -cp target/classes tarefas.PollingBranchWatcher \
 *   --repo="/home/user/projetos/RotinaMaisDesktop" \
 *   --branch=desenvolvimento \
 *   --interval=60 \
 *   --git="git" \
 *   --stashOnDirty=true
 */
public class PollingBranchWatcher {

    private static final DateTimeFormatter TS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter STASH_TS = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private static File repoDir;
    private static String branch;
    private static String remote;
    private static String gitExe;
    private static boolean stashOnDirty;
    private static FileWriter logFile;

    private static final AtomicBoolean running = new AtomicBoolean(false);

    public static void main(String[] args) throws Exception {
        Map<String, String> params = parseArgs(args);

        String repoPath = params.get("repo");
        if (repoPath == null) {
            System.err.println("Parâmetro --repo é obrigatório");
            System.exit(1);
        }
        repoDir = new File(repoPath);
        if (!repoDir.isDirectory()) {
            System.err.println("Repositório inválido: " + repoPath);
            System.exit(1);
        }

        branch = params.getOrDefault("branch", "desenvolvimento");
        remote = params.getOrDefault("remote", "origin");
        gitExe = params.getOrDefault("git", "git");
        stashOnDirty = Boolean.parseBoolean(params.getOrDefault("stashOnDirty", "false"));
        int interval = Integer.parseInt(params.getOrDefault("interval", "60"));

        if (params.containsKey("logFile")) {
            File lf = new File(params.get("logFile"));
            lf.getParentFile().mkdirs();
            logFile = new FileWriter(lf, true);
        }

        try {
            int exit = new ProcessBuilder(gitExe, "--version").start().waitFor();
            if (exit != 0) {
                System.err.println("Git não encontrado: " + gitExe);
                System.exit(1);
            }
        } catch (IOException e) {
            System.err.println("Git não encontrado: " + gitExe);
            System.exit(1);
        }

        log("Iniciando watcher no repositório " + repoDir.getAbsolutePath());

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log("Encerrando PollingBranchWatcher");
            try {
                if (logFile != null) {
                    logFile.close();
                }
            } catch (IOException e) {
                // ignore
            }
        }));

        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(() -> {
            if (!running.compareAndSet(false, true)) {
                log("Ciclo anterior ainda em execução");
                return;
            }
            try {
                ciclo();
            } finally {
                running.set(false);
            }
        }, 0, interval, TimeUnit.SECONDS);

        exec.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
    }

    private static void ciclo() {
        try {
            runGit("fetch", "--prune");
            String localSha = readGit("rev-parse", branch);
            String remoteSha = readGit("rev-parse", remote + "/" + branch);
            if (localSha.equals(remoteSha)) {
                log("Sem mudanças");
                return;
            }
            log("Mudança detectada: " + abreviar(localSha) + " -> " + abreviar(remoteSha));
            String status = readGit("status", "--porcelain");
            boolean dirty = status != null && !status.trim().isEmpty();
            boolean stashed = false;
            if (dirty) {
                if (stashOnDirty) {
                    String stashName = "autostash-" + LocalDateTime.now().format(STASH_TS);
                    runGit("stash", "push", "--include-untracked", "-m", stashName);
                    stashed = true;
                } else {
                    log("Working tree sujo, pull cancelado");
                    return;
                }
            }
            runGit("pull", "--ff-only");
            if (stashed) {
                int exit = runGitAllowExit("stash", "pop");
                if (exit == 0) {
                    log("stash pop aplicado");
                } else {
                    log("falha ao aplicar stash (exit " + exit + ")");
                }
            }
        } catch (Exception e) {
            logErr("Erro no ciclo: " + e.getMessage());
            e.printStackTrace(System.err);
        }
    }

    private static String abreviar(String sha) {
        if (sha == null) {
            return "";
        }
        return sha.length() > 8 ? sha.substring(0, 8) : sha;
    }

    private static Map<String, String> parseArgs(String[] args) {
        Map<String, String> map = new HashMap<>();
        for (String arg : args) {
            if (arg.startsWith("--")) {
                int idx = arg.indexOf('=');
                if (idx > 2) {
                    map.put(arg.substring(2, idx), arg.substring(idx + 1));
                }
            }
        }
        return map;
    }

    private static void log(String msg) {
        String line = "[" + LocalDateTime.now().format(TS) + "] " + msg;
        System.out.println(line);
        if (logFile != null) {
            try {
                logFile.write(line + System.lineSeparator());
                logFile.flush();
            } catch (IOException e) {
                System.err.println("Falha ao escrever log: " + e.getMessage());
            }
        }
    }

    private static void logErr(String msg) {
        String line = "[" + LocalDateTime.now().format(TS) + "] " + msg;
        System.err.println(line);
        if (logFile != null) {
            try {
                logFile.write(line + System.lineSeparator());
                logFile.flush();
            } catch (IOException e) {
                System.err.println("Falha ao escrever log: " + e.getMessage());
            }
        }
    }

    private static void runGit(String... args) throws IOException, InterruptedException {
        int exit = runGitAllowExit(args);
        if (exit != 0) {
            throw new IOException("git " + Arrays.toString(args) + " saiu com código " + exit);
        }
    }

    private static int runGitAllowExit(String... args) throws IOException, InterruptedException {
        List<String> cmd = new ArrayList<>();
        cmd.add(gitExe);
        cmd.addAll(Arrays.asList(args));
        ProcessBuilder pb = new ProcessBuilder(cmd);
        pb.directory(repoDir);
        pb.redirectErrorStream(true);
        Process p = pb.start();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                log(line);
            }
        }
        return p.waitFor();
    }

    private static String readGit(String... args) throws IOException, InterruptedException {
        List<String> cmd = new ArrayList<>();
        cmd.add(gitExe);
        cmd.addAll(Arrays.asList(args));
        ProcessBuilder pb = new ProcessBuilder(cmd);
        pb.directory(repoDir);
        pb.redirectErrorStream(true);
        Process p = pb.start();
        String first = "";
        try (BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream(), StandardCharsets.UTF_8))) {
            String line = br.readLine();
            if (line != null) {
                first = line.trim();
            }
            while (br.readLine() != null) {
                // drena
            }
        }
        int exit = p.waitFor();
        if (exit != 0) {
            throw new IOException("git " + Arrays.toString(args) + " saiu com código " + exit);
        }
        return first;
    }
}

