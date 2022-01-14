package club.mineplex.core.database;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class DatabaseInfo {

    private final String username;
    private final String password;
    private final String host;
    private final String database;
    private final int port;

    @JsonCreator
    public DatabaseInfo(@JsonProperty("username") final String username,
                        @JsonProperty("password") final String password,
                        @JsonProperty("host") final String host,
                        @JsonProperty("database") final String database,
                        @JsonProperty("port") final int port) {
        this.username = username;
        this.password = password;
        this.host = host;
        this.database = database;
        this.port = port;
    }

}
