package club.mineplex.core.cache;

import lombok.NonNull;
import org.apache.commons.lang3.Validate;

import java.util.HashMap;
import java.util.Map;

/**
 * Global public cache repository. This is where all singleton caches are stored.
 *
 * @see Cache
 */
public class GlobalCacheRepository {

    private static final Map<Class<? extends Cache>, Cache> caches = new HashMap<>();

    private GlobalCacheRepository() {
    }

    /**
     * Register a cache in this repository.
     *
     * @param cache A cache instance we are attempting to register.
     * @param <T>   The object type the cache stores.
     * @throws IllegalArgumentException If a cache of the same class is already registered in the repository.
     */
    public static <T> void register(@NonNull final Cache<T> cache) {
        Validate.isTrue(!caches.containsKey(cache.getClass()), "%s is already a globally registered cache",
                        cache.getClass().getName()
        );
        caches.put(cache.getClass(), cache);
    }

    /**
     * Retrieve a stored cache within the repository, queried by class.
     *
     * @param clazz The class of the cache we are attempting to retrieve.
     * @param <T>   The object tye the cache stores.
     * @return The singleton instance of the cache we are attempting to retrieve.
     * @throws IllegalArgumentException If the cache class is not already registered in the repository.
     */
    public static <T extends Cache<?>> T getCache(@NonNull final Class<T> clazz) {
        Validate.isTrue(caches.containsKey(clazz), "%s is not a globally registered cache", clazz.getName());
        return clazz.cast(caches.get(clazz));
    }

}
