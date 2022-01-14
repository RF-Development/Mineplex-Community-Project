package club.mineplex.core.mineplex.champions.skill.ranger;

import club.mineplex.core.mineplex.champions.ChampionsSkill;
import club.mineplex.core.mineplex.champions.shop.IRechargeable;

public class PinDown extends ChampionsSkill implements IRechargeable {

    protected PinDown() {
        super("Pin Down", Type.BOW, 4);
    }

    @Override
    public double getRecharge(final int level) {
        return this.formatValue("#13#-1", level);
    }

}
