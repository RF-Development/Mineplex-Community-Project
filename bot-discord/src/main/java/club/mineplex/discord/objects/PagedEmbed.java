package club.mineplex.discord.objects;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.ArrayList;

public class PagedEmbed {

    private static final ArrayList<PagedEmbed> currentPagedEmbeds = new ArrayList<>();
    private final ArrayList<EmbedBuilder> pages;
    private final TextChannel channel;
    private Message message = null;
    private int currentPage = 1;

    public PagedEmbed(final TextChannel channel, final ArrayList<EmbedBuilder> pages) {
        this.channel = channel;
        this.pages = pages;

        currentPagedEmbeds.add(this);
    }

    public static ArrayList<PagedEmbed> getCurrentPagedEmbeds() {
        return currentPagedEmbeds;
    }

    public int getPageCount() {
        return this.pages.size();
    }

    public Message getMessage() {
        return this.message;
    }

    public int getCurrentPage() {
        return this.currentPage;
    }

    public ArrayList<EmbedBuilder> getAllPages() {
        return this.pages;
    }

    public void setPage(int page) {
        if (this.message == null) {
            this.send();
        }

        page = Math.min(this.getPageCount(), Math.max(1, page));
        if (page > this.pages.size()) {
            page = 1;
        }
        if (page == this.currentPage) {
            return;
        }


        this.message.editMessage(this.pages.get(page - 1).build()).queue();
        this.currentPage = page;
    }

    public void send() {
        this.message = this.channel.sendMessage(this.pages.get(this.currentPage - 1).build()).complete();

        if (this.pages.size() > 1) {
            this.message.addReaction("⏮️").queue();
            this.message.addReaction("◀️").queue();
            this.message.addReaction("▶️").queue();
            this.message.addReaction("⏩").queue();
        }
    }

}
