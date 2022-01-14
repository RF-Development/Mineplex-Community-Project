package club.mineplex.api.database;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.postgres.PostgresPlugin;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class JdbiConfiguration {

    @Bean
    public Jdbi jdbi(final DataSource dataSource) {
        return Jdbi.create(dataSource)
                   .installPlugin(new PostgresPlugin())
                   .installPlugin(new SqlObjectPlugin());
    }
}