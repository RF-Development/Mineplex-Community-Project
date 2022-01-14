package club.mineplex.core.mineplex.champions.skill.assassin;

import club.mineplex.core.mineplex.champions.ChampionsSkill;
import club.mineplex.core.mineplex.champions.shop.IRechargeable;

public class Illusion extends ChampionsSkill implements IRechargeable {

    public Illusion() {
        super("Illusion", Type.SWORD, 4);
    }

    @Override
    public double getRecharge(final int level) {
        return this.formatValue("#17#-1", level);
    }
}
