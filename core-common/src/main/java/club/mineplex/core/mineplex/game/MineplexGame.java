package club.mineplex.core.mineplex.game;

import java.util.Arrays;

public enum MineplexGame {

    MASTER_BUILDERS("Master Builders", "BLD"),
    DRAW_MY_THING("Draw My Thing", "DMT"),
    SUPER_PAINTBALL("Super Paintball", "PB"),
    NANO_GAMES("Nano Games", "NANO"),
    MICRO_BATTLES("Micro Battle", "MB"),
    TURF_WARS("Turf Wars", "TF"),
    DRAGON_ESCAPE("Dragon Escape", "DE"),
    SPEED_BUILDERS("Speed Builders", "SB"),
    BLOCK_HUNT("Block Hunt", "BH"),
    CAKEWARS_STANDARD("Cake Wars Standard", "CW4"),
    CAKEWARS_DUOS("Cake Wars Duos", "CW2"),
    CAKEWARS_SOLO("Cake Wars Solo", "CW1"),
    SURVIVAL_GAMES_TEAMS("Survival Games Teams", "SG2"),
    SURVIVAL_GAMES_SOLO("Survival Games Solo", "SG"),
    SKYWARS_TEAMS("Skywars Teams", "SKY2"),
    SKYWARS_SOLO("Skywars", "SKY"),
    UHC("UHC", "UHC"),
    BRIDGES("The Bridges", "BR"),
    MINESTRIKE("Mine-Strike", "MS"),
    SUPER_SMASH_MOBS_TEAMS("Super Smash Mobs Teams", "SSM2"),
    SUPER_SMASH_MOBS_SOLO("Super Smash Mobs", "SSM"),
    CHAMPIONS_DOMINATE("Champions Domination", "DOM"),
    CHAMPIONS_CTF("Champions CTF", "CTF"),
    CLANS("Clans", "Clans"),

    // Mixed Arcade
    MIXED_ARCADE("Mixed Arcade", "MIN"),


    NONE("Unknown Game", null);

    private final String shortName;
    private final String name;

    MineplexGame(final String name, final String shortName) {
        this.name = name;
        this.shortName = shortName;
    }

    public static MineplexGame fromServer(final CharSequence server) {
        return Arrays.stream(MineplexGame.values())
                     .filter(game -> game.getShortName() != null)
                     .filter(game -> server.toString().matches(game.getShortName() + "-\\d+"))
                     .findFirst()
                     .orElse(MineplexGame.NONE);
    }

    public String getName() {
        return this.name;
    }

    public String getShortName() {
        return this.shortName;
    }

}