package club.mineplex.core.mineplex.champions;

import club.mineplex.core.mineplex.champions.skill.BreakFall;
import club.mineplex.core.mineplex.champions.skill.Resistance;
import lombok.NonNull;
import lombok.Value;

import java.util.UUID;

@Value
public class ChampionsBuild {

    final static ChampionsSkill RESISTANCE_SKILL = new Resistance();
    final static ChampionsSkill BREAKFALL_SKILL = new BreakFall();

    @NonNull
    UUID uuid;

    @NonNull
    ChampionsKit kit;

    Class<? extends ChampionsSkill> swordSkill;
    Class<? extends ChampionsSkill> axeSkill;
    Class<? extends ChampionsSkill> bowSkill;
    Class<? extends ChampionsSkill> passiveASkill;
    Class<? extends ChampionsSkill> passiveBSkill;
    Class<? extends ChampionsSkill> globalPassiveSkill;

}
