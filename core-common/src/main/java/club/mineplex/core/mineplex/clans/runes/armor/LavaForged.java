package club.mineplex.core.mineplex.clans.runes.armor;

import club.mineplex.core.ValueRange;
import club.mineplex.core.mineplex.clans.ClansRune;
import club.mineplex.core.mineplex.clans.RuneType;

public class LavaForged extends ClansRune {

    protected LavaForged() {
        super("Lava Forged", RuneType.SUPER_PREFIX, UnitType.FIRE_PROTECTION, new ValueRange(20F, 100F));
    }

}
