package club.mineplex.api.mineplex.website.status.database;

import club.mineplex.api.mineplex.website.status.models.UptimeMetric;
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.transaction.Transaction;

import java.util.List;

public interface JavaTracker {

    @Transaction
    @SqlUpdate("CREATE TABLE IF NOT EXISTS mp_java_ping_hist(`date` timestamp NOT NULL DEFAULT current_timestamp(), `ping` bigint(20) NOT NULL DEFAULT -1)")
    void createTable();

    @Transaction
    @SqlUpdate("INSERT INTO mp_java_ping_hist(date, ping) VALUES(:date, :ping)")
    void recordMetric(@BindBean UptimeMetric metric);

    @Transaction
    @SqlUpdate("DELETE FROM mp_java_ping_hist WHERE date < now() - interval 30 DAY;")
    void cleanEntries();

    @Transaction
    @SqlQuery("SELECT * FROM mp_java_ping_hist WHERE date > now() - interval :days DAY;")
    @RegisterConstructorMapper(UptimeMetric.class)
    List<UptimeMetric> getMetrics(int days);

}
