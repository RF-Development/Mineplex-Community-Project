package club.mineplex.core.mineplex.champions.skill.brute;

import club.mineplex.core.mineplex.champions.ChampionsSkill;
import club.mineplex.core.mineplex.champions.shop.IChargeable;
import club.mineplex.core.mineplex.champions.shop.IRechargeable;

public class BlockToss extends ChampionsSkill implements IRechargeable, IChargeable {

    public BlockToss() {
        super("Block Toss", Type.SWORD, 5);
    }

    @Override
    public double getChargeAt(final int level) {
        return this.formatValue("#24#8", level);
    }

    @Override
    public double getRecharge(final int level) {
        return 1.5D;
    }

}
