package club.mineplex.core.mineplex.clans;

import club.mineplex.core.ValueRange;

public abstract class ClansRune {

    private final String name;
    private final UnitType unit;
    private final RuneType type;
    private final ValueRange valueRange;

    protected ClansRune(final String name, final RuneType type, final UnitType unit, final ValueRange valueRange) {
        this.name = name;
        this.unit = unit;
        this.type = type;
        if (!this.unit.hasRange && valueRange != null) {
            throw new UnsupportedOperationException("This rune cannot have a range because it's a unit with no value.");
        }
        this.valueRange = valueRange;
    }

    protected ClansRune(final String name, final RuneType type, final UnitType unit) {
        this(name, type, unit, null);
    }

    public RuneType getType() {
        return this.type;
    }

    public boolean isPercentageBased() {
        if (!this.unit.hasRange) {
            throw new UnsupportedOperationException("This rune does not have a range, and is a unit with no value.");
        }
        return this.valueRange != null;
    }

    public ValueRange getValueRange() {
        if (!this.unit.hasRange) {
            throw new UnsupportedOperationException("This rune does not have a range, and is a unit with no value.");
        }
        return this.valueRange;
    }

    public final String getName() {
        return this.name;
    }

    public final UnitType getUnit() {
        return this.unit;
    }

    public enum UnitType {
        PROTECTION, FIRE_PROTECTION, TICKS, DAMAGE, KNOCKBACK, HEALTH, SECONDS, STUN(false), FALL_PROTECTION;

        private final boolean hasRange;

        UnitType(final boolean hasRange) {
            this.hasRange = hasRange;
        }

        UnitType() {
            this(true);
        }

        public boolean hasRange() {
            return this.hasRange;
        }
    }

}
