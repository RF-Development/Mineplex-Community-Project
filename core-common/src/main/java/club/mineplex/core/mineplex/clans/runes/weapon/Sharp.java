package club.mineplex.core.mineplex.clans.runes.weapon;

import club.mineplex.core.ValueRange;
import club.mineplex.core.mineplex.clans.ClansRune;
import club.mineplex.core.mineplex.clans.RuneType;

public class Sharp extends ClansRune {

    protected Sharp() {
        super("Sharp", RuneType.PREFIX, UnitType.DAMAGE, new ValueRange(0.5F, 1.5F));
    }

}
