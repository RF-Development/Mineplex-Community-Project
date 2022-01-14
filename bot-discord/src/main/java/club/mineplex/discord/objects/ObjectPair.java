package club.mineplex.discord.objects;

public class ObjectPair<K, V> {

    K key;
    V value;

    public ObjectPair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }
}
