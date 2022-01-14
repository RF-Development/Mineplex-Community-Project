package club.mineplex.core.mineplex.champions.skill.assassin;

import club.mineplex.core.mineplex.champions.ChampionsSkill;

public class Blink extends ChampionsSkill {

    protected Blink() {
        super("Blink", Type.AXE, 4);
        this.setSubSkills(new SubSkill[]{new DeBlink()});
    }

    public static class DeBlink extends SubSkill {

        public DeBlink() {
            super("De-Blink", Type.AXE, 4);
        }

        @Override
        protected boolean isIndependent() {
            return true;
        }

    }


}
