package club.mineplex.discord.events.reactions;

import club.mineplex.discord.BotUtil;
import club.mineplex.discord.Main;
import net.dv8tion.jda.api.entities.EmbedType;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MemeUpvotes extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(final GuildMessageReceivedEvent e) {
        if (e.getAuthor().isBot()) {
            return;
        }

        if (!Main.MEME_CHANNELS.contains(e.getChannel().getIdLong())) {
            return;
        }

        final boolean isValidAttachment =
                e.getMessage().getAttachments().stream().anyMatch(a -> a.isImage() || a.isVideo())
                        || e.getMessage().getEmbeds().stream().anyMatch(z -> z.getType() == EmbedType.VIDEO
                        || z.getType() == EmbedType.IMAGE);

        if (!isValidAttachment) {
            return;
        }

        e.getMessage().addReaction(BotUtil.getEmoji(Main.upvoteEmoji)).queue();
        e.getMessage().addReaction(BotUtil.getEmoji(Main.downvoteEmoji)).queue();
    }


}
