package club.mineplex.core.mineplex.clans.runes.armor;

import club.mineplex.core.ValueRange;
import club.mineplex.core.mineplex.clans.ClansRune;
import club.mineplex.core.mineplex.clans.RuneType;

public class Padded extends ClansRune {

    protected Padded() {
        super("Padded", RuneType.PREFIX, UnitType.FALL_PROTECTION, new ValueRange(1F, 4F));
    }

}
