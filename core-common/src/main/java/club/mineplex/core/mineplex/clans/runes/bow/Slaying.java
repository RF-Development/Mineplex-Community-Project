package club.mineplex.core.mineplex.clans.runes.bow;

import club.mineplex.core.ValueRange;
import club.mineplex.core.mineplex.clans.ClansRune;
import club.mineplex.core.mineplex.clans.RuneType;

public class Slaying extends ClansRune {

    protected Slaying() {
        super("Slaying", RuneType.SUFFIX, UnitType.DAMAGE, new ValueRange(2F, 12F));
    }

}
