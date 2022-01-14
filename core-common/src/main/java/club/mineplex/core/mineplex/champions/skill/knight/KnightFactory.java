package club.mineplex.core.mineplex.champions.skill.knight;

import club.mineplex.core.mineplex.champions.ChampionsSkill;
import club.mineplex.core.mineplex.champions.skill.SkillFactory;

public class KnightFactory extends SkillFactory {

    private static final KnightFactory INSTANCE = new KnightFactory();

    private KnightFactory() {

    }

    public static KnightFactory getInstance() {
        return INSTANCE;
    }

    @Override
    protected ChampionsSkill[] createSkills() {
        return new ChampionsSkill[]{
                new Riposte(), new HiltSmash(), new DefensiveStance(), // Sword
                new BullsCharge(), new ShieldSmash(), new RopedAxeThrow(), new HoldPosition(), // Axe
                new Swordsmanship(), new Cleave(), new Deflection(), // Passive A
                new LevelField(), new Vengeance(), new Fortitude(), // Passive B
                SkillFactory.RESISTANCE_SKILL, SkillFactory.BREAKFALL_SKILL // Global Passive
        };
    }

}
