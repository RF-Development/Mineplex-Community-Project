package club.mineplex.bot.common.player;

import club.mineplex.core.cache.Cache;
import club.mineplex.core.mineplex.player.PlayerData;
import club.mineplex.core.util.HttpClientUtilities;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class PlayerCache extends Cache<Collection<PlayerData>> {

    private static final String MOJANG_USERNAME_TO_UUID_URL = "https://api.mojang.com/users/profiles/minecraft/%s";

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final List<PlayerData> savedPlayers = new ArrayList<>();

    @Override
    public Collection<PlayerData> get() {
        return this.savedPlayers;
    }

    @Override
    protected void updateCache() {
        // We're clearing the cache because we don't want to store all players that come across us.
        // We will only be storing the cached players from the last 30 minutes.
        this.savedPlayers.clear();
    }

    /**
     * @param name The player's username
     * @return The data of the player that was saved, if any
     */
    @SneakyThrows
    public Optional<PlayerData> savePlayer(final String name) {
        final Optional<PlayerData> currentlySaved = this.savedPlayers
                .stream()
                .filter(player -> player.getName().equalsIgnoreCase(name))
                .findAny();
        if (currentlySaved.isPresent()) {
            return currentlySaved;
        }

        final String endpoint = String.format(MOJANG_USERNAME_TO_UUID_URL, name);
        final Optional<PlayerData> parsedResponse = HttpClientUtilities.getParsedResponse(
                endpoint,
                this.objectMapper,
                PlayerData.class
        );

        if (!parsedResponse.isPresent()) {
            return Optional.empty();
        }

        final PlayerData playerData = parsedResponse.get();
        this.savedPlayers.add(playerData);
        return parsedResponse;
    }

}
