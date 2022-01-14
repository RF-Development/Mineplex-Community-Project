package club.mineplex.core.mineplex.community;

import club.mineplex.core.mineplex.player.PlayerData;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.jdbi.v3.core.mapper.reflect.ColumnName;

import java.util.*;

@Value
@EqualsAndHashCode
public class Community {

    String name;

    @JsonIgnore
    Set<CommunityPlayerData> punishData = new HashSet<>();

    public Community(@JsonProperty("name") final String name) {
        this.name = name;
    }

    public Collection<CommunityPlayerData> getPlayerData() {
        return this.punishData;
    }

    public Optional<CommunityPlayerData> getPlayerData(final UUID uuid) {
        for (final CommunityPlayerData punishData : this.punishData) {
            if (punishData.getUuid().equals(uuid)) {
                return Optional.of(punishData);
            }
        }

        return Optional.empty();
    }

    @Override
    public boolean equals(final Object object) {
        return object instanceof Community && ((Community) object).getName().equals(this.name);
    }

    @Value
    @AllArgsConstructor(access = AccessLevel.PUBLIC)
    public static class Message {

        Community community;
        PlayerData player;
        String text;

    }

    @Data
    public static class CommunityPlayerData {

        private final String uuid;
        private long messages;
        private int kicks;
        private int bans;

        public CommunityPlayerData(@ColumnName("uuid") final String uuid,
                                   @ColumnName("messages") final long messages,
                                   @ColumnName("kicks") final int kicks,
                                   @ColumnName("bans") final int bans) {
            this.messages = messages;
            this.uuid = uuid;
            this.kicks = kicks;
            this.bans = bans;
        }

        public UUID getUuid() {
            return UUID.fromString(this.uuid);
        }
    }

}
