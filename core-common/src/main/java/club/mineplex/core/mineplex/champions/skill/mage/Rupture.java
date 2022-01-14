package club.mineplex.core.mineplex.champions.skill.mage;

import club.mineplex.core.mineplex.champions.ChampionsSkill;
import club.mineplex.core.mineplex.champions.shop.IChargeable;
import club.mineplex.core.mineplex.champions.shop.IEnergyConsumer;

public class Rupture extends ChampionsSkill implements IChargeable, IEnergyConsumer.PerSecond {

    protected Rupture() {
        super("Rupture", Type.SWORD, 5);
    }

    @Override
    public double getChargeAt(final int level) {
        return this.formatValue("#40#10", level);
    }

    @Override
    public double getEnergy(final int level) {
        return this.formatValue("#20#-1", level);
    }

}
