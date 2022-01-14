package club.mineplex.bot.common.mineplex.community;

import club.mineplex.core.mineplex.community.Community;
import lombok.SneakyThrows;
import org.jdbi.v3.core.statement.UnableToExecuteStatementException;
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.Define;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.transaction.Transaction;

public interface CommunityDb {

    @Transaction
    @SqlUpdate("CREATE TABLE IF NOT EXISTS community_<table>(`uuid` VARCHAR(36) NOT NULL PRIMARY KEY, `messages` BIGINT NOT NULL DEFAULT 0, `kicks` INT NOT NULL DEFAULT 0, `bans` INT NOT NULL DEFAULT 0);")
    void createTable(@Define("table") String communityName);

    @SneakyThrows
    @Transaction
    @SqlUpdate("INSERT INTO community_<table>(uuid,messages,kicks,bans) VALUES(:uuid, :messages, :kicks, :bans)")
    void insertRow(@Define("table") String communityName,
                   @Bind("uuid") String uuid,
                   @Bind("messages") long messages,
                   @Bind("kicks") int kicks,
                   @Bind("bans") int bans) throws UnableToExecuteStatementException;

    @Transaction
    @SqlUpdate("UPDATE community_<table> SET messages=<messages>, kicks=<kicks>, bans=<bans> WHERE uuid=:uuid")
    void updateRow(@Define("table") String communityName,
                   @Bind("uuid") String uuid,
                   @Define("messages") long messages,
                   @Define("kicks") int kicks,
                   @Define("bans") int bans);

    @Transaction
    @SqlQuery("SELECT * FROM community_<table> WHERE uuid=:uuid LIMIT 1")
    @RegisterConstructorMapper(Community.CommunityPlayerData.class)
    Community.CommunityPlayerData getPunishData(@Define("table") String communityName,
                                                @Bind("uuid") String uuid);

}
