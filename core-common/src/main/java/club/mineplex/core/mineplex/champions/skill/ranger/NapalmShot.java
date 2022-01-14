package club.mineplex.core.mineplex.champions.skill.ranger;

import club.mineplex.core.mineplex.champions.ChampionsSkill;
import club.mineplex.core.mineplex.champions.shop.IPreparableArrow;
import club.mineplex.core.mineplex.champions.shop.IRechargeable;

public class NapalmShot extends ChampionsSkill implements IRechargeable, IPreparableArrow {

    protected NapalmShot() {
        super("Napalm Shot", Type.BOW, 4);
    }

    @Override
    public double getRecharge(final int level) {
        return this.formatValue("#30#-2", level);
    }

}
