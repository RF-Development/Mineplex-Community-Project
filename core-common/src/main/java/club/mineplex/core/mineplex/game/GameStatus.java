package club.mineplex.core.mineplex.game;

public enum GameStatus {

    IN_PROGRESS("In Progress"),
    RECRUITING("Recruiting"),
    ONGOING("Ongoing");

    private final String identifier;

    GameStatus(final String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return this.identifier;
    }

}
