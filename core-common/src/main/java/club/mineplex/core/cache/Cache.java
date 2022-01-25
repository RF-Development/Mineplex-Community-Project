package club.mineplex.core.cache;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * A storage object with update calls every desired amount of minutes.
 *
 * @param <T> The object we are trying to cache.
 */
public abstract class Cache<T> {

    /**
     * Create a cache, with a default 30 minute interval between update calls.
     */
    public Cache() {
        this(30L);
    }

    /**
     * Create a cache, with a desired minute interval between update calls.
     *
     * @param minuteInterval Number of minutes between each update. If the interval is below or equal to 0, the cache
     *                       will not be updated.
     */
    public Cache(final long minuteInterval) {
        if (minuteInterval >= 0) {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    Cache.this.updateCache();
                }
            }, 0, TimeUnit.MINUTES.toMillis(minuteInterval));
        }
    }

    /**
     * Get the stored cache.
     *
     * @return The cache this object is storing.
     */
    public abstract T get();

    /**
     * Update the cache.
     */
    protected abstract void updateCache();

}
