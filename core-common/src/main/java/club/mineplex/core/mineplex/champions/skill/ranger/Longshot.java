package club.mineplex.core.mineplex.champions.skill.ranger;

import club.mineplex.core.mineplex.champions.ChampionsSkill;
import club.mineplex.core.mineplex.champions.shop.IRechargeable;

public class Longshot extends ChampionsSkill implements IRechargeable {

    protected Longshot() {
        super("Longshot", Type.PASSIVE_B, 3);
    }

    @Override
    public double getRecharge(final int level) {
        return this.formatValue("#7#-1", level);
    }

}
