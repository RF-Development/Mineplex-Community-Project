package club.mineplex.core.mineplex.champions.skill.ranger;

import club.mineplex.core.mineplex.champions.ChampionsSkill;
import club.mineplex.core.mineplex.champions.shop.IRechargeable;

public class Agility extends ChampionsSkill implements IRechargeable {

    protected Agility() {
        super("Agility", Type.AXE, 4);
    }

    @Override
    public double getRecharge(final int level) {
        return this.formatValue("#14#1", level);
    }

}
