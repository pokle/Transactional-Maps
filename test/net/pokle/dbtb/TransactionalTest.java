package net.pokle.dbtb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;
import net.pokle.dbtb.HashMapDatabase;
import net.pokle.dbtb.Transaction;

/**
 * User: tushar
 * Date: 24/06/2008
 * Time: 00:35:10
 */
public class TransactionalTest {

    @Test
    public void simple() {
        HashMapDatabase<String, String> base = new HashMapDatabase<String, String>();

        base.put("a", "A");
        base.put("base", "B");
        base.put("c", "C");

        Transaction<String, String> child = new Transaction<String, String>(base);
        assertEquals("B", child.get("base"));

        child.put("base", "Boy");
        assertEquals("Boy", child.get("base"));
        assertEquals("B", base.get("base"));

    }

    @Test
    public void rollback() {
        HashMapDatabase<String, String> base = new HashMapDatabase<String, String>();

        base.put("a", "apple");
        base.put("b", "bear");
        base.put("c", "cold");

        Transaction<String, String> child = new Transaction<String, String>(base);

        assertEquals("bear", child.get("b"));

        child.put("b", "beard");
        assertEquals("beard", child.get("b"));
        child.rollback();

        assertEquals("bear", child.get("b"));


    }

    @Test
    public void commitDeletions() {
        HashMapDatabase<String, String> base = new HashMapDatabase<String, String>();

        base.put("a", "apple");
        base.put("b", "bear");
        base.put("c", "cold");

        Transaction<String, String> child = new Transaction<String, String>(base);
        child.remove("a");
        child.remove("c");
        child.commit();

        assertEquals(1, base.size());
        assertEquals("bear", base.get("b"));

    }

    @Test
    public void commitModifications() {
        HashMapDatabase<String, String> base = new HashMapDatabase<String, String>();

        base.put("a", "apple");
        base.put("b", "bear");
        base.put("c", "cold");

        Transaction<String, String> child = new Transaction<String, String>(base);
        child.put("b", "bat");
        child.put("d", "dog");

        child.commit();

        assertEquals(4, base.size());

        assertEquals("apple", base.get("a"));
        assertEquals("bat", base.get("b"));
        assertEquals("cold", base.get("c"));
        assertEquals("dog", base.get("d"));

    }

    @Test
    public void removeKeyThatDoesntExist() {
        HashMapDatabase<String, String> base = new HashMapDatabase<String, String>();

        base.put("a", "apple");
        base.put("b", "bear");
        base.put("c", "cold");

        Transaction<String, String> child = new Transaction<String, String>(base);
        child.put("b", "bat");
        child.put("d", "dog");

        String result = child.remove("donkey");
        assertNull(result);

        // Assert that no changes have occurred.
        assertEquals("bat", child.get("b"));
        assertEquals("dog", child.get("d"));
        assertEquals("cold", child.get("c"));
                                        

    }

}
