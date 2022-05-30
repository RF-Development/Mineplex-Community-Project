package club.mineplex.core;

import java.util.Random;

public class ValueRange {

    private final Random random = new Random();

    private final float min;
    private final float max;

    public ValueRange(final float min, final float max) {
        this.min = min;
        this.max = max;
    }

    public final float getMin() {
        return this.min;
    }

    public final float getMax() {
        return this.max;
    }

    public float random() {
        return this.min + this.random.nextFloat() * (this.max - this.min);
    }

    public int randomInt() {
        return Math.round(this.random());
    }

}
