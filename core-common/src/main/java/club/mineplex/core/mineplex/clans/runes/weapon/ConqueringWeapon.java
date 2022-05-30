package club.mineplex.core.mineplex.clans.runes.weapon;

import club.mineplex.core.ValueRange;
import club.mineplex.core.mineplex.clans.ClansRune;
import club.mineplex.core.mineplex.clans.RuneType;

public class ConqueringWeapon extends ClansRune {

    protected ConqueringWeapon() {
        super("Conquering", RuneType.SUFFIX, UnitType.DAMAGE, new ValueRange(1F, 4F));
    }

}
