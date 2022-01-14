package club.mineplex.core.mineplex.champions.skill.ranger;

import club.mineplex.core.mineplex.champions.ChampionsSkill;
import club.mineplex.core.mineplex.champions.shop.IPreparable;
import club.mineplex.core.mineplex.champions.shop.IRechargeable;

public class Disengage extends ChampionsSkill implements IRechargeable, IPreparable {

    protected Disengage() {
        super("Disengage", Type.SWORD, 4);
    }

    @Override
    public double getRecharge(final int level) {
        return this.formatValue("#16#-1", level);
    }

    @Override
    public double getPrepareExpire(final int level) {
        return 1D;
    }

}
