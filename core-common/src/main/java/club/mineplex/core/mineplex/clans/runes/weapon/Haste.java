package club.mineplex.core.mineplex.clans.runes.weapon;

import club.mineplex.core.mineplex.clans.ClansRune;
import club.mineplex.core.mineplex.clans.shop.IAttackInterval;
import club.mineplex.core.mineplex.clans.shop.IEffectApplier;

public class Haste extends ClansRune implements IEffectApplier, IAttackInterval {

    protected Haste() {
        super("Haste", UnitType.TICKS, 60F, 120F, false);
    }

    @Override
    public int getMinAttacks() {
        return 2;
    }

    @Override
    public int getMaxAttacks() {
        return 4;
    }

    @Override
    public int getMaxAmplifier() {
        return 2;
    }

}
