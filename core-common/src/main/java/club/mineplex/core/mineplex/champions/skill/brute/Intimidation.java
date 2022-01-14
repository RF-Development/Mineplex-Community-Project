package club.mineplex.core.mineplex.champions.skill.brute;

import club.mineplex.core.mineplex.champions.ChampionsSkill;
import club.mineplex.core.mineplex.champions.shop.IActivatable;
import club.mineplex.core.mineplex.champions.shop.IRechargeable;

public class Intimidation extends ChampionsSkill implements IRechargeable, IActivatable {

    public Intimidation() {
        super("Intimidation", Type.PASSIVE_A, 5);
    }

    @Override
    public double getRecharge(final int level) {
        return this.formatValue("#15#-1.5", level);
    }

    @Override
    public double applyRechargeAfter(final int level) {
        return this.formatValue("#10#1", level);
    }

}
