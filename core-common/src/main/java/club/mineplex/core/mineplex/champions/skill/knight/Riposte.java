package club.mineplex.core.mineplex.champions.skill.knight;

import club.mineplex.core.mineplex.champions.ChampionsSkill;
import club.mineplex.core.mineplex.champions.shop.IPreparable;
import club.mineplex.core.mineplex.champions.shop.IRechargeable;

public class Riposte extends ChampionsSkill implements IRechargeable, IPreparable {

    protected Riposte() {
        super("Riposte", Type.SWORD, 5);
    }

    @Override
    public double getRecharge(final int level) {
        return this.formatValue("#15#-1", level);
    }

    @Override
    public double getPrepareExpire(final int level) {
        return 1D;
    }

}
