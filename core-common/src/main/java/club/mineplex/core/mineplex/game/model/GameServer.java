package club.mineplex.core.mineplex.game.model;

import club.mineplex.core.mineplex.game.GameStatus;
import lombok.NonNull;
import lombok.Value;

import java.util.Optional;

@Value
public class GameServer {

    @NonNull String serverName;

    int maxPlayers;
    int onlinePlayers;

    @NonNull GameStatus status;
    String map;

    public Optional<String> getMap() {
        return Optional.ofNullable(this.map);
    }

}
