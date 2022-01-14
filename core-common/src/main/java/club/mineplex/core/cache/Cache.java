package club.mineplex.core.cache;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public abstract class Cache<T> {

    public Cache() {
        this(30L);
    }

    public Cache(final long minuteInterval) {
        if (minuteInterval > 0) {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    Cache.this.updateCache();
                }
            }, 0, TimeUnit.MINUTES.toMillis(minuteInterval));
        }
    }

    public abstract T get();

    protected abstract void updateCache();

}
