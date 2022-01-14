package club.mineplex.core.mineplex.champions.skill.ranger;

import club.mineplex.core.mineplex.champions.ChampionsSkill;
import club.mineplex.core.mineplex.champions.shop.IChargeable;

public class Barrage extends ChampionsSkill implements IChargeable {

    public Barrage() {
        super("Barrage", Type.PASSIVE_A, 3);
    }

    @Override
    public double getChargeAt(final int level) {
        return this.formatValue("#24#8", level);
    }

}
