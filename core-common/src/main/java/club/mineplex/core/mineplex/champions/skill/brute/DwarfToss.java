package club.mineplex.core.mineplex.champions.skill.brute;

import club.mineplex.core.mineplex.champions.ChampionsSkill;
import club.mineplex.core.mineplex.champions.shop.IChargeable;
import club.mineplex.core.mineplex.champions.shop.IRechargeable;

public class DwarfToss extends ChampionsSkill implements IRechargeable, IChargeable {

    protected DwarfToss() {
        super("Dwarf Toss", Type.SWORD, 1);
    }

    @Override
    public double getRecharge(final int level) {
        return 16D;
    }

    @Override
    public double getChargeAt(final int level) {
        return 2.5D;
    }

}
