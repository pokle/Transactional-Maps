package net.pokle.dbtb;

import java.util.HashMap;

/**
 * A Database that is backed by a java.util.HashMap. Thus it isn't persistent.
 *
 * @param <K>
 * @param <V>
 */
public class HashMapDatabase<K, V> extends HashMap<K, V> implements Database<K, V> {
}
