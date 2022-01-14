package club.mineplex.core.mineplex.champions.skill.knight;

import club.mineplex.core.mineplex.champions.ChampionsSkill;
import club.mineplex.core.mineplex.champions.shop.IRechargeable;

public class RopedAxeThrow extends ChampionsSkill implements IRechargeable {

    protected RopedAxeThrow() {
        super("Roped Axe Throw", Type.AXE, 5);
    }

    @Override
    public double getRecharge(final int level) {
        return this.formatValue("#4.3#-0.3", level);
    }

}
