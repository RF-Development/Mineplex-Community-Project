package club.mineplex.bot.common.mineplex.community;

import club.mineplex.bot.chat.AbstractMessageHandler;
import club.mineplex.bot.common.player.PlayerCache;
import club.mineplex.core.cache.GlobalCacheRepository;
import club.mineplex.core.mineplex.MineplexRank;
import club.mineplex.core.mineplex.community.Community;
import club.mineplex.core.mineplex.player.PlayerData;
import club.mineplex.core.util.UtilText;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.ClientboundChatPacket;
import com.github.steveice10.packetlib.Session;
import lombok.NonNull;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CommunityMessageHandler extends AbstractMessageHandler {

    public CommunityMessageHandler() {
        super("community_message");
    }

    @Override
    public void handle(@NonNull final ClientboundChatPacket packet, @NonNull final Session session) {

        final Component message = packet.getMessage();
        if (!(message instanceof TextComponent)) {
            return;
        }

        // Community messages have exactly three children. Community Name -> Rank (if any) & Username -> Message
        final TextComponent text = (TextComponent) message;
        if (text.children().size() < 3
                || text.children().stream().noneMatch(component -> component instanceof TextComponent)) {
            return;
        }

        final List<TextComponent> children = text.children()
                                                 .stream()
                                                 .map(component -> ((TextComponent) component))
                                                 .collect(Collectors.toList());

        // The community component is the bolded community name and a final space
        final TextComponent first = children.get(0);
        if (!first.content().endsWith(" ")
                || !first.content().trim().equals(first.content().substring(0, first.content().length() - 1))
                || !this.isBoldOnly(first)) {
            return;
        }

        // The username component is the possible staff rank of the user, the username, and a final space, bolded
        final StringBuilder secondText = new StringBuilder();
        for (int i = 1; i < children.size() - 1; i++) {
            final TextComponent second = children.get(i);
            if (!this.isBoldOnly(second)) {
                return;
            }

            secondText.append(second.content());
        }

        // Returning if the username component does not consist of one or two words
        final String[] usernameSplit = secondText.toString().trim().split(" ");
        if (usernameSplit.length != 2 && usernameSplit.length != 1) {
            return;
        }

        // The message component consists only of the raw message with a color, no decorations
        final TextComponent last = children.get(children.size() - 1);
        if (!this.hasNoDecorations(last)) {
            return;
        }

        // We need to verify that the first component is a community we track
        // The component will be trimmed and case-sensitively matched with the tracked communities
        final CommunityCache communityCache = GlobalCacheRepository.getCache(CommunityCache.class);
        final Collection<Community> communities = communityCache.get();
        final Optional<Community> communityOpt = communities
                .stream()
                .filter(tracked -> tracked.getName().equals(first.content().trim()))
                .findAny();
        if (!communityOpt.isPresent()) {
            return;
        }

        // We need to verify there is a staff rank or no rank in general, as well as caching the player username
        // Even if the following checks don't match, we can still save the username if it were to be valid for
        // caching purposes
        final Community community = communityOpt.get();
        final PlayerCache playerCache = GlobalCacheRepository.getCache(PlayerCache.class);
        final String username = usernameSplit[usernameSplit.length - 1];
        final Optional<PlayerData> playerDataOpt = playerCache.savePlayer(username);
        if (!playerDataOpt.isPresent()) {
            return;
        }

        // The rank needs to be checked in order to be valid, but it can also be null
        final String rankName = usernameSplit.length == 2 ? usernameSplit[0] : null;
        final PlayerData playerData = playerDataOpt.get();
        if (rankName != null && rankName.length() != 0) {
            boolean matchedRank = false;
            for (final MineplexRank value : MineplexRank.values()) {
                if (!UtilText.getRawTextComponent(((TextComponent) value.getPrefix())).equalsIgnoreCase(rankName)) {
                    continue;
                }

                matchedRank = true;
                playerData.setMineplexRank(value);
                break;
            }

            // The rank name did not match any of the current ones stored in code
            if (!matchedRank) {
                return;
            }
        }

        final Community.CommunityPlayerData commPlayerData = CommunityManager.getInstance().getPlayerData(
                community,
                playerData.getUuid()
        );

        final String content = last.content();
        commPlayerData.setMessages(commPlayerData.getMessages() + 1);
        CommunityManager.getInstance().postCommunityMessage(new Community.Message(community, playerData, content));
    }

    private boolean isBoldOnly(final TextComponent component) {
        return component.hasDecoration(TextDecoration.BOLD) && !component.hasDecoration(TextDecoration.STRIKETHROUGH)
                && !component.hasDecoration(TextDecoration.UNDERLINED)
                && !component.hasDecoration(TextDecoration.OBFUSCATED)
                && !component.hasDecoration(TextDecoration.ITALIC);
    }

    private boolean hasNoDecorations(final TextComponent component) {
        return !component.hasDecoration(TextDecoration.BOLD) && !component.hasDecoration(TextDecoration.STRIKETHROUGH)
                && !component.hasDecoration(TextDecoration.UNDERLINED)
                && !component.hasDecoration(TextDecoration.OBFUSCATED)
                && !component.hasDecoration(TextDecoration.ITALIC);
    }

}
