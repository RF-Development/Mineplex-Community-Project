package club.mineplex.core.mineplex.champions.skill.assassin;

import club.mineplex.core.mineplex.champions.ChampionsSkill;
import club.mineplex.core.mineplex.champions.shop.IRechargeable;

public class SmokeBomb extends ChampionsSkill implements IRechargeable {

    public SmokeBomb() {
        super("Smoke Bomb", Type.PASSIVE_A, 3);
    }

    @Override
    public double getRecharge(final int level) {
        return this.formatValue("#45#-5", level);
    }

}
