package club.mineplex.core.mineplex;

import lombok.NonNull;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.Optional;

public enum MineplexRank {

    PLAYER("player", "", NamedTextColor.AQUA),
    ULTRA("ultra", "ULTRA", NamedTextColor.AQUA),
    HERO("hero", "HERO", NamedTextColor.LIGHT_PURPLE),
    LEGEND("legend", "LEGEND", NamedTextColor.GREEN),
    TITAL("titan", "TITAN", NamedTextColor.RED),
    ETERNAL("eternal", "ETERNAL", NamedTextColor.DARK_AQUA),
    IMMORTAL("immortal", "IMMORTAL", NamedTextColor.YELLOW),

    TWITCH("twitch", "TWITCH", NamedTextColor.DARK_PURPLE),
    STREAM("stream", "STREAM", NamedTextColor.DARK_PURPLE),
    YT("yt", "YT", NamedTextColor.DARK_PURPLE),
    YOUTUBE("youtube", "YOUTUBE", NamedTextColor.RED),

    BUILDER("builder", "BUILDER", NamedTextColor.BLUE),
    MAPPER("mapper", "MAPPER", NamedTextColor.BLUE),
    MAPLEAD("maplead", "MAPLEAD", NamedTextColor.BLUE),
    TRAINEE("trainee", "TRAINEE", NamedTextColor.GOLD),
    MOD("mod", "MOD", NamedTextColor.GOLD),
    SRMOD("srmod", "SR.MOD", NamedTextColor.GOLD),
    ADMIN("admin", "ADMIN", NamedTextColor.DARK_RED),
    DEV("dev", "DEV", NamedTextColor.DARK_RED),
    LEADER("leader", "LEADER", NamedTextColor.DARK_RED),
    OWNER("owner", "OWNER", NamedTextColor.DARK_RED);

    private final String name;
    private final Component prefix;
    private final NamedTextColor prefixColor;

    MineplexRank(@NonNull final String name, @NonNull final String prefix, final NamedTextColor prefixColor) {
        this.name = name;
        this.prefixColor = prefixColor;
        this.prefix = Component.text(prefix).color(prefixColor).decorate(TextDecoration.BOLD);
    }

    public String getName() {
        return this.name;
    }

    public Component getPrefix() {
        return this.prefix;
    }

    public Optional<NamedTextColor> getPrefixColor() {
        return Optional.ofNullable(this.prefixColor);
    }

}
