package club.mineplex.core.mineplex.champions.skill.mage;

import club.mineplex.core.mineplex.champions.ChampionsSkill;
import club.mineplex.core.mineplex.champions.skill.SkillFactory;

public class MageFactory extends SkillFactory {

    private static final MageFactory INSTANCE = new MageFactory();

    private MageFactory() {

    }

    public static MageFactory getInstance() {
        return INSTANCE;
    }

    @Override
    protected ChampionsSkill[] createSkills() {
        return new ChampionsSkill[]{
                new StaticLazer(), new Rupture(), new Inferno(), new Blizzard(), // Sword
                new IcePrison(), new Fissure(), new FireBlast(), new LightningOrb(), // Axe
                new Immolate(), new Void(), new LifeBonds(), new ArcticArmor(), // Passive A
                new NullBlade(), new MagmaBlade(), new GlacialBlade(), // Passive B
                new ManaPool(), new ManaRegeneration(), SkillFactory.RESISTANCE_SKILL, SkillFactory.BREAKFALL_SKILL // Global Passive
        };
    }
}
