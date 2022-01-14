package club.mineplex.core.mineplex.champions.skill.mage;

import club.mineplex.core.mineplex.champions.ChampionsSkill;
import club.mineplex.core.mineplex.champions.shop.IEnergyConsumer;
import club.mineplex.core.mineplex.champions.shop.IRechargeable;

public class LightningOrb extends ChampionsSkill implements IEnergyConsumer, IRechargeable {

    protected LightningOrb() {
        super("Lightning Orb", Type.AXE, 5);
    }

    @Override
    public double getEnergy(final int level) {
        return this.formatValue("#60#-2", level);
    }

    @Override
    public double getRecharge(final int level) {
        return this.formatValue("#13#-1", level);
    }
}
