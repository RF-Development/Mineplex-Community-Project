package club.mineplex.core.mineplex.game.model;

import club.mineplex.core.mineplex.game.MineplexGame;
import lombok.NonNull;
import lombok.Value;

@Value
public class GameData {

    @NonNull MineplexGame game;
    int players;

    @NonNull GameServer[] servers;

}
