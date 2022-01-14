package club.mineplex.core.mineplex.player;

import club.mineplex.core.mineplex.MineplexRank;
import club.mineplex.core.util.UtilText;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NonNull;

import java.util.UUID;

@Data
public class PlayerData {

    private final String name;
    private final UUID uuid;
    @JsonIgnore
    private MineplexRank mineplexRank = MineplexRank.PLAYER;

    @JsonCreator
    public PlayerData(@JsonProperty("name") final String name, @JsonProperty("id") final String uuid) {
        this(name, UtilText.uuidWithNoDashes(uuid));
    }

    public PlayerData(@JsonProperty("name") final String name, @JsonProperty("id") final UUID uuid) {
        this.name = name;
        this.uuid = uuid;
    }

    public String getAvatarUrl() {
        return "https://mc-heads.net/avatar/" + this.uuid.toString().replaceAll("-", "") + "/100.png";
    }

    public void setMineplexRank(@NonNull final MineplexRank mineplexRank) {
        this.mineplexRank = mineplexRank;
    }

}
