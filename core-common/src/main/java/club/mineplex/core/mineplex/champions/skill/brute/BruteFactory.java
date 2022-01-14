package club.mineplex.core.mineplex.champions.skill.brute;

import club.mineplex.core.mineplex.champions.ChampionsSkill;
import club.mineplex.core.mineplex.champions.skill.SkillFactory;

public class BruteFactory extends SkillFactory {

    private static final BruteFactory INSTANCE = new BruteFactory();

    private BruteFactory() {

    }

    public static BruteFactory getInstance() {
        return INSTANCE;
    }

    @Override
    protected ChampionsSkill[] createSkills() {
        return new ChampionsSkill[]{
                new FleshHook(), new BlockToss(), new DwarfToss(), // Sword
                new Takedown(), new WhirlwindAxe(), new SeismicSlam(), // Axe
                new Bloodlust(), new Stampede(), new Intimidation(), // Passive A
                new Overwhelm(), new Colossus(), new CripplingBlow(), // Passive C
                SkillFactory.RESISTANCE_SKILL, SkillFactory.BREAKFALL_SKILL // Global Passive
        };
    }

}
