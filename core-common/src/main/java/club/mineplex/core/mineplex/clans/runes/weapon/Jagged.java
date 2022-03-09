package club.mineplex.core.mineplex.clans.runes.weapon;

import club.mineplex.core.mineplex.clans.ClansRune;
import club.mineplex.core.mineplex.clans.shop.IAttackInterval;

public class Jagged extends ClansRune implements IAttackInterval {

    protected Jagged() {
        super("Jagged", UnitType.STUN);
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
