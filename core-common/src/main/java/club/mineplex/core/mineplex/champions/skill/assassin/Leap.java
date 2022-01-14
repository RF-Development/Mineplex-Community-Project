package club.mineplex.core.mineplex.champions.skill.assassin;

import club.mineplex.core.mineplex.champions.ChampionsSkill;
import club.mineplex.core.mineplex.champions.shop.IRechargeable;

public class Leap extends ChampionsSkill implements IRechargeable {

    public Leap() {
        super("Leap", Type.AXE, 4);
        this.setSubSkills(new SubSkill[]{new WallKick()});
    }

    @Override
    public double getRecharge(final int level) {
        return this.formatValue("#10.5#-1.5", level);
    }

    public static class WallKick extends SubSkill implements IRechargeable {

        public WallKick() {
            super("Wall Kick", Type.AXE, 4);
        }

        @Override
        protected boolean isIndependent() {
            return true;
        }

        @Override
        public double getRecharge(final int level) {
            return 1.5D;
        }
    }

}
