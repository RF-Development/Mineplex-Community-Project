package club.mineplex.core.mineplex.champions.skill.brute;

import club.mineplex.core.mineplex.champions.ChampionsSkill;
import club.mineplex.core.mineplex.champions.shop.IRechargeable;

public class Takedown extends ChampionsSkill implements IRechargeable {

    protected Takedown() {
        super("Takedown", Type.AXE, 5);
    }

    @Override
    public double getRecharge(final int level) {
        return this.formatValue("#17#-1", level);
    }
}
