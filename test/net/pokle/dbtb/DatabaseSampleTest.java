package net.pokle.dbtb;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import net.pokle.dbtb.HashMapDatabase;
import net.pokle.dbtb.Transaction;

/**
 */
public class DatabaseSampleTest {
    @Test
    public void demo() {
        // A base database
        HashMapDatabase<String, String> base = new HashMapDatabase<String, String>();
        // And its un-commited transaction
        Transaction<String, String> child = new Transaction<String, String>(base);

        // A modification to the base...
        base.put("The meaning of life", "0");

        // ... is visible to both the base and child transactions
        assertEquals("0", base.get("The meaning of life"));
        assertEquals("0", child.get("The meaning of life"));

        // A modification of the child transaction...
        child.put("The meaning of life", "42");

        // ... is only visible in the child transaction
        assertEquals("42", child.get("The meaning of life"));
        assertEquals("0", base.get("The meaning of life"));

        // You can commit a child transaction to its parent
        child.commit();
        assertEquals("42", child.get("The meaning of life"));
        assertEquals("42", base.get("The meaning of life"));
    }
}
