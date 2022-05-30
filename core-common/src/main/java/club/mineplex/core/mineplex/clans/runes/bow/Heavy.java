package club.mineplex.core.mineplex.clans.runes.bow;

import club.mineplex.core.ValueRange;
import club.mineplex.core.mineplex.clans.ClansRune;
import club.mineplex.core.mineplex.clans.RuneType;

public class Heavy extends ClansRune {

    protected Heavy() {
        super("Heavy", RuneType.PREFIX, UnitType.KNOCKBACK, new ValueRange(25F, 75F));
    }

}
