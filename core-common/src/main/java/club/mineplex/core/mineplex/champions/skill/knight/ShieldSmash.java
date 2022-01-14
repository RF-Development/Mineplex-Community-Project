package club.mineplex.core.mineplex.champions.skill.knight;

import club.mineplex.core.mineplex.champions.ChampionsSkill;
import club.mineplex.core.mineplex.champions.shop.IRechargeable;

public class ShieldSmash extends ChampionsSkill implements IRechargeable {

    public ShieldSmash() {
        super("Shield Smash", Type.AXE, 5);
    }

    @Override
    public double getRecharge(final int level) {
        return this.formatValue("#15#-1", level);
    }

}
