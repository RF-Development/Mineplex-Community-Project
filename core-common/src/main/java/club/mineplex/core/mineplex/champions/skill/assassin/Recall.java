package club.mineplex.core.mineplex.champions.skill.assassin;

import club.mineplex.core.mineplex.champions.ChampionsSkill;
import club.mineplex.core.mineplex.champions.shop.IRechargeable;

public class Recall extends ChampionsSkill implements IRechargeable {

    protected Recall() {
        super("Recall", Type.PASSIVE_A, 3);
        this.setSubSkills(new SubSkill[]{new SecondaryRecall()});
    }

    @Override
    public double getRecharge(final int level) {
        return this.formatValue("#35#-5", level);
    }

    public static class SecondaryRecall extends SubSkill implements IRechargeable {

        public SecondaryRecall() {
            super("Secondary Recall", Type.PASSIVE_A, 3);
        }

        @Override
        public boolean isIndependent() {
            return false;
        }

        @Override
        public double getRecharge(final int level) {
            return this.formatValue("#18#-2", level);
        }
    }

}
