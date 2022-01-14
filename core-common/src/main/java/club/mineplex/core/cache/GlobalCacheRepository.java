package club.mineplex.core.cache;

import lombok.NonNull;
import org.apache.commons.lang3.Validate;

import java.util.HashMap;
import java.util.Map;

public class GlobalCacheRepository {

    private static final Map<Class<? extends Cache>, Cache> caches = new HashMap<>();

    private GlobalCacheRepository() {
    }

    public static <T> void register(@NonNull final Cache<T> cache) {
        Validate.isTrue(!caches.containsKey(cache.getClass()), "%s is already a globally registered cache",
                        cache.getClass().getName()
        );
        caches.put(cache.getClass(), cache);
    }

    public static <T extends Cache<?>> T getCache(@NonNull final Class<T> clazz) {
        Validate.isTrue(caches.containsKey(clazz), "%s is not a globally registered cache", clazz.getName());
        return clazz.cast(caches.get(clazz));
    }

}
