package club.mineplex.core.mineplex;

import club.mineplex.core.mineplex.game.MineplexGame;

import java.util.function.Function;
import java.util.regex.Pattern;

public enum MineplexServerType {

    GAME("Game Lobby", true, charSequence -> MineplexGame.fromServer(charSequence) != MineplexGame.NONE),

    LOBBY("Lobby", true, Pattern.compile("Lobby-\\d+")),
    STAFF_LOBBY("Staff Lobby", false, Pattern.compile("Staff-\\d+")),
    CLANSHUB("ClansHub", true, Pattern.compile("ClansHub-\\d+")),
    MPS("MPS", true, Pattern.compile("\\w+-\\d+")),
    UNKNOWN(MineplexServerType.DEFAULT_MINEPLEX_SERVER, true, Pattern.compile(".+"));

    private static final String DEFAULT_MINEPLEX_SERVER = "Unknown Server";
    private final Function<CharSequence, Boolean> matcher;
    private final String name;
    private final boolean isPublic;

    MineplexServerType(final String name, final boolean isPublic, final Pattern serverPattern) {
        this(name, isPublic, server -> server.toString().matches(serverPattern.pattern()));
    }

    MineplexServerType(final String name, final boolean isPublic, final Function<CharSequence, Boolean> matcher) {
        this.name = name;
        this.matcher = matcher;
        this.isPublic = isPublic;
    }

    public static String getDefaultMineplexServer() {
        return DEFAULT_MINEPLEX_SERVER;
    }

    public boolean isPublic() {
        return this.isPublic;
    }

    public String getName() {
        return this.name;
    }

    public boolean doesServerMatch(final CharSequence serverName) {
        return this.matcher.apply(serverName);
    }

}