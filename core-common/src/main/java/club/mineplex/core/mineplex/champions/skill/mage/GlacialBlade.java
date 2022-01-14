package club.mineplex.core.mineplex.champions.skill.mage;

import club.mineplex.core.mineplex.champions.ChampionsSkill;
import club.mineplex.core.mineplex.champions.shop.IEnergyConsumer;
import club.mineplex.core.mineplex.champions.shop.IRechargeable;

public class GlacialBlade extends ChampionsSkill implements IEnergyConsumer, IRechargeable {

    public GlacialBlade() {
        super("Glacial Blade", Type.PASSIVE_B, 3);
    }

    @Override
    public double getEnergy(final int level) {
        return this.formatValue("#11#-2", level);
    }

    @Override
    public double getRecharge(final int level) {
        return this.formatValue("#1#-0.1", level);
    }

}
