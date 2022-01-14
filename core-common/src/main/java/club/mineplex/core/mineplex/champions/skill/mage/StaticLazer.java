package club.mineplex.core.mineplex.champions.skill.mage;

import club.mineplex.core.mineplex.champions.ChampionsSkill;
import club.mineplex.core.mineplex.champions.shop.IChargeable;
import club.mineplex.core.mineplex.champions.shop.IEnergyConsumer;
import club.mineplex.core.mineplex.champions.shop.IRechargeable;

public class StaticLazer extends ChampionsSkill implements IChargeable, IRechargeable, IEnergyConsumer.PerSecond {

    protected StaticLazer() {
        super("Static Lazer", Type.SWORD, 5);
    }

    @Override
    public double getChargeAt(final int level) {
        return this.formatValue("#24#8", level);
    }

    @Override
    public double getRecharge(final int level) {
        return this.formatValue("#11#-0.5", level);
    }

    @Override
    public double getEnergy(final int level) {
        return 24D;
    }

}
