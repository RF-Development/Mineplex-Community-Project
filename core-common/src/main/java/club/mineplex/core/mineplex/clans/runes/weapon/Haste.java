package club.mineplex.core.mineplex.clans.runes.weapon;

import club.mineplex.core.ValueRange;
import club.mineplex.core.mineplex.clans.ClansRune;
import club.mineplex.core.mineplex.clans.RuneType;
import club.mineplex.core.mineplex.clans.shop.IAttackInterval;
import club.mineplex.core.mineplex.clans.shop.IEffectApplier;

public class Haste extends ClansRune implements IEffectApplier, IAttackInterval {

    protected Haste() {
        super("Haste", RuneType.SUFFIX, UnitType.TICKS, new ValueRange(60F, 120F));
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
