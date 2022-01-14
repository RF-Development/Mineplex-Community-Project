package club.mineplex.core.mineplex.champions;

import club.mineplex.core.mineplex.champions.skill.assassin.*;
import club.mineplex.core.mineplex.champions.skill.brute.*;
import club.mineplex.core.mineplex.champions.skill.knight.*;
import club.mineplex.core.mineplex.champions.skill.mage.Void;
import club.mineplex.core.mineplex.champions.skill.mage.*;
import club.mineplex.core.mineplex.champions.skill.ranger.*;

public enum ChampionsKit {

    BRUTE("Brute", new ChampionsSkill[]{
            new FleshHook(), new BlockToss(), new DwarfToss(), // Sword
            new Takedown(), new WhirlwindAxe(), new SeismicSlam(), // Axe
            new Bloodlust(), new Stampede(), new Intimidation(), // Passive A
            new Overwhelm(), new Colossus(), new CripplingBlow(), // Passive C
            ChampionsBuild.RESISTANCE_SKILL, ChampionsBuild.BREAKFALL_SKILL // Global Passive
    }),

    RANGER("Ranger", new ChampionsSkill[]{
            new WolfsFury(), new Disengage(), // Sword
            new WolfsFury(), new Agility(), // Axe
            new NapalmShot(), new ExplosiveArrow(), new PinDown(), new RopedArrow(), new HealingShot(), // Bow
            new Barrage(), new Overcharge(), new VitalitySpores(), // Passive A
            new Sharpshooter(), new Longshot(), new HeavyArrows(), new BarbedArrows(), // Passive B
            ChampionsBuild.RESISTANCE_SKILL, ChampionsBuild.BREAKFALL_SKILL // Global Passive
    }),

    KNIGHT("Knight", new ChampionsSkill[]{
            new Riposte(), new HiltSmash(), new DefensiveStance(), // Sword
            new BullsCharge(), new ShieldSmash(), new RopedAxeThrow(), new HoldPosition(), // Axe
            new Swordsmanship(), new Cleave(), new Deflection(), // Passive A
            new LevelField(), new Vengeance(), new Fortitude(), // Passive B
            ChampionsBuild.RESISTANCE_SKILL, ChampionsBuild.BREAKFALL_SKILL // Global Passive
    }),

    MAGE("Mage", new ChampionsSkill[]{
            new StaticLazer(), new Rupture(), new Inferno(), new Blizzard(), // Sword
            new IcePrison(), new Fissure(), new FireBlast(), new LightningOrb(), // Axe
            new Immolate(), new Void(), new LifeBonds(), new ArcticArmor(), // Passive A
            new NullBlade(), new MagmaBlade(), new GlacialBlade(), // Passive B
            new ManaPool(), new ManaRegeneration(), ChampionsBuild.RESISTANCE_SKILL, ChampionsBuild.BREAKFALL_SKILL // Global Passive
    }),

    ASSASSIN("Assassin", new ChampionsSkill[]{
            new Illusion(), new Evade(), // Sword
            new Leap(), new Flash(), new Blink(), // Axe
            new SmokeArrow(), new SilencingArrow(), new MarkedForDeath(), // Bow
            new Recall(), new SmokeBomb(), // Passive A
            new Backstab(), new ViperStrikes(), new ComboAttack(), new ShockingStrikes(), // Passive B
            ChampionsBuild.RESISTANCE_SKILL, ChampionsBuild.BREAKFALL_SKILL // Global Passive
    });

    private final ChampionsSkill[] skills;
    private final String name;

    ChampionsKit(final String name, final ChampionsSkill[] skills) {
        this.skills = skills;
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public ChampionsSkill[] getSkills() {
        return this.skills;
    }

}
