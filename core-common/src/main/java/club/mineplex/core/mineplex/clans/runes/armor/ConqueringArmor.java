package club.mineplex.core.mineplex.clans.runes.armor;

import club.mineplex.core.ValueRange;
import club.mineplex.core.mineplex.clans.ClansRune;
import club.mineplex.core.mineplex.clans.RuneType;

public class ConqueringArmor extends ClansRune {

    protected ConqueringArmor() {
        super("Conquering", RuneType.SUFFIX, UnitType.PROTECTION, new ValueRange(97.5F, 93.5F));
    }

}
