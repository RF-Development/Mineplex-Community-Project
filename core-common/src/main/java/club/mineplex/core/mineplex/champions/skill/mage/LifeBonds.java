package club.mineplex.core.mineplex.champions.skill.mage;

import club.mineplex.core.mineplex.champions.ChampionsSkill;
import club.mineplex.core.mineplex.champions.shop.IEnergyConsumer;
import club.mineplex.core.mineplex.champions.shop.IToggleable;

public class LifeBonds extends ChampionsSkill implements IToggleable, IEnergyConsumer.PerSecond {

    protected LifeBonds() {
        super("Life Bonds", Type.PASSIVE_A, 3);
    }

    @Override
    public double getEnergy(final int level) {
        return this.formatValue("#9#-1", level);
    }

}
