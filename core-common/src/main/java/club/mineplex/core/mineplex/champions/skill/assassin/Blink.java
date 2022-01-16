package club.mineplex.core.mineplex.champions.skill.assassin;

import club.mineplex.core.mineplex.champions.ChampionsSkill;
import club.mineplex.core.mineplex.champions.shop.IRechargeable;

public class Blink extends ChampionsSkill implements IRechargeable {

    protected Blink() {
        super("Blink", Type.AXE, 4);
        this.setSubSkills(new SubSkill[]{new DeBlink()});
    }

    @Override
    public double getRecharge(final int level) {
        return 12.0D;
    }

    public static class DeBlink extends SubSkill {

        public DeBlink() {
            super("De-Blink", Type.AXE, 4);
        }

        @Override
        public boolean isIndependent() {
            return true;
        }

    }


}
