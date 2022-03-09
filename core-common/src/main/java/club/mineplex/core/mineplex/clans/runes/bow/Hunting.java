package club.mineplex.core.mineplex.clans.runes.bow;

import club.mineplex.core.mineplex.clans.ClansRune;
import club.mineplex.core.mineplex.clans.shop.IEffectApplier;

public class Hunting extends ClansRune implements IEffectApplier {

    protected Hunting() {
        super("Hunting", UnitType.SECONDS, 1F, 4F, false);
    }

    @Override
    public int getMaxAmplifier() {
        return 2;
    }

}
