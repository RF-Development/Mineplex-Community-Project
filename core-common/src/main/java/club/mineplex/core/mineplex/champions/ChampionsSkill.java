package club.mineplex.core.mineplex.champions;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NonNull;
import lombok.Setter;

@Data
public abstract class ChampionsSkill {

    @NonNull
    private final String name;
    @NonNull
    private final Type type;
    private final int maxLevel;

    @Setter(AccessLevel.PROTECTED)
    private boolean isBoostedByWeapon = true;

    @Setter(AccessLevel.PROTECTED)
    private SubSkill[] subSkills = new SubSkill[0];

    /**
     * @param name The name of the skill
     * @return An instance of the skill whose name matches with the one provided
     * @throws IllegalArgumentException If the name provided is not a valid skill
     */
    public static ChampionsSkill ofName(final String name) {
        for (final ChampionsKit value : ChampionsKit.values()) {
            for (final ChampionsSkill skill : value.getSkills()) {
                if (!skill.getName().equals(name)) {
                    continue;
                }

                return skill;
            }
        }

        throw new IllegalArgumentException("Name provided is not a valid skill");
    }

    /**
     * Used to obtain a value from a string formula like the following: "#X#Y";
     * where 'X' stands for the base number in the formula, and 'Y' for the modifier,
     * which is then multiplied by the level and added to the base value.
     * <p>
     * Example: "#40#-5"
     * At level one, the formula would return 5.
     * At level 4, the formula would return 20.
     *
     * @param format The string formula for the value. Follows the format "#X#Y", where 'X' is the base value and 'Y'
     *               the modifier per level.
     * @param level  The level to apply the formula with.
     * @return The result of the formula applied with the level provided.
     */
    protected double formatValue(@NonNull final String format, final int level) {
        final String[] split = format.substring(1).split("#");
        final double baseValue = Double.parseDouble(split[0]);
        final double incrementalValue = Double.parseDouble(split[1]);

        return baseValue + (level * incrementalValue);
    }

    public enum Type {

        SWORD,
        AXE,
        BOW,
        PASSIVE_A,
        PASSIVE_B,
        GLOBAL_PASSIVE

    }

    public abstract static class SubSkill extends ChampionsSkill {

        public SubSkill(@NonNull final String name, @NonNull final Type type, final int maxLevel) {
            super(name, type, maxLevel);
        }

        /**
         * An independent {@link SubSkill} has a separate cooldown from its parent, and has unique
         * attributes that only apply to itself. If an ability is not independent, the cooldown
         * applied when using it, and all other conditions, will be applied by the parent, and are,
         * therefore, shared.
         *
         * @return Whether this subskill is independent of its parent
         */
        public abstract boolean isIndependent();

    }

}
