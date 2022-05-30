package club.mineplex.core.mineplex.clans.runes.bow;

import club.mineplex.core.ValueRange;
import club.mineplex.core.mineplex.clans.ClansRune;
import club.mineplex.core.mineplex.clans.RuneType;
import club.mineplex.core.mineplex.clans.shop.IEffectApplier;

public class Hunting extends ClansRune implements IEffectApplier {

    protected Hunting() {
        super("Hunting", RuneType.SUPER_PREFIX, UnitType.SECONDS, new ValueRange(1F, 4F));
    }

    @Override
    public int getMaxAmplifier() {
        return 2;
    }

}
