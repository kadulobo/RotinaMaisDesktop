package tarefas;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Responsável por aprovar e mesclar todos os pull requests pendentes de um
 * repositório.
 * <p>
 * A classe utiliza a API REST do GitHub. É necessário fornecer um token
 * de acesso pessoal com permissão para aprovar e mesclar pull requests.
 * O token pode ser informado no construtor ou pela variável de ambiente
 * {@code GITHUB_TOKEN}.
 */
public class AprovadorPullRequests {

    private final String repoUrl;
    private final String token;

    /**
     * Cria um aprovador utilizando o token presente na variável de ambiente
     * {@code GITHUB_TOKEN}.
     *
     * @param repoUrl URL do repositório no GitHub
     */
    public AprovadorPullRequests(String repoUrl) {
        this(repoUrl, System.getenv("GITHUB_TOKEN"));
    }

    /**
     * Cria um aprovador informando explicitamente o token a ser utilizado.
     *
     * @param repoUrl URL do repositório no GitHub
     * @param token   token de acesso pessoal
     */
    public AprovadorPullRequests(String repoUrl, String token) {
        this.repoUrl = repoUrl;
        this.token = token;
        if (this.token == null || this.token.isEmpty()) {
            throw new IllegalArgumentException("Token do GitHub não fornecido");
        }
    }

    /**
     * Aprova e mescla todos os pull requests abertos do repositório informado.
     *
     * @throws IOException se ocorrer erro de comunicação com a API do GitHub
     */
    public void aprovarPendentes() throws IOException {
        String[] partes = extrairRepositorio(repoUrl);
        String owner = partes[0];
        String repo = partes[1];

        String authLogin = getAuthLogin();

        for (Integer pr : listarPullRequests(owner, repo)) {
            // verifica draft e autor
            boolean draftOuSelf = !podeAprovar(owner, repo, pr, authLogin);
            if (!draftOuSelf) {
                // aprova só quando NÃO é seu PR e não é draft
                aprovar(owner, repo, pr);
            }
            // tenta o merge de qualquer forma; sua mescla já valida merged:true e usa sha
            // se branch protection exigir review, aqui vai falhar com mensagem clara
            mesclar(owner, repo, pr);
        }
    }

    private String[] extrairRepositorio(String url) {
        String caminho = url.trim();
        if (caminho.startsWith("git@")) {
            int idx = caminho.indexOf(':');
            if (idx < 0) {
                throw new IllegalArgumentException("URL do repositório inválida: " + url);
            }
            caminho = caminho.substring(idx + 1);
        } else {
            caminho = caminho.replaceFirst("^https://github.com/", "");
        }
        caminho = caminho.replaceFirst("/+$", "");
        caminho = caminho.replaceFirst("\\.git$", "");
        caminho = caminho.replaceFirst("/+$", "");
        String[] partes = caminho.split("/");
        if (partes.length < 2) {
            throw new IllegalArgumentException("URL do repositório inválida: " + url);
        }
        return new String[]{partes[0], partes[1]};
    }

    private List<Integer> listarPullRequests(String owner, String repo) throws IOException {
        List<Integer> numeros = new ArrayList<>();
        Pattern padrao = Pattern.compile("\"number\"\\s*:\\s*([0-9]+)");
        int page = 1;
        while (true) {
            URL url = new URL("https://api.github.com/repos/" + owner + "/" + repo
                    + "/pulls?state=open&per_page=100&page=" + page);
            HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
            padraoHeaders(conexao);

            int status = conexao.getResponseCode();
            String json = readBody(conexao);
            if (status >= 300) {
                throw new IOException("Falha ao listar PRs: HTTP " + status + " - " + json);
            }

            Matcher matcher = padrao.matcher(json);
            int encontrados = 0;
            while (matcher.find()) {
                numeros.add(Integer.parseInt(matcher.group(1)));
                encontrados++;
            }
            if (encontrados == 0) {
                break;
            }
            page++;
        }
        return numeros;
    }

    private void aprovar(String owner, String repo, int numero) throws IOException {
        URL url = new URL("https://api.github.com/repos/" + owner + "/" + repo + "/pulls/" + numero + "/reviews");
        HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
        conexao.setRequestMethod("POST");
        padraoHeaders(conexao);
        conexao.setRequestProperty("Content-Type", "application/json");
        conexao.setDoOutput(true);

        String corpo = "{\"event\":\"APPROVE\",\"body\":\"Aprovado automaticamente pelo RotinaMaisDesktop\"}";
        try (OutputStream os = conexao.getOutputStream()) {
            os.write(corpo.getBytes(StandardCharsets.UTF_8));
        }

        int status = conexao.getResponseCode();
        String body = readBody(conexao);
        if (status >= 300) {
            throw new IOException("Falha ao aprovar PR " + numero + ": HTTP " + status + " - " + body);
        }
    }

 // 4) Mesclar validando "merged:true" e usando sha
    private void mesclar(String owner, String repo, int numero) throws IOException {
        if (!aguardarMergeavel(owner, repo, numero)) {
            throw new IOException("PR " + numero + " não está mergeável (mergeable=false/estado não limpo).");
        }

        String sha = obterHeadSha(owner, repo, numero);
        String metodo = escolherMetodoMerge(owner, repo); // merge|squash|rebase

        URL url = new URL("https://api.github.com/repos/" + owner + "/" + repo + "/pulls/" + numero + "/merge");
        HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
        conexao.setRequestMethod("PUT");
        padraoHeaders(conexao);
        conexao.setRequestProperty("Content-Type", "application/json");
        conexao.setDoOutput(true);

        String corpo = String.format(
            "{\"sha\":\"%s\",\"merge_method\":\"%s\",\"commit_title\":\"Merge PR #%d (bot)\",\"commit_message\":\"Mesclado automaticamente\"}",
            sha, metodo, numero
        );
        try (OutputStream os = conexao.getOutputStream()) {
            os.write(corpo.getBytes(StandardCharsets.UTF_8));
        }

        int status = conexao.getResponseCode();
        String body = readBody(conexao);

        // *** PONTO-CHAVE: só considerar sucesso se merged:true ***
        boolean merged = Pattern.compile("\"merged\"\\s*:\\s*true").matcher(body).find();
        if (status >= 300 || !merged) {
            throw new IOException("Falha ao mesclar PR " + numero + ": HTTP " + status + " - " + body);
        }
    }


    private void padraoHeaders(HttpURLConnection c) {
        c.setRequestProperty("Authorization", "Bearer " + token);
        c.setRequestProperty("Accept", "application/vnd.github+json");
        c.setRequestProperty("X-GitHub-Api-Version", "2022-11-28");
        c.setRequestProperty("User-Agent", "RotinaMaisDesktop/1.0");
        c.setConnectTimeout(15000);
        c.setReadTimeout(30000);
    }

    private String readBody(HttpURLConnection c) throws IOException {
        int status = c.getResponseCode();
        InputStream is = status < 400 ? c.getInputStream() : c.getErrorStream();
        if (is == null) {
            return "";
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String linha;
            while ((linha = reader.readLine()) != null) {
                sb.append(linha);
            }
            return sb.toString();
        }
    }

    private String getAuthLogin() throws IOException {
        URL url = new URL("https://api.github.com/user");
        HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
        padraoHeaders(conexao);
        int status = conexao.getResponseCode();
        String body = readBody(conexao);
        if (status >= 300) {
            throw new IOException("Falha ao obter usuário autenticado: HTTP " + status + " - " + body);
        }
        Matcher matcher = Pattern.compile("\"login\"\\s*:\\s*\"([^\"]+)\"").matcher(body);
        if (matcher.find()) {
            return matcher.group(1);
        }
        throw new IOException("Campo 'login' não encontrado na resposta do GitHub");
    }

    private boolean podeAprovar(String owner, String repo, int pr, String authLogin) throws IOException {
        URL url = new URL("https://api.github.com/repos/" + owner + "/" + repo + "/pulls/" + pr);
        HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
        padraoHeaders(conexao);
        int status = conexao.getResponseCode();
        String body = readBody(conexao);
        if (status >= 300) {
            throw new IOException("Falha ao consultar PR " + pr + ": HTTP " + status + " - " + body);
        }
        if (Pattern.compile("\"draft\"\\s*:\\s*true").matcher(body).find()) {
            return false;
        }
        int idxUser = body.indexOf("\"user\"");
        if (idxUser >= 0) {
            String sub = body.substring(idxUser);
            Matcher matcher = Pattern.compile("\"login\"\\s*:\\s*\"([^\"]+)\"").matcher(sub);
            if (matcher.find()) {
                String autor = matcher.group(1);
                if (authLogin != null && authLogin.equals(autor)) {
                    return false;
                }
            }
        }
        return true;
    }
 // 1) Espera o PR ficar mergeable (o GitHub recalcula isso após a aprovação)
    private boolean aguardarMergeavel(String owner, String repo, int pr) throws IOException {
        URL url = new URL("https://api.github.com/repos/" + owner + "/" + repo + "/pulls/" + pr);
        for (int i = 0; i < 5; i++) { // ~5s
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            padraoHeaders(c);
            String body = readBody(c);
            if (c.getResponseCode() >= 300) {
                throw new IOException("Falha ao consultar PR " + pr + ": HTTP " + c.getResponseCode() + " - " + body);
            }
            boolean mergeable = Pattern.compile("\"mergeable\"\\s*:\\s*true").matcher(body).find();
            String state = null;
            Matcher ms = Pattern.compile("\"mergeable_state\"\\s*:\\s*\"([^\"]+)\"").matcher(body);
            if (ms.find()) state = ms.group(1);
            if (mergeable && ("clean".equals(state) || "unstable".equals(state))) return true;
            try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
        }
        return false;
    }
 // 2) Captura o head.sha do PR
    private String obterHeadSha(String owner, String repo, int pr) throws IOException {
        URL url = new URL("https://api.github.com/repos/" + owner + "/" + repo + "/pulls/" + pr);
        HttpURLConnection c = (HttpURLConnection) url.openConnection();
        padraoHeaders(c);
        String body = readBody(c);
        if (c.getResponseCode() >= 300) {
            throw new IOException("Falha ao obter head.sha do PR " + pr + ": HTTP " + c.getResponseCode() + " - " + body);
        }
        Matcher m = Pattern.compile("\"head\"\\s*:\\s*\\{[^}]*\"sha\"\\s*:\\s*\"([^\"]+)\"").matcher(body);
        if (m.find()) return m.group(1);
        throw new IOException("head.sha não encontrado no PR " + pr);
    }
    
 // 3) (opcional) lê quais métodos de merge o repo aceita
    private String escolherMetodoMerge(String owner, String repo) throws IOException {
        URL url = new URL("https://api.github.com/repos/" + owner + "/" + repo);
        HttpURLConnection c = (HttpURLConnection) url.openConnection();
        padraoHeaders(c);
        String body = readBody(c);
        if (c.getResponseCode() >= 300) return "merge"; // fallback
        boolean allowMerge  = body.contains("\"allow_merge_commit\":true");
        boolean allowSquash = body.contains("\"allow_squash_merge\":true");
        boolean allowRebase = body.contains("\"allow_rebase_merge\":true");
        if (allowMerge)  return "merge";
        if (allowSquash) return "squash";
        if (allowRebase) return "rebase";
        return "merge";
    }



}
