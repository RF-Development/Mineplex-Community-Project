package club.mineplex.core.mineplex.champions;

import club.mineplex.core.mineplex.champions.skill.BreakFall;
import club.mineplex.core.mineplex.champions.skill.Resistance;
import lombok.Data;
import lombok.NonNull;

import java.util.UUID;

@Data
public class ChampionsBuild {

    final static ChampionsSkill RESISTANCE_SKILL = new Resistance();
    final static ChampionsSkill BREAKFALL_SKILL = new BreakFall();

    @NonNull
    private final UUID uuid;

    @NonNull
    private final ChampionsKit kit;

    private Class<? extends ChampionsSkill> swordSkill;
    private Class<? extends ChampionsSkill> axeSkill;
    private Class<? extends ChampionsSkill> bowSkill;
    private Class<? extends ChampionsSkill> passiveASkill;
    private Class<? extends ChampionsSkill> passiveBSkill;
    private Class<? extends ChampionsSkill> globalPassiveSkill;

}
