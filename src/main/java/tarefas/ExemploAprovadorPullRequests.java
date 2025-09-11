package tarefas;

/**
 * Exemplo de utilização da classe {@link AprovadorPullRequests}.
 *
 * <p>Substitua o valor da constante {@code TOKEN} por um token pessoal
 * do GitHub antes de executar.</p>
 */
public class ExemploAprovadorPullRequests {

    private static final String REPO_URL = "https://github.com/kadulobo/RotinaMaisDesktop";

    // TODO: inserir token do GitHub aqui
    private static final String TOKEN = "SEU_TOKEN_AQUI";

    public static void main(String[] args) {
        try {
            AprovadorPullRequests aprovador = new AprovadorPullRequests(REPO_URL, TOKEN);
            aprovador.aprovarPendentes();
            System.out.println("Pull requests aprovados com sucesso.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
