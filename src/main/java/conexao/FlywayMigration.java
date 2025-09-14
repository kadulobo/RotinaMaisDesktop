package conexao;

import org.flywaydb.core.Flyway;

/**
 * Responsável por executar as migrações do banco de dados utilizando o Flyway.
 */
public final class FlywayMigration {

    private FlywayMigration() {
    }

    public static void migrate(DatabaseConfig cfg) {
    	if(cfg.isEmbedded()) {
            Flyway flyway = Flyway.configure()
                    .locations("classpath:db/migration")
                    .dataSource(cfg.getJdbcUrl(), cfg.getUser(), cfg.getPassword())
                    .load();
            flyway.migrate();
    	}

    }
}
