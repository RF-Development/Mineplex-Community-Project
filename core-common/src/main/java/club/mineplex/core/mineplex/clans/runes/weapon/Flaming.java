package club.mineplex.core.mineplex.clans.runes.weapon;

import club.mineplex.core.mineplex.clans.ClansRune;
import club.mineplex.core.mineplex.clans.shop.IAttackInterval;

public class Flaming extends ClansRune implements IAttackInterval {

    protected Flaming() {
        super("Flaming", UnitType.TICKS, 60F, 120F, false);
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
