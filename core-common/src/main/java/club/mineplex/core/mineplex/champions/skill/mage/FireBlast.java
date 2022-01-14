package club.mineplex.core.mineplex.champions.skill.mage;

import club.mineplex.core.mineplex.champions.ChampionsSkill;
import club.mineplex.core.mineplex.champions.shop.IEnergyConsumer;
import club.mineplex.core.mineplex.champions.shop.IRechargeable;

public class FireBlast extends ChampionsSkill implements IEnergyConsumer, IRechargeable {

    public FireBlast() {
        super("Fire Blast", Type.AXE, 5);
    }

    @Override
    public double getEnergy(final int level) {
        return this.formatValue("#54#-4", level);
    }

    @Override
    public double getRecharge(final int level) {
        return this.formatValue("#13#-1", level);
    }
}
