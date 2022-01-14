package club.mineplex.core.mineplex.champions.skill.mage;

import club.mineplex.core.mineplex.champions.ChampionsSkill;
import club.mineplex.core.mineplex.champions.shop.IEnergyConsumer;
import club.mineplex.core.mineplex.champions.shop.IRechargeable;

public class IcePrison extends ChampionsSkill implements IEnergyConsumer, IRechargeable {

    public IcePrison() {
        super("Ice Prison", Type.AXE, 5);
    }

    @Override
    public double getEnergy(final int level) {
        return this.formatValue("#60#-3", level);
    }

    @Override
    public double getRecharge(final int level) {
        return this.formatValue("#21#-1", level);
    }
}
