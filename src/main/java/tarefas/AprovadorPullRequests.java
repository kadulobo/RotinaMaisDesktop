package tarefas;

import java.io.BufferedReader;
import java.io.IOException;
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
 * Responsável por aprovar todos os pull requests pendentes de um repositório.
 * <p>
 * A classe utiliza a API REST do GitHub. É necessário fornecer um token
 * de acesso pessoal com permissão para aprovar pull requests.
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
     * Aprova todos os pull requests abertos do repositório informado.
     *
     * @throws IOException se ocorrer erro de comunicação com a API do GitHub
     */
    public void aprovarPendentes() throws IOException {
        String[] partes = extrairRepositorio(repoUrl);
        String owner = partes[0];
        String repo = partes[1];

        for (Integer pr : listarPullRequests(owner, repo)) {
            aprovar(owner, repo, pr);
        }
    }

    private String[] extrairRepositorio(String url) {
        String caminho = url.replace("https://github.com/", "");
        String[] partes = caminho.split("/");
        if (partes.length < 2) {
            throw new IllegalArgumentException("URL do repositório inválida: " + url);
        }
        return new String[]{partes[0], partes[1]};
    }

    private List<Integer> listarPullRequests(String owner, String repo) throws IOException {
        List<Integer> numeros = new ArrayList<>();
        Pattern padrao = Pattern.compile("\"number\"\s*:\s*(\\d+)");
        int page = 1;
        while (true) {
            URL url = new URL("https://api.github.com/repos/" + owner + "/" + repo
                    + "/pulls?state=open&per_page=100&page=" + page);
            HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
            conexao.setRequestProperty("Authorization", "Bearer " + token);
            conexao.setRequestProperty("Accept", "application/vnd.github+json");

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conexao.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder json = new StringBuilder();
                String linha;
                while ((linha = reader.readLine()) != null) {
                    json.append(linha);
                }
                Matcher matcher = padrao.matcher(json.toString());
                int encontrados = 0;
                while (matcher.find()) {
                    numeros.add(Integer.parseInt(matcher.group(1)));
                    encontrados++;
                }
                if (encontrados == 0) {
                    break;
                }
            }
            page++;
        }
        return numeros;
    }

    private void aprovar(String owner, String repo, int numero) throws IOException {
        URL url = new URL("https://api.github.com/repos/" + owner + "/" + repo + "/pulls/" + numero + "/reviews");
        HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
        conexao.setRequestMethod("POST");
        conexao.setRequestProperty("Authorization", "Bearer " + token);
        conexao.setRequestProperty("Accept", "application/vnd.github+json");
        conexao.setRequestProperty("Content-Type", "application/json");
        conexao.setDoOutput(true);

        String corpo = "{\"event\":\"APPROVE\"}";
        try (OutputStream os = conexao.getOutputStream()) {
            os.write(corpo.getBytes(StandardCharsets.UTF_8));
        }

        int status = conexao.getResponseCode();
        if (status >= 300) {
            throw new IOException("Falha ao aprovar PR " + numero + ": " + conexao.getResponseMessage());
        }
    }
}

