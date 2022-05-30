package club.mineplex.core.mineplex.clans.runes.armor;

import club.mineplex.core.ValueRange;
import club.mineplex.core.mineplex.clans.ClansRune;
import club.mineplex.core.mineplex.clans.RuneType;

public class Reinforced extends ClansRune {

    protected Reinforced() {
        super("Reinforced", RuneType.PREFIX, UnitType.PROTECTION, new ValueRange(0.5F, 1F));
    }

}
