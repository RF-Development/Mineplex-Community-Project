package club.mineplex.discord.events;

import club.mineplex.discord.objects.PagedEmbed;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class PagedEmbedReactions extends ListenerAdapter {

    @Override
    public void onMessageReactionAdd(final MessageReactionAddEvent e) {
        if (e.getMember() == null || e.getMember().getUser().isBot()) {
            return;
        }

        for (final PagedEmbed pagedEmbed : PagedEmbed.getCurrentPagedEmbeds()) {
            if (pagedEmbed.getMessage() == null || pagedEmbed.getMessage().getIdLong() != e.getMessageIdLong()) {
                continue;
            }

            /* Handling reactions */
            PageButton buttonPressed = null;
            switch (e.getReactionEmote().getName()) {
                case "⏮️":
                    buttonPressed = PageButton.START;
                    break;
                case "◀️":
                    buttonPressed = PageButton.PREVIOUS;
                    break;
                case "▶️":
                    buttonPressed = PageButton.NEXT;
                    break;
                case "⏩":
                    buttonPressed = PageButton.END;
                    break;
            }

            if (buttonPressed == null) {
                return;
            }

            switch (buttonPressed) {
                case START:
                    pagedEmbed.setPage(1);
                    break;
                case PREVIOUS:
                    pagedEmbed.setPage(pagedEmbed.getCurrentPage() - 1);
                    break;
                case NEXT:
                    pagedEmbed.setPage(pagedEmbed.getCurrentPage() + 1);
                    break;
                case END:
                    pagedEmbed.setPage(pagedEmbed.getPageCount());
                    break;
            }

            e.getReaction().removeReaction(e.getMember().getUser()).queue();
        }

    }

    enum PageButton {
        START, PREVIOUS, NEXT, END
    }
}
