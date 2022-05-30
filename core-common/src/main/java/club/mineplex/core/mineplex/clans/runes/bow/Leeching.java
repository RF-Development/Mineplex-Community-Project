package club.mineplex.core.mineplex.clans.runes.bow;

import club.mineplex.core.ValueRange;
import club.mineplex.core.mineplex.clans.ClansRune;
import club.mineplex.core.mineplex.clans.RuneType;

public class Leeching extends ClansRune {

    protected Leeching() {
        super("Leeching", RuneType.SUPER_PREFIX, UnitType.HEALTH, new ValueRange(5F, 15F));
    }

}
