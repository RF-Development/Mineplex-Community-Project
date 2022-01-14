package club.mineplex.core.mineplex.champions.skill.assassin;

import club.mineplex.core.mineplex.champions.ChampionsSkill;
import club.mineplex.core.mineplex.champions.shop.IPreparable;
import club.mineplex.core.mineplex.champions.shop.IRechargeable;

public class Evade extends ChampionsSkill implements IRechargeable, IPreparable {

    protected Evade() {
        super("Evade", Type.SWORD, 1);
    }

    @Override
    public double getRecharge(final int level) {
        return 0.1D;
    }

    @Override
    public double getPrepareExpire(final int level) {
        return 2D;
    }

    @Override
    public double getFailRecharge(final int level) {
        return 16D;
    }

}
