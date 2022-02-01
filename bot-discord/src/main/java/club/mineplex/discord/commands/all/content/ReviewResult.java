package club.mineplex.discord.commands.all.content;

import club.mineplex.discord.Main;

import java.awt.*;

public enum ReviewResult {

    EXPLICIT(Color.YELLOW, Main.neutralEmoji),
    ACCEPTED(Color.GREEN, Main.yesEmoji),
    REJECTED(Color.RED, Main.noEmoji);

    private final Color color;
    private final long emoji;

    ReviewResult(final Color color, final long emoji) {
        this.color = color;
        this.emoji = emoji;
    }

    public long getEmoji() {
        return this.emoji;
    }

    public Color getColor() {
        return this.color;
    }

}
