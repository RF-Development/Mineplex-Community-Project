package club.mineplex.core.mineplex.champions;

import club.mineplex.core.mineplex.champions.skill.SkillFactory;
import club.mineplex.core.mineplex.champions.skill.assassin.AssassinFactory;
import club.mineplex.core.mineplex.champions.skill.brute.BruteFactory;
import club.mineplex.core.mineplex.champions.skill.knight.KnightFactory;
import club.mineplex.core.mineplex.champions.skill.mage.MageFactory;
import club.mineplex.core.mineplex.champions.skill.ranger.RangerFactory;

import java.util.Arrays;

public enum ChampionsKit {

    BRUTE("Brute", BruteFactory.getInstance()),
    RANGER("Ranger", RangerFactory.getInstance()),
    KNIGHT("Knight", KnightFactory.getInstance()),
    MAGE("Mage", MageFactory.getInstance()),
    ASSASSIN("Assassin", AssassinFactory.getInstance());

    private final ChampionsSkill[] skills;
    private final String name;
    private final boolean usesBow;

    ChampionsKit(final String name, final SkillFactory skillFactory) {
        this.skills = skillFactory.getSkills();
        this.name = name;
        this.usesBow = Arrays.stream(this.skills).anyMatch(skills -> skills.getType().equals(ChampionsSkill.Type.BOW));
    }

    public String getName() {
        return this.name;
    }

    public ChampionsSkill[] getSkills() {
        return this.skills;
    }

    public boolean usesBow() {
        return this.usesBow;
    }

}
