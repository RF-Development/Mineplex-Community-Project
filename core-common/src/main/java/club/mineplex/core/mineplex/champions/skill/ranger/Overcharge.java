package club.mineplex.core.mineplex.champions.skill.ranger;

import club.mineplex.core.mineplex.champions.ChampionsSkill;
import club.mineplex.core.mineplex.champions.shop.IChargeable;

public class Overcharge extends ChampionsSkill implements IChargeable {

    public Overcharge() {
        super("Overcharge", Type.PASSIVE_A, 3);
    }

    @Override
    public double getChargeAt(final int level) {
        return this.formatValue("#24#8", level);
    }

}
