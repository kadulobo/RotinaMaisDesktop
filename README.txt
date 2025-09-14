RotinaMaisDesktop (estrutura completa)
=====================================
Requer **Java 17** ou superior para compilar e executar, devido às
dependências Jakarta Persistence 3.x e Hibernate 6.x.
- Versão do projeto definida no pom.xml: 1.0.0-SNAPSHOT.
- A versão `1.0.0-SNAPSHOT` indica que o projeto está em desenvolvimento (snapshot).
- Para alterar a versão, edite a tag <version> no pom.xml.
- Para consultar a versão atual pela linha de comando, execute `mvn -q help:evaluate -Dexpression=project.version -DforceStdout`.
- Em cada release, incremente este número seguindo a convenção MAJOR.MINOR.PATCH.
- Para atualizar automaticamente, utilize `mvn versions:set -DnewVersion=X.Y.Z` (plugin Versions).
- O valor de `project.version` é utilizado pelo Maven para nomear artefatos e deve estar sempre alinhado com o número acima.
- Sempre que alterar `<version>` no `pom.xml`, atualize também este README para documentar a versão em uso.
- Código refatorado em PT-BR (component, swing, evento, model, dialog, form, main).
- Utilitário de ícones: swing.icon.Icones (PNG/JPG em /resources/icon + fallback Material Icons).
- Coloque seus PNGs/JPGs de ícones em: src/main/resources/icon/
- A dependência TimingFramework é gerenciada pelo Maven e incluída automaticamente no JAR.
- Gere um executável com todas as dependências usando `mvn package`.
- O arquivo resultante estará em `target/rotinamais-desktop-*-jar-with-dependencies.jar` e pode ser executado com `java -jar`.

Configuração do PostgreSQL
------------------------------
O arquivo `database.properties` define as credenciais de conexão do banco e
permite escolher entre usar um PostgreSQL embutido ou um servidor externo:

- `db.embedded=true`: o aplicativo iniciará um servidor PostgreSQL embutido.
- `db.embedded=false`: utiliza um servidor PostgreSQL já instalado na máquina
  ou em outra máquina acessível.

Altere também as demais propriedades (`db.port`, `db.name`, `db.user`, etc.) de
acordo com o ambiente utilizado.

- Exemplos de entidades JPA e DAOs nativos adicionados para Meta, Cofre,
  TreinoExercicio, RotinaPeriodo, AlimentacaoIngrediente, IngredienteFornecedor,
  SiteObjeto, MonitoramentoObjeto, Papel, Carteira e Operacao.

- Exemplos de entidades JPA e DAOs nativos adicionados para Monitoramento, Alimentacao, Treino, Objeto, Site, Caixa e Periodo.

- Classe `AprovadorPullRequests`: utiliza a API REST do GitHub para aprovar e mesclar automaticamente pull requests pendentes de um repositório.
 Requer um token de acesso pessoal e possui um exemplo de uso em `src/main/java/tarefas/ExemploAprovadorPullRequests.java`.
 Funções principais:
  - `aprovarPendentes()`: aprova e mescla todos os pull requests abertos.
  - `extrairRepositorio(String url)`: extrai o par owner/repo a partir da URL do repositório.
  - `listarPullRequests(String owner, String repo)`: obtém os números dos pull requests em aberto.
  - `podeAprovar(String owner, String repo, int pr, String authLogin)`: valida se o pull request pode ser aprovado automaticamente.
  - `aprovar(String owner, String repo, int pr)`: envia uma revisão de aprovação para o pull request.
  - `mesclar(String owner, String repo, int pr)`: realiza o merge do pull request aprovado.


Entidades JPA
-------------
Lista resumida dos campos principais de cada entidade:

- **Alimentacao**: idAlimentacao, status, nome, link, video, preparo, idRotina
- **AlimentacaoIngrediente**: idAlimentacaoIngrediente, quantidade, idAlimentacao, idIngrediente
- **Caixa**: idCaixa, nome, reservaEmergencia, salarioMedio, valorTotal, usuario, movimentacoes
- **Carteira**: idCarteira, nome, tipo, dataInicio, idUsuario
- **Categoria**: idCategoria, nome, descricao, foto, dataCriacao, eventos
- **Cofre**: idCofre, login, senha, tipo, foto, plataforma, idUsuario
- **Documento**: idDocumento, nome, arquivo, foto, video, data, idUsuario
- **Evento**: idEvento, vantagem, foto, nome, descricao, dataCriacao, categoria, lancamentos
- **Exercicio**: idExercicio, nome, cargaLeve, cargaMedia, cargaMaxima
- **Fornecedor**: idFornecedor, nome, foto, endereco, online
- **Ingrediente**: foto, idIngrediente, nome, descricao
- **IngredienteFornecedor**: idFornecedorIngrediente, valor, data, idFornecedor, idIngrediente
- **Lancamento**: idLancamento, valor, fixo, dataPagamento, status, movimentacao, evento
- **Meta**: idMeta, pontoMinimo, pontoMedio, pontoMaximo, status, foto, idPeriodo
- **ModelCard**: title, values, percentage, icon
- **ModelMenu**: icon, menuName, subMenu[]
- **ModelStudent**: icon, name, gender, course, fees
- **Monitoramento**: idMonitoramento, status, nome, descricao, foto, idPeriodo
- **MonitoramentoObjeto**: idMonitoramentoObjeto, data, idMonitoramento, idObjeto
- **Movimentacao**: idMovimentacao, desconto, vantagem, liquido, tipo, status, ponto, usuario, caixa, periodo, lancamentos
- **Objeto**: idObjeto, nome, tipo, valor, descricao, foto
- **Operacao**: idOperacao, fechamento, tempoOperacao, qtdCompra, abertura, qtdVenda, lado, precoCompra, precoVenda, precoMedio, resIntervalo, numeroOperacao, resOperacao, drawdon, ganhoMax, perdaMax, tet, total, idCarteira, idPapel
- **Papel**: idPapel, codigo, tipo, vencimento
- **Periodo**: idPeriodo, ano, mes, movimentacoes
- **Rotina**: idRotina, nome, inicio, fim, descricao, status, ponto, idUsuario
- **RotinaPeriodo**: idRotinaPeriodo, idRotina, idPeriodo
- **Site**: idSite, url, ativo, logo
- **SiteObjeto**: idSiteObjeto, idSite, idObjeto
- **Treino**: idTreino, nome, classe, idRotina
- **TreinoExercicio**: idTreinoExercicio, qtdRepeticao, tempoDescanso, ordem, feito, idExercicio, idTreino
- **Usuario**: idUsuario, nome, senha, foto, email, cpf, movimentacoes, caixas
