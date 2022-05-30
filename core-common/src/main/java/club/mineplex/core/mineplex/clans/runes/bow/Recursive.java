package club.mineplex.core.mineplex.clans.runes.bow;

import club.mineplex.core.ValueRange;
import club.mineplex.core.mineplex.clans.ClansRune;
import club.mineplex.core.mineplex.clans.RuneType;

public class Recursive extends ClansRune {

    protected Recursive() {
        super("Recursive", RuneType.PREFIX, UnitType.DAMAGE, new ValueRange(2F, 6F));
    }

}
