package club.mineplex.api.mineplex.website.status.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NonNull;

@Data
public abstract class ServerStatus {
    @NonNull
    @JsonIgnore
    protected final String serverName;
    private Condition condition = Condition.BAD;

    public ServerStatus(@NonNull final String serverName) {
        this.serverName = serverName;
    }

}
