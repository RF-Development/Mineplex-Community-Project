package club.mineplex.core.mineplex.champions.skill.brute;

import club.mineplex.core.mineplex.champions.ChampionsSkill;
import club.mineplex.core.mineplex.champions.shop.IRechargeable;

public class SeismicSlam extends ChampionsSkill implements IRechargeable {

    protected SeismicSlam() {
        super("Seismic Slam", Type.AXE, 5);
    }

    @Override
    public double getRecharge(final int level) {
        return this.formatValue("#21#-1", level);
    }

}
