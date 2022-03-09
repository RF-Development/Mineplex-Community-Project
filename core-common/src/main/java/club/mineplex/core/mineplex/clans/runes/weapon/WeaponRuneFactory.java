package club.mineplex.core.mineplex.clans.runes.weapon;

import club.mineplex.core.mineplex.clans.ClansRune;
import club.mineplex.core.mineplex.clans.runes.RuneFactory;

public class WeaponRuneFactory extends RuneFactory {

    private static final WeaponRuneFactory INSTANCE = new WeaponRuneFactory();

    private WeaponRuneFactory() {

    }

    public static WeaponRuneFactory getInstance() {
        return INSTANCE;
    }

    @Override
    protected ClansRune[] createRunes() {
        return new ClansRune[] {
                new ConqueringWeapon(),
                new Flaming(),
                new Frosted(),
                new Haste(),
                new Jagged(),
                new Sharp()
        };
    }

}
