package club.mineplex.bot.common.mineplex.community;

import club.mineplex.bot.MineplexBot;
import club.mineplex.bot.util.UtilFile;
import club.mineplex.core.cache.Cache;
import club.mineplex.core.database.Database;
import club.mineplex.core.mineplex.community.Community;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.jdbi.v3.core.statement.UnableToExecuteStatementException;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class CommunityCache extends Cache<Collection<Community>> {

    private static final File COMMUNITIES_FILE = UtilFile.getAppResource("communities.json");

    private final HashMap<Community, String[]> communities = new HashMap<>();

    public CommunityCache() {
        super(5L);
        try {
            final JsonMapper mapper = new JsonMapper();
            final Collection<CommunityModel> communityModels = mapper.readValue(
                    COMMUNITIES_FILE,
                    new TypeReference<Collection<CommunityModel>>() {
                    }
            );

            for (final CommunityModel model : communityModels) {
                final Community community = model.getCommunity();
                this.communities.put(community, model.getWebhookLinks());

                Database.getInstance().getJdbi().useExtension(CommunityDb.class, extension -> {
                    extension.createTable(community.getName());
                });
            }

        } catch (final IOException e) {
            MineplexBot.getLogger().error("There was an error reading '{}'", COMMUNITIES_FILE.getName());
            e.printStackTrace();
        }
    }

    @Override
    public Collection<Community> get() {
        return this.communities.keySet();
    }

    @Override
    protected void updateCache() {
        this.saveData();
    }

    protected void saveData() {
        for (final Community community : this.communities.keySet()) {
            Database.getInstance().getJdbi().useExtension(CommunityDb.class, extension -> {
                community.getPlayerData().forEach(data -> {
                    final UUID playerUuid = data.getUuid();
                    final int kicks = data.getKicks();
                    final int bans = data.getBans();
                    final long messages = data.getMessages();
                    final String name = community.getName().toLowerCase();
                    try {
                        extension.insertRow(name, playerUuid.toString(), messages, kicks, bans);
                    } catch (final UnableToExecuteStatementException e) {
                        if (e.getMessage().contains("Duplicate entry")) {
                            extension.updateRow(name, playerUuid.toString(), messages, kicks, bans);
                        }
                    }
                });
            });
        }
    }

    protected Optional<Community> getTrackedCommunity(final String name) {
        return this.get()
                   .stream()
                   .filter(tracked -> tracked.getName().equals(name))
                   .findAny();
    }

    public String[] getWebhookLinks(final String community) {
        return this.communities.getOrDefault(this.getTrackedCommunity(community).orElse(null), new String[]{});
    }

    private static class CommunityModel {

        private final Community community;
        private final String[] webhookLinks;

        public CommunityModel(@JsonProperty("name") final String name,
                              @JsonProperty("webhookLinks") final String[] webhookLinks) {
            this.community = new Community(name);
            this.webhookLinks = webhookLinks;
        }

        public Community getCommunity() {
            return this.community;
        }

        public String[] getWebhookLinks() {
            return this.webhookLinks;
        }

    }

}
