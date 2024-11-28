package classTest.itemsTest;

import io.github.maingame.core.GameStat;
import io.github.maingame.entities.Entity;
import io.github.maingame.items.Item;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;


public class ItemTest {
    private Item item;

    @BeforeClass
    public static void setupClass() {
        System.out.println("\n=== Setup: Initializing tests for Item ===\n");
    }

    @Before
    public void setUp() {
        // Initialize a TestItem with sample values
        item = new Item(100, "java/sprite_platform.png", "java/sprite_platform.png", "java/sprite_platform.png") {
            @Override
            public void applyItem(Entity targetEntity) {

            }

            @Override
            public void resetItem(Entity targetEntity) {

            }

            @Override
            public boolean isUnlocked(GameStat stat) {
                return false;
            }
        };
        System.out.println("Initialized a new TestItem instance with default values.");
    }

    @Test
    public void testGetStrGold() {
        System.out.println("=== Test: testGetStrGold ===");
        String strGold = item.getStrGold();
        assertEquals("Gold should be returned as a string", "100", strGold);
    }

    @Test
    public void testGetGold() {
        System.out.println("=== Test: testGetGold ===");
        int gold = item.getGold();
        assertEquals("Gold value should be 100", 100, gold);
    }

    @Test
    public void testGetIncreaseValue() {
        System.out.println("=== Test: testGetIncreaseValue ===");
        float increaseValue = item.getIncreaseValue();
        // Assuming default value for increaseValue is 0, adjust accordingly
        assertEquals("Increase value should be 0 initially", 0.0f, increaseValue, 0.0f);
    }

    @Test
    public void testSetIncreaseValue() {
        System.out.println("=== Test: testSetIncreaseValue ===");
        item.setIncreaseValue(2.5f);
        assertEquals("Increase value should be updated to 2.5", 2.5f, item.getIncreaseValue(), 0.0f);
    }

    @After
    public void after() {
        System.out.println("=== After: Test completed. Cleaning up. ===\n");
    }

    @AfterClass
    public static void teardownClass() {
        System.out.println("\n=== AfterClass: All tests completed for Item ===\n");
    }
}
