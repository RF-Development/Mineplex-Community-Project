package club.mineplex.core.mineplex.champions.skill.knight;

import club.mineplex.core.mineplex.champions.ChampionsSkill;
import club.mineplex.core.mineplex.champions.shop.IRechargeable;

public class HiltSmash extends ChampionsSkill implements IRechargeable {

    protected HiltSmash() {
        super("Hilt Smash", Type.SWORD, 5);
    }

    @Override
    public double getRecharge(final int level) {
        return this.formatValue("#15#-1", level);
    }

}
