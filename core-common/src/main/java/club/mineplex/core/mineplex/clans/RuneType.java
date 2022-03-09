package club.mineplex.core.mineplex.clans;

import club.mineplex.core.mineplex.clans.runes.RuneFactory;
import club.mineplex.core.mineplex.clans.runes.armor.ArmorRuneFactory;
import club.mineplex.core.mineplex.clans.runes.bow.BowRuneFactory;
import club.mineplex.core.mineplex.clans.runes.weapon.WeaponRuneFactory;

public enum RuneType {

    BOW(BowRuneFactory.getInstance()),
    WEAPON(WeaponRuneFactory.getInstance()),
    ARMOR(ArmorRuneFactory.getInstance());

    private final ClansRune[] runes;

    RuneType(final RuneFactory runeFactory) {
        this.runes = runeFactory.getRunes();
    }

    public ClansRune[] getRunes() {
        return this.runes;
    }

}
