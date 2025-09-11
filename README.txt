RotinaMaisDesktop (estrutura completa)
=====================================
Requer **Java 17** ou superior para compilar e executar, devido às
dependências Jakarta Persistence 3.x e Hibernate 6.x.
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

