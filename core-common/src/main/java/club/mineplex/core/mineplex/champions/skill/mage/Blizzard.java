package club.mineplex.core.mineplex.champions.skill.mage;

import club.mineplex.core.mineplex.champions.ChampionsSkill;
import club.mineplex.core.mineplex.champions.shop.IEnergyConsumer;

public class Blizzard extends ChampionsSkill implements IEnergyConsumer.PerSecond {

    protected Blizzard() {
        super("Blizzard", Type.SWORD, 5);
    }

    @Override
    public double getEnergy(final int level) {
        return this.formatValue("#31#-1", level);
    }

}
