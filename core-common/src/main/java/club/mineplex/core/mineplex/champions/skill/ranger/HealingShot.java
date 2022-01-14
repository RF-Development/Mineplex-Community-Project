package club.mineplex.core.mineplex.champions.skill.ranger;

import club.mineplex.core.mineplex.champions.ChampionsSkill;
import club.mineplex.core.mineplex.champions.shop.IPreparableArrow;
import club.mineplex.core.mineplex.champions.shop.IRechargeable;

public class HealingShot extends ChampionsSkill implements IRechargeable, IPreparableArrow {

    protected HealingShot() {
        super("Healing Shot", Type.BOW, 4);
    }

    @Override
    public double getRecharge(final int level) {
        return this.formatValue("#20#-3", level);
    }

}
