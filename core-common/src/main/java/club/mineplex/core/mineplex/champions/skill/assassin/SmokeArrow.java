package club.mineplex.core.mineplex.champions.skill.assassin;

import club.mineplex.core.mineplex.champions.ChampionsSkill;
import club.mineplex.core.mineplex.champions.shop.IPreparableArrow;
import club.mineplex.core.mineplex.champions.shop.IRechargeable;

public class SmokeArrow extends ChampionsSkill implements IRechargeable, IPreparableArrow {

    protected SmokeArrow() {
        super("Smoke Arrow", Type.BOW, 4);
    }

    @Override
    public double getRecharge(final int level) {
        return this.formatValue("#20#-2", level);
    }

}
