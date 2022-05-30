package club.mineplex.core.mineplex.clans.runes.bow;

import club.mineplex.core.ValueRange;
import club.mineplex.core.mineplex.clans.ClansRune;
import club.mineplex.core.mineplex.clans.RuneType;

public class Inverse extends ClansRune {

    protected Inverse() {
        super("Inverse", RuneType.PREFIX, UnitType.KNOCKBACK, new ValueRange(50F, 150F));
    }

}
