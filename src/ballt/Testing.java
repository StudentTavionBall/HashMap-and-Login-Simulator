
package ballt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Testing {

    private Hashmap<String, Integer> map;

    @BeforeEach
    void setUp() {
        // Creates a fresh map before every single test
        map = new Hashmap<>();
    }

    @Test
    void testPutAndGet() throws Exception {
        assertNull(map.put("Apple", 10)); // Should return null on new insertion
        assertEquals(10, map.get("Apple"));
        assertEquals(1, map.size());
    }

    @Test
    void testUpdateExistingKey() throws Exception {
        map.put("Banana", 20);

        // Updating the key should return the OLD value
        assertEquals(20, map.put("Banana", 30));

        // Getting the key should return the NEW value
        assertEquals(30, map.get("Banana"));

        // This will fail until you apply the size fix for updates!
        assertEquals(1, map.size());
    }

    @Test
    void testCollisionHandling() throws Exception {
        // Fun Fact: "Aa" and "BB" have the exact same hashCode in Java!
        // This forces a guaranteed collision in your array.
        map.put("Aa", 100);
        map.put("BB", 200);

        assertEquals(100, map.get("Aa"));
        assertEquals(200, map.get("BB")); // Proves linear probing works!
        assertEquals(2, map.size());
    }

    @Test
    void testRemoveAndTombstones() throws Exception {
        map.put("Aa", 100);
        map.put("BB", 200); // Collides and is placed after "Aa"

        // Remove "Aa" (Leaving a Tombstone)
        assertEquals(100, map.remove("Aa"));

        // "Aa" should no longer be found
        assertNull(map.get("Aa"));

        // "BB" MUST still be found. If it isn't, the Tombstone broke the search chain!
        assertEquals(200, map.get("BB"));
        assertEquals(1, map.size());
    }

    @Test
    void testClear() throws Exception {
        map.put("Cat", 1);
        map.put("Dog", 2);

        map.clear();

        assertNull(map.get("Cat"));
        assertNull(map.get("Dog"));

        // This will fail until you apply the size reset fix in clear()!
        assertEquals(0, map.size());
    }

    @Test
    void testMapFullException() throws Exception {
        // Fill the map to its maximum capacity of 1000
        for (int i = 0; i < 1000; i++) {
            map.put("Key" + i, i);
        }

        assertEquals(1000, map.size());

        // The 1001st insertion should throw your Exception
        Exception exception = assertThrows(Exception.class, () -> {
            map.put("OverflowKey", 9999);
        });

        assertEquals("No more space in hashmap!", exception.getMessage());
    }
}