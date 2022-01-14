package club.mineplex.core.mineplex.champions.skill.mage;

import club.mineplex.core.mineplex.champions.ChampionsSkill;
import club.mineplex.core.mineplex.champions.shop.IEnergyConsumer;

public class Inferno extends ChampionsSkill implements IEnergyConsumer.PerSecond {

    public Inferno() {
        super("Inferno", Type.SWORD, 5);
    }

    @Override
    public double getEnergy(final int level) {
        return this.formatValue("#34#-1", level);
    }

}
