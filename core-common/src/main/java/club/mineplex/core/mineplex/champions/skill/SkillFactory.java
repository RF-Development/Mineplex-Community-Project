package club.mineplex.core.mineplex.champions.skill;

import club.mineplex.core.mineplex.champions.ChampionsSkill;

public abstract class SkillFactory {

    protected final static ChampionsSkill RESISTANCE_SKILL = new Resistance();
    protected final static ChampionsSkill BREAKFALL_SKILL = new BreakFall();

    private final ChampionsSkill[] skills;

    protected SkillFactory() {
        this.skills = this.createSkills();
    }

    protected abstract ChampionsSkill[] createSkills();

    public ChampionsSkill[] getSkills() {
        return this.skills;
    }

}
