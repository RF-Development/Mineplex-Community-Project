package club.mineplex.core.mineplex.champions.skill.ranger;

import club.mineplex.core.mineplex.champions.ChampionsSkill;
import club.mineplex.core.mineplex.champions.shop.IRechargeable;

public class WolfsFury extends ChampionsSkill implements IRechargeable {

    public WolfsFury() {
        super("Wolfs Fury", Type.AXE, 4);
    }

    @Override
    public double getRecharge(final int level) {
        return this.formatValue("#17#2", level);
    }
}
