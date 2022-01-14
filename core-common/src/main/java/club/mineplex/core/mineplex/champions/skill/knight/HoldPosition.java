package club.mineplex.core.mineplex.champions.skill.knight;

import club.mineplex.core.mineplex.champions.ChampionsSkill;
import club.mineplex.core.mineplex.champions.shop.IRechargeable;

public class HoldPosition extends ChampionsSkill implements IRechargeable {

    public HoldPosition() {
        super("Hold Position", Type.AXE, 5);
    }

    @Override
    public double getRecharge(final int level) {
        return this.formatValue("#16#2", level);
    }

}
