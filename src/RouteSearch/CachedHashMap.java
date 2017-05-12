package RouteSearch;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class CachedHashMap<K, V> extends HashMap<K, V> {

  private static final long serialVersionUID = 36215319740415714L;

  private LRUCache<K, V> cache;
  private LRUCache<K, Boolean> containsCache;

  public CachedHashMap(int cacheSize) {
    containsCache = new LRUCache<>(2 * cacheSize);
    cache = new LRUCache<>(cacheSize);
  }

  @Override
  public V put(K key, V value) {

    cache.put(key, value);
    containsCache.put(key, true);
    return super.put(key, value);
  }

  @Override
  public boolean containsKey(Object key) {

    if (containsCache.containsKey(key)) {
      return containsCache.get(key);
    }

    boolean contains = super.containsKey(key);

    containsCache.put((K)key, contains);

    return contains;
  }

  @Override
  public V remove(Object key) {
    containsCache.remove(key);
    cache.remove(key);
    return super.remove(key);
  }

  @Override
  public V get(Object key) {
    if (containsCache.containsKey(key)) {
      if (!containsCache.get(key))
        return null;
    }

    V value = cache.get(key);
    if (value != null)
      return value;

    value = super.get(key);

    K keyCast = (K)key;
    if (value != null) {
      containsCache.put(keyCast, true);
      cache.put(keyCast, value);
    } else {
      containsCache.put(keyCast, false);
    }

    return value;
  }

  class LRUCache<A, B> extends LinkedHashMap<A, B> {
    private static final long serialVersionUID = -5958373482978477783L;

    private int cacheSize;

    public LRUCache(int cacheSize) {
      super(16, 0.75f, true);
      this.cacheSize = cacheSize;
    }

    protected boolean removeEldestEntry(Map.Entry<A, B> eldest) {
      return size() >= cacheSize;
    }
  }
}
