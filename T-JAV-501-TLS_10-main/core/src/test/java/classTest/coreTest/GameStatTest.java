
package classTest.coreTest;
import io.github.maingame.core.GameStat;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class GameStatTest {

    private GameStat gameStat;

    @BeforeClass
    public static void setupClass() {
        System.out.println("\n=== Setup: Initializing tests for GameStat ===\n");
    }

    @Before
    public void setup() {
        gameStat = new GameStat();
        System.out.println("Initialized a new GameStat instance with default values.");
    }

    @Test
    public void testAddGolds() {
        System.out.println("=== Test: Add Golds ===");
        System.out.println("Initial Golds: " + gameStat.getGolds());
        gameStat.addGolds(100);
        System.out.println("Added 100 Golds. Current Golds: " + gameStat.getGolds());
        assertEquals("Golds not added correctly!", 100, gameStat.getGolds());
        System.out.println("Test Passed: Golds were added successfully.\n");
    }

    @Test
    public void testRemoveGolds() {
        System.out.println("=== Test: Remove Golds ===");
        System.out.println("Initial Golds: " + gameStat.getGolds());
        gameStat.addGolds(100);
        System.out.println("Added 100 Golds. Current Golds: " + gameStat.getGolds());

        gameStat.removeGolds(50);
        System.out.println("Removed 50 Golds. Current Golds: " + gameStat.getGolds());
        assertEquals("Golds not removed correctly!", 50, gameStat.getGolds());

        gameStat.removeGolds(60);
        System.out.println("Attempted to remove 60 Golds. Current Golds: " + gameStat.getGolds());
        assertEquals("Golds went negative!", 0, gameStat.getGolds());

        System.out.println("Test Passed: Golds were removed successfully without going negative.\n");
    }

    @Test
    public void testInitialValues() {
        System.out.println("=== Test: Initial Values ===");
        System.out.println("Initial Golds: " + gameStat.getGolds());
        System.out.println("Initial Kills: " + gameStat.getKills());
        System.out.println("Initial Floors: " + gameStat.getFloors());
        System.out.println("First Game Flag: " + gameStat.isFirstGame());

        assertEquals("Initial golds should be 0!", 0, gameStat.getGolds());
        assertEquals("Initial kills should be 0!", 0, gameStat.getKills());
        assertEquals("Initial floors should be 0!", 0, gameStat.getFloors());
        assertTrue("First game flag should be true!", gameStat.isFirstGame());

        System.out.println("Test Passed: All initial values are correct.\n");
    }

    @After
    public void teardown() {
        System.out.println("=== Teardown: Test completed. Cleaning up. ===\n");
    }

    @AfterClass
    public static void teardownClass() {
        System.out.println("\n=== Teardown: All tests completed for GameStat ===\n");
    }
}
