package club.mineplex.core.mineplex.clans.runes.bow;

import club.mineplex.core.ValueRange;
import club.mineplex.core.mineplex.clans.ClansRune;
import club.mineplex.core.mineplex.clans.RuneType;

public class Scorching extends ClansRune {

    protected Scorching() {
        super("Scorching", RuneType.SUPER_PREFIX, UnitType.SECONDS, new ValueRange(2F, 6F));
    }

}
