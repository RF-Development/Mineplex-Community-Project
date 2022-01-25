package club.mineplex.core.database;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.SneakyThrows;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import javax.sql.DataSource;
import java.io.File;

/**
 * A database
 */
public class Database {

    private static final Database INSTANCE = new Database();
    private Jdbi jdbi;

    private Database() {
    }

    public static Database getInstance() {
        return INSTANCE;
    }

    @SneakyThrows
    public void init(final File file) {
        final ObjectMapper mapper = new ObjectMapper();
        final DatabaseInfo databaseInfo = mapper.readValue(file, new TypeReference<DatabaseInfo>() {
        });

        final HikariConfig config = new HikariConfig();
        config.setJdbcUrl(

                "jdbc:mysql://" +
                        databaseInfo.getHost() +
                        ":" +
                        databaseInfo.getPort() +
                        "/" +
                        databaseInfo.getDatabase()

        );

        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setUsername(databaseInfo.getUsername());
        config.setPassword(databaseInfo.getPassword());
        config.setMinimumIdle(1);
        config.setMaximumPoolSize(5);
        config.setConnectionTimeout(1000);
        config.setConnectionTestQuery("SELECT 1");
        final DataSource dataSource = new HikariDataSource(config);

        this.jdbi = Jdbi.create(dataSource).installPlugin(new SqlObjectPlugin());
    }


    public Jdbi getJdbi() {
        return this.jdbi;
    }

}