package club.mineplex.core.mineplex.champions.skill.ranger;

import club.mineplex.core.mineplex.champions.ChampionsSkill;
import club.mineplex.core.mineplex.champions.skill.SkillFactory;

public class RangerFactory extends SkillFactory {

    private static final RangerFactory INSTANCE = new RangerFactory();

    private RangerFactory() {

    }

    public static RangerFactory getInstance() {
        return INSTANCE;
    }

    @Override
    protected ChampionsSkill[] createSkills() {
        return new ChampionsSkill[]{
                new WolfsPounce(), new Disengage(), // Sword
                new WolfsFury(), new Agility(), // Axe
                new NapalmShot(), new ExplosiveArrow(), new PinDown(), new RopedArrow(), new HealingShot(), // Bow
                new Barrage(), new Overcharge(), new VitalitySpores(), // Passive A
                new Sharpshooter(), new Longshot(), new HeavyArrows(), new BarbedArrows(), // Passive B
                SkillFactory.RESISTANCE_SKILL, SkillFactory.BREAKFALL_SKILL // Global Passive
        };
    }

}
