package club.mineplex.core.mineplex.clans;

public abstract class ClansRune {

    private final String name;
    private final UnitType unit;
    private final float minimum;
    private final float maximum;
    private final boolean percentageBased;

    protected ClansRune(final String name, final UnitType unit, final float minimum,
                        final float maximum, final boolean percentageBased) {
        this.name = name;
        this.unit = unit;
        this.minimum = minimum;
        this.maximum = maximum;
        this.percentageBased = percentageBased;
        if (maximum <= minimum) {
            throw new IllegalArgumentException("Maximum value cannot be lower than minimum");
        }
        if (maximum < 0 || minimum < 0) {
            throw new IllegalArgumentException("Range values must be above or equal to 0");
        }
    }

    protected ClansRune(final String name, final UnitType unit) {
        if (unit.hasRange) {
            throw new UnsupportedOperationException(unit.name() + " must have a specified range!");
        }

        this.name = name;
        this.unit = unit;
        this.minimum = -1F;
        this.maximum = -1F;
        this.percentageBased = false;
    }

    public boolean isPercentageBased() {
        if (!this.unit.hasRange) {
            throw new UnsupportedOperationException("This rune does not have a range, and is a unit with no value.");
        }
        return this.percentageBased;
    }

    public final float getMinimum() {
        if (!this.unit.hasRange) {
            throw new UnsupportedOperationException("This rune does not have a range, and is a unit with no value.");
        }
        return this.minimum;
    }

    public final float getMaximum() {
        if (!this.unit.hasRange) {
            throw new UnsupportedOperationException("This rune does not have a range, and is a unit with no value.");
        }
        return this.maximum;
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
