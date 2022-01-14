package club.mineplex.core.mineplex.champions.skill.ranger;

import club.mineplex.core.mineplex.champions.ChampionsSkill;
import club.mineplex.core.mineplex.champions.shop.IPreparableArrow;
import club.mineplex.core.mineplex.champions.shop.IRechargeable;

public class ExplosiveArrow extends ChampionsSkill implements IRechargeable, IPreparableArrow {

    public ExplosiveArrow() {
        super("Explosive Arrow", Type.BOW, 4);
    }

    @Override
    public double getRecharge(final int level) {
        return this.formatValue("#22#-2", level);
    }

}
