package club.mineplex.core.mineplex.champions.skill.assassin;

import club.mineplex.core.mineplex.champions.ChampionsSkill;
import club.mineplex.core.mineplex.champions.shop.IPreparableArrow;
import club.mineplex.core.mineplex.champions.shop.IRechargeable;

public class MarkedForDeath extends ChampionsSkill implements IRechargeable, IPreparableArrow {

    protected MarkedForDeath() {
        super("Marked for Death", Type.BOW, 4);
    }

    @Override
    public double getRecharge(final int level) {
        return this.formatValue("#20#-2", level);
    }

}
