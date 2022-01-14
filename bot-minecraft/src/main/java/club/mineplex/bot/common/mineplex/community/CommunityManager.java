package club.mineplex.bot.common.mineplex.community;

import club.mineplex.bot.UtilReference;
import club.mineplex.bot.chat.SystemMessage;
import club.mineplex.bot.common.player.PlayerCache;
import club.mineplex.core.cache.GlobalCacheRepository;
import club.mineplex.core.database.Database;
import club.mineplex.core.discord.Embed;
import club.mineplex.core.discord.WebhookMessage;
import club.mineplex.core.mineplex.MineplexRank;
import club.mineplex.core.mineplex.community.Community;
import club.mineplex.core.mineplex.player.PlayerData;
import club.mineplex.core.util.UtilText;
import lombok.NonNull;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;

import java.awt.*;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class CommunityManager {

    private static final CommunityManager INSTANCE = new CommunityManager();

    private CommunityManager() {
    }

    public static CommunityManager getInstance() {
        return INSTANCE;
    }

    public Community.CommunityPlayerData getPlayerData(final Community community, final UUID uuid) {
        if (community.getPlayerData(uuid).isPresent()) {
            return community.getPlayerData(uuid).get();
        }

        final Community.CommunityPlayerData defaultData = new Community.CommunityPlayerData(uuid.toString(), 0, 0, 0);
        final AtomicReference<Community.CommunityPlayerData> data = new AtomicReference<>(defaultData);
        Database.getInstance().getJdbi().useExtension(CommunityDb.class, extension -> {
            final Community.CommunityPlayerData foundData =
                    extension.getPunishData(community.getName(), uuid.toString());
            if (foundData != null) {
                data.set(foundData);
            }
        });

        community.getPunishData().add(data.get());
        return data.get();
    }

    public void handleCommunityAction(@NonNull final String type,
                                      @NonNull final String communityName,
                                      @NonNull final String playerName,
                                      final String staffName) {

        final CommunityCache cache = GlobalCacheRepository.getCache(CommunityCache.class);
        final Optional<Community> communityOpt = cache.getTrackedCommunity(communityName);
        if (!communityOpt.isPresent()) {
            return;
        }

        final Community community = communityOpt.get();
        final Optional<PlayerData> playerOpt = GlobalCacheRepository.getCache(PlayerCache.class).savePlayer(playerName);
        if (!playerOpt.isPresent()) {
            return;
        }

        final PlayerData player = playerOpt.get();
        final Community.CommunityPlayerData playerData = this.getPlayerData(community, player.getUuid());

        final String title;
        boolean usePunishmentFooter = false;

        switch (SystemMessage.valueOf(type)) {
            case COMMUNITY_BAN:
                playerData.setBans(playerData.getBans() + 1);
                cache.saveData();
                final String bannedPlayerFormat = player.getName() + " (" + playerData.getBans() + ")";
                title = String.format("%s has been banned by %s.", bannedPlayerFormat, staffName);
                break;
            case COMMUNITY_UNBAN:
                final String unbannedPlayerFormat = player.getName() + " (" + playerData.getBans() + ")";
                title = String.format("%s has been unbanned by %s.", unbannedPlayerFormat, staffName);
                break;
            case COMMUNITY_KICK:
                playerData.setKicks(playerData.getKicks() + 1);
                cache.saveData();
                final String kickedPlayerFormat = player.getName() + " (" + playerData.getKicks() + ")";
                title = String.format("%s has been kicked by %s.", kickedPlayerFormat, staffName);
                break;
            case COMMUNITY_JOIN:
                title = String.format("%s has joined %s.", player.getName(), community.getName());
                usePunishmentFooter = true;
                break;
            case COMMUNITY_LEAVE:
                title = String.format("%s has left %s.", player.getName(), community.getName());
                usePunishmentFooter = true;
                break;
            case COMMUNITY_INVITE:
                title = String.format("%s has been invited by %s.", player.getName(), staffName);
                usePunishmentFooter = true;
                break;
            case COMMUNITY_UNINVITE:
                title = String.format("%s has been uninvited by %s.", player.getName(), staffName);
                usePunishmentFooter = true;
                break;
            default:
                throw new IllegalArgumentException("'type' must be a community system message");
        }

        Embed.Footer footer = null;
        if (usePunishmentFooter) {
            footer = new Embed.Footer(
                    String.format("Kicks: %s   |   Bans: %s", playerData.getKicks(), playerData.getBans()), null
            );
        }

        final Embed embed = Embed.builder().title(title).color(Color.RED).footer(footer).build();
        for (final String webhookLink : cache.getWebhookLinks(community.getName())) {
            WebhookMessage.builder()
                          .url(webhookLink)
                          .avatar_url(UtilReference.MINEPLEX_SMALL_LOGO_URL)
                          .username(community.getName())
                          .embeds(new Embed[]{embed}).build()
                          .post();
        }

    }

    public void postCommunityMessage(final Community.Message message) {
        final PlayerData player = message.getPlayer();
        final MineplexRank rank = player.getMineplexRank();
        final Community community = message.getCommunity();

        final Community.CommunityPlayerData data = community.getPlayerData()
                                                            .stream()
                                                            .filter(data_ -> data_.getUuid().equals(player.getUuid()))
                                                            .findAny()
                                                            .orElse(new Community.CommunityPlayerData(
                                                                    player.getUuid().toString(), 0, 0, 0));

        final int kicks = data.getKicks();
        final int bans = data.getBans();
        final long messages = data.getMessages();
        final String footer = String.format("Kicks: %s   |   Bans: %s   |   Messages: %s", kicks, bans, messages);
        final String prefix = UtilText.getRawTextComponent((TextComponent) rank.getPrefix()) + " ";

        final Color color = Color.decode(rank.getPrefixColor().orElse(NamedTextColor.AQUA).asHexString());

        final Embed embed = Embed.builder()
                                 .color(color)
                                 .footer(new Embed.Footer(footer, null))
                                 .thumbnail(new Embed.Thumbnail(player.getAvatarUrl(), 100, 100))
                                 .description(UtilText.getDiscordCompatibleText(message.getText()))
                                 .title((rank.equals(MineplexRank.PLAYER) ? "" : prefix) + player.getName())
                                 .build();

        final CommunityCache cache = GlobalCacheRepository.getCache(CommunityCache.class);

        final String[] links = cache.getWebhookLinks(community.getName());

        for (final String webhookLink : links) {
            WebhookMessage.builder()
                          .avatar_url(UtilReference.MINEPLEX_SMALL_LOGO_URL)
                          .username(community.getName())
                          .url(webhookLink)
                          .embeds(new Embed[]{embed})
                          .build()
                          .post();
        }
    }

}
