package club.mineplex.core.mineplex.clans.runes.weapon;

import club.mineplex.core.ValueRange;
import club.mineplex.core.mineplex.clans.ClansRune;
import club.mineplex.core.mineplex.clans.RuneType;
import club.mineplex.core.mineplex.clans.shop.IAttackInterval;

public class Flaming extends ClansRune implements IAttackInterval {

    protected Flaming() {
        super("Flaming", RuneType.SUPER_PREFIX, UnitType.TICKS, new ValueRange(60F, 120F));
    }

    @Override
    public int getMinAttacks() {
        return 2;
    }

    @Override
    public int getMaxAttacks() {
        return 4;
    }
}
