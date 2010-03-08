package net.pokle.dbtb;

/**
 * Defines the basic operations that you can perform on a DBTB database. Essentially a cut down version of java.util.Map
 * <p/>
 * User: tushar
 * Date: 23/06/2008
 * Time: 23:37:02
 */
public interface Database<K, V> {

    boolean containsKey(K k);

    V put(K key, V val);

    V get(K key);

    V remove(K key);
}
