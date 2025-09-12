RotinaMaisDesktop (estrutura completa)
=====================================
Requer **Java 17** ou superior para compilar e executar, devido às
dependências Jakarta Persistence 3.x e Hibernate 6.x.
- Versão do projeto definida no pom.xml: 1.0.0-SNAPSHOT.
- Para alterar a versão, edite a tag <version> no pom.xml.
- Para consultar a versão atual pela linha de comando, execute `mvn -q help:evaluate -Dexpression=project.version -DforceStdout`.
- Em cada release, incremente este número seguindo a convenção MAJOR.MINOR.PATCH.
- Código refatorado em PT-BR (component, swing, evento, model, dialog, form, main).
- Utilitário de ícones: swing.icon.Icones (PNG/JPG em /resources/icon + fallback Material Icons).
- Coloque seus PNGs/JPGs de ícones em: src/main/resources/icon/
- Coloque TimingFramework-0.55.jar em: libs/TimingFramework-0.55.jar (ou ajuste o pom.xml).

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
