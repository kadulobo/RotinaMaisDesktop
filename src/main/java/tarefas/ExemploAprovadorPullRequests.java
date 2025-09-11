package tarefas;

/**
 * Exemplo de utilização da classe {@link AprovadorPullRequests}.
 *
 * <p>O token de acesso é lido de um arquivo texto cujo caminho relativo
 * deve ser informado como primeiro argumento da aplicação.</p>
 */
public class ExemploAprovadorPullRequests {

    private static final String REPO_URL = "https://github.com/kadulobo/RotinaMaisDesktop";

    private static String TOKEN;

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Informe o caminho relativo do arquivo com o token.");
            return;
        }
        try {
            TOKEN = LeitorToken.ler(args[0]);
            AprovadorPullRequests aprovador = new AprovadorPullRequests(REPO_URL, TOKEN);
            aprovador.aprovarPendentes();
            System.out.println("Pull requests aprovados com sucesso.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

