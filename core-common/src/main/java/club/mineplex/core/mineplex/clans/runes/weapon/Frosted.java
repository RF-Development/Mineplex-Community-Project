package club.mineplex.core.mineplex.clans.runes.weapon;

import club.mineplex.core.mineplex.clans.ClansRune;
import club.mineplex.core.mineplex.clans.shop.IEffectApplier;

public class Frosted extends ClansRune implements IEffectApplier {

    protected Frosted() {
        super("Frosted", UnitType.TICKS, 20F, 60F, false);
    }

    @Override
    public int getMaxAmplifier() {
        return 3;
    }
}
