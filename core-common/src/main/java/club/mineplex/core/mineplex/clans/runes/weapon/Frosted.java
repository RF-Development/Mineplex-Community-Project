package club.mineplex.core.mineplex.clans.runes.weapon;

import club.mineplex.core.ValueRange;
import club.mineplex.core.mineplex.clans.ClansRune;
import club.mineplex.core.mineplex.clans.RuneType;
import club.mineplex.core.mineplex.clans.shop.IEffectApplier;

public class Frosted extends ClansRune implements IEffectApplier {

    protected Frosted() {
        super("Frosted", RuneType.SUPER_PREFIX, UnitType.TICKS, new ValueRange(20F, 60F));
    }

    @Override
    public int getMaxAmplifier() {
        return 3;
    }
}
