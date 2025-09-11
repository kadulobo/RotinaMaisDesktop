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

        try {
            TOKEN = LeitorToken.ler(readToken());
            AprovadorPullRequests aprovador = new AprovadorPullRequests(REPO_URL, TOKEN);
            aprovador.aprovarPendentes();
            System.out.println("Pull requests aprovados e mesclados com sucesso.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
	static String readToken() {
		String t = System.getenv("GH_PAT");
		if (t == null || t.isEmpty()) {
			throw new IllegalStateException("Defina GH_PAT no ambiente/Action");
		}
		return t;
	}
}

