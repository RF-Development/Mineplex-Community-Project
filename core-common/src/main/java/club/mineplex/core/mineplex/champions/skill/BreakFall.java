package club.mineplex.core.mineplex.champions.skill;

import club.mineplex.core.mineplex.champions.ChampionsSkill;

public class BreakFall extends ChampionsSkill {

    final static ChampionsSkill BREAKFALL_SKILL = new BreakFall();

    public BreakFall() {
        super("Break Fall", Type.GLOBAL_PASSIVE, 3);
    }

}
