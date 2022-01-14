package club.mineplex.core.mineplex.champions.skill.brute;

import club.mineplex.core.mineplex.champions.ChampionsSkill;
import club.mineplex.core.mineplex.champions.shop.IChargeable;
import club.mineplex.core.mineplex.champions.shop.IRechargeable;

public class FleshHook extends ChampionsSkill implements IChargeable, IRechargeable {

    public FleshHook() {
        super("Flesh Hook", Type.SWORD, 5);
    }

    @Override
    public double getChargeAt(final int level) {
        return this.formatValue("#40#10", level);
    }

    @Override
    public double getRecharge(final int level) {
        return this.formatValue("#15#-1", level);
    }

}
