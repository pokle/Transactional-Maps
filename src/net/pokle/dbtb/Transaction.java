package net.pokle.dbtb;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Represents an atomic unit of work that can be committed to a parent Database.
 *
 * @param <K>
 * @param <V>
 */
public class Transaction<K, V> implements Database<K, V> {

    private final Database<K, V> parent;

    /*
    * If a key exists in modifications, then it mustn't exist in deletions.
    * If a key exists in deletions, then it mustn't exist in modifications.
    */
    private final Map<K, V> modifications = new HashMap<K, V>();
    private final Set<K> deletions = new HashSet<K>();

    protected Transaction(Database<K, V> parent) {
        if (parent == null) {
            throw new IllegalArgumentException("A parent database is required");
        }
        this.parent = parent;
    }

    public boolean containsKey(K k) {
        return !deletions.contains(k) && (modifications.containsKey(k) || parent.containsKey(k));
    }

    public V put(K key, V val) {
        V old = get(key);
        modifications.put(key, val);
        deletions.remove(key);
        return old;
    }

    public V get(K key) {
        if (modifications.containsKey(key)) {
            return modifications.get(key);
        } else if (deletions.contains(key)) {
            return null; // Same behaviour as Map.get() for unknown keys
        } else {
            return parent.get(key);
        }
    }

    public V remove(K key) {
        if (modifications.containsKey(key)) {
            deletions.add(key);
            return modifications.remove(key);
        } else if (parent.containsKey(key)) {
            deletions.add(key);
            return parent.get(key);
        } else {
            // Key didn't exist, so don't remove (from parent!)
            return null;
        }
    }

    public void commit() {
        for (K k : deletions) {
            parent.remove(k);
        }
        deletions.clear();

        for (K k : modifications.keySet()) {
            parent.put(k, modifications.get(k));
        }
        modifications.clear();
    }

    public void rollback() {
        modifications.clear();
        deletions.clear();
    }
}
