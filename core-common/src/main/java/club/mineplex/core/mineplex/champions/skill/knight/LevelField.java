package club.mineplex.core.mineplex.champions.skill.knight;

import club.mineplex.core.mineplex.champions.ChampionsSkill;
import club.mineplex.core.mineplex.champions.shop.IActivatable;
import club.mineplex.core.mineplex.champions.shop.IRechargeable;

public class LevelField extends ChampionsSkill implements IRechargeable, IActivatable {

    public LevelField() {
        super("Level Field", Type.PASSIVE_B, 3);
    }

    @Override
    public double getRecharge(final int level) {
        return this.formatValue("#16#-1", level);
    }

    @Override
    public double applyRechargeAfter(final int level) {
        return this.formatValue("#6#1", level);
    }

}
