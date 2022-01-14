package club.mineplex.core.mineplex.champions.skill.assassin;

import club.mineplex.core.mineplex.champions.ChampionsSkill;
import club.mineplex.core.mineplex.champions.skill.SkillFactory;

public class AssassinFactory extends SkillFactory {

    private static final AssassinFactory INSTANCE = new AssassinFactory();

    private AssassinFactory() {

    }

    public static AssassinFactory getInstance() {
        return INSTANCE;
    }

    @Override
    protected ChampionsSkill[] createSkills() {
        return new ChampionsSkill[]{
                new Illusion(), new Evade(), // Sword
                new Leap(), new Flash(), new Blink(), // Axe
                new SmokeArrow(), new SilencingArrow(), new MarkedForDeath(), // Bow
                new Recall(), new SmokeBomb(), // Passive A
                new Backstab(), new ViperStrikes(), new ComboAttack(), new ShockingStrikes(), // Passive B
                SkillFactory.RESISTANCE_SKILL, SkillFactory.BREAKFALL_SKILL // Global Passive
        };
    }

}
