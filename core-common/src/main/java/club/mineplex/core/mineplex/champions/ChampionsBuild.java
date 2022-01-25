package club.mineplex.core.mineplex.champions;

import lombok.*;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

/**
 * A data class storing all build information bound to a user's UUID, like the {@link ChampionsKit kit} and the {@link
 * ChampionsSkill skills} mapped by {@link ChampionsSkill.Type skill type}.
 */
@Data
@RequiredArgsConstructor
public class ChampionsBuild {

    @NonNull
    private final UUID uuid;

    @NonNull
    private final ChampionsKit kit;

    @NonNull
    @Getter(AccessLevel.PRIVATE)
    private final HashMap<ChampionsSkill.Type, Pair<ChampionsSkill, Integer>> skillMap = new HashMap<>();

    /**
     * Sets a skill's level within the build
     *
     * @param skillType The skill type
     * @param skill     The skill instance
     * @param level     The level to set the skill to within the build
     * @return The same instance of the ChampionsBuild being modified
     */
    public final ChampionsBuild setSkill(@NonNull final ChampionsSkill.Type skillType,
                                         @NonNull final ChampionsSkill skill,
                                         final int level) {
        Validate.isTrue(level > 0, "A skill within a build has to be above level 0");
        Validate.isTrue(level <= skill.getMaxLevel(), "%s can only go up to level %s", skill.getName(),
                        skill.getMaxLevel()
        );
        Validate.isTrue(skillType == skill.getType(), "The skill and type provided do not match");
        Validate.isTrue(Arrays.asList(this.kit.getSkills()).contains(skill),
                        "The skill provided does not much the build kit"
        );

        this.skillMap.put(skillType, Pair.of(skill, level));
        return this;
    }

    /**
     * Removes a skill from the build, therefore making the slot empty
     *
     * @param skillType The skill type to remove
     * @return The same instance of the ChampionsBuild being modified
     */
    public final ChampionsBuild removeSkill(final ChampionsSkill.Type skillType) {
        this.skillMap.put(skillType, null);
        return this;
    }

    /**
     * Get a ChampionsSkill and its level within the build.
     *
     * @param skillType The {@link ChampionsSkill.Type skill type} to query within the build.
     * @return An empty Optional if the skill type is not present in the build, or an Optional filled with a Pair of the
     * ChampionsSkill and its level within the build
     */
    public final Optional<Pair<ChampionsSkill, Integer>> getSkill(final ChampionsSkill.Type skillType) {
        if (!this.skillMap.containsKey(skillType)) {
            return Optional.empty();
        }

        return Optional.ofNullable(this.skillMap.get(skillType));
    }

}
