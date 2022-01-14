package club.mineplex.core.mineplex.champions.skill.knight;

import club.mineplex.core.mineplex.champions.ChampionsSkill;
import club.mineplex.core.mineplex.champions.shop.IRechargeable;

public class BullsCharge extends ChampionsSkill implements IRechargeable {

    protected BullsCharge() {
        super("Bulls Charge", Type.AXE, 5);
    }

    @Override
    public double getRecharge(final int level) {
        return this.formatValue("#10#1", level);
    }

}
