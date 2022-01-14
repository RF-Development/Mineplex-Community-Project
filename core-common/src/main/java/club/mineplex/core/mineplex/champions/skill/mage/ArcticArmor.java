package club.mineplex.core.mineplex.champions.skill.mage;

import club.mineplex.core.mineplex.champions.ChampionsSkill;
import club.mineplex.core.mineplex.champions.shop.IEnergyConsumer;
import club.mineplex.core.mineplex.champions.shop.IToggleable;

public class ArcticArmor extends ChampionsSkill implements IToggleable, IEnergyConsumer.PerSecond {

    public ArcticArmor() {
        super("Arctic Armor", Type.PASSIVE_A, 3);
    }

    @Override
    public double getEnergy(final int level) {
        return this.formatValue("#11#-1", level);
    }

}
