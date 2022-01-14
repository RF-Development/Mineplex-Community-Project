package club.mineplex.core.mineplex.champions.skill.mage;

import club.mineplex.core.mineplex.champions.ChampionsSkill;
import club.mineplex.core.mineplex.champions.shop.IEnergyConsumer;
import club.mineplex.core.mineplex.champions.shop.IToggleable;

public class Immolate extends ChampionsSkill implements IToggleable, IEnergyConsumer.PerSecond {

    public Immolate() {
        super("Immolate", Type.PASSIVE_A, 1);
    }

    @Override
    public double getEnergy(final int level) {
        return 14D;
    }

}
