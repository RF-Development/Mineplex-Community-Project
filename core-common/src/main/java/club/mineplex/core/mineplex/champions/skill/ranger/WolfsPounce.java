package club.mineplex.core.mineplex.champions.skill.ranger;

import club.mineplex.core.mineplex.champions.ChampionsSkill;
import club.mineplex.core.mineplex.champions.shop.IChargeable;
import club.mineplex.core.mineplex.champions.shop.IRechargeable;

public class WolfsPounce extends ChampionsSkill implements IChargeable, IRechargeable {

    public WolfsPounce() {
        super("Wolfs Pounce", Type.SWORD, 4);
    }

    @Override
    public double getChargeAt(final int level) {
        return this.formatValue("#24#8", level);
    }

    @Override
    public double getRecharge(final int level) {
        return this.formatValue("#8#-1", level);
    }
}
