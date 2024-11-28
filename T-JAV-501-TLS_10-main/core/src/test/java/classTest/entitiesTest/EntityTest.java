package classTest.entitiesTest;

import com.badlogic.gdx.math.Vector2;
import fictitiousClass.TestEntity;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;

public class EntityTest {
    private TestEntity entity;

    @BeforeClass
    public static void setupClass() {
        System.out.println("\n=== Setup: Initializing tests for Entity===\n");
    }

    @Before
    public void setUp() {
        entity = new TestEntity(new Vector2(0, 0), 100, 10, 20);
        entity.setJumpVelocity(15);
        System.out.println("Initialized a new Entity instance with default values.");
    }

    @Test
    public void testJumpWhenNotJumping() {
        System.out.println("=== Test: testJumpWhenNotJumping ===");
        entity.jump();
        assertTrue("Entity isJumping variable should be true", entity.isJumping() );
        float vertical = entity.getVelocity().y;
        assertEquals("Y velocity not equal to jump velocity", 15,vertical, 0);
    }

    @Test
    public void testApplyGravityWhileJumping() {
        entity.setGravity(1);
        System.out.println("=== Test: testApplyGravityWhileJumping ===");
        entity.jump();
        float initialVelocity = entity.getVelocity().y;
        entity.applyGravity();
        System.out.println(entity.getVelocity().y);
        assertTrue("Gravity should decrease Y velocity", entity.getVelocity().y < initialVelocity);
    }

    @Test
    public void testCheckOnFloor() {
        System.out.println("=== Test: testCheckOnFloor ===");
        entity.setPosition(new Vector2(0, -5)); // Position sous le sol
        entity.checkOnFloor();
        float verticalPosition = entity.getVelocity().y;
        assertEquals("Y position should be reset to 0 when position y < 0", 0, verticalPosition, 0.0);
        assertFalse("Entity should not be in jump", entity.isJumping());
    }

    @Test
    public void testIsAlive() {
        System.out.println("=== Test: testIsAlive ===");
        assertTrue("normally alive", entity.isAlive());

        entity.setHealth(0);
        assertFalse( "normally dead",entity.isAlive());

        entity.setHealth(-10);
        assertFalse("normally dead", entity.isAlive());
    }

    @Test
    public void testReceiveDamage() {
        System.out.println("=== Test: testReceiveDamage ===");
        entity.setArmor(5);
        entity.receiveDamage(20);

        assertEquals("armor should decrease the damage", 85, entity.getHealth(),0);

        entity.receiveDamage(90);
        assertEquals("Health should not go under 0", 0, entity.getHealth(), 0);
    }

    @Test
    public void testMoveLaterally() {
        System.out.println("=== Test: testMoveLaterally ===");
        entity.moveLaterally(5);
        assertEquals("X velocity should increase correctly", 5, entity.getVelocity().x, 0);
        assertTrue("Entity should walk", entity.isWalking());
        assertTrue("entity shouldn't look left", entity.isLookingRight());

        entity.moveLaterally(-3);
        assertEquals ("X velocity should decrease correctly" , -3, entity.getVelocity().x, 0);
        assertFalse("Entity shouldn't look right", entity.isLookingRight());
    }

    @Test
    public void testIsHorizontallyAlignedLookingRight() {
        System.out.println("=== Test: testIsHorizontallyAlignedLookingRight ===");
        entity.setLookingRight(true);

        assertTrue("Should be horizontally aligned with attackRange", entity.isHorizontallyAligned(0, 50, 60, 80, 20));

        assertFalse("Should not be horizontally aligned", entity.isHorizontallyAligned(0, 50, 80, 100, 20));
    }

    @Test
    public void testIsHorizontallyAlignedLookingLeft() {
        System.out.println("=== Test: testIsHorizontallyAlignedLookingLeft ===");
        entity.setLookingRight(false);
        assertTrue( "Should be horizontally aligned with attackRange", entity.isHorizontallyAligned(50, 100, 20, 40, 20));
        assertFalse("Should not be horizontally aligned", entity.isHorizontallyAligned(50, 100, 0, 20, 20));
    }

    @Test
    public void testIsHorizontallyAlignedExactBoundary() {
        System.out.println("=== Test: testIsHorizontallyAlignedExactBoundary ===");
        entity.setLookingRight(true);
        assertTrue("Should be aligned exactly at the boundary" ,entity.isHorizontallyAligned(0, 50, 70, 90, 20));
        assertFalse("Should not be aligned due to offset",entity.isHorizontallyAligned(0, 50, 71, 90, 20));
    }

    @Test
    public void testVerticalAlignment() {
        System.out.println("=== Test: testVerticalAlignment ===");
        assertTrue( "Entities should be vertically aligned", entity.isVerticallyAligned(0, 50, 25, 75));
        assertFalse("Entities should not be vertically aligned", entity.isVerticallyAligned(0, 50, 60, 100));
    }

    @Test
    public void testIsNotCollidingWhenNoHorizontalOverlap() {
        System.out.println("=== Test: testIsNotCollidingWhenNoHorizontalOverlap ===");
        TestEntity other = new TestEntity(new Vector2(200, 0), 100, 10, 20);
        assertFalse("entity not horizontal superposed", entity.isCollidingWith(other, 10));
    }

    @Test
    public void testIsNotCollidingWhenNoVerticalOverlap() {
        System.out.println("=== Test: testIsNotCollidingWhenNoVerticalOverlap ===");
        TestEntity other = new TestEntity(new Vector2(0, 200), 100, 10, 20);
        assertFalse("entity not Vertical superposed",entity.isCollidingWith(other, 10));
    }

    @Test
    public void testIsNotCollidingBeyondAttackRange() {
        System.out.println("=== Test: testIsNotCollidingBeyondAttackRange ===");
        TestEntity other = new TestEntity(new Vector2(200, 0), 100, 10, 20);
        assertFalse("AttackRange is too low Entity not suppose to superpose",entity.isCollidingWith(other, 50));
    }

    @Test
    public void testIsCollidingWhenEntitiesOverlap() {
        System.out.println("=== Test: testIsCollidingWhenEntitiesOverlap ===");
        entity.setPosition(new Vector2(0, 0));
        TestEntity other = new TestEntity(new Vector2(50, 50), 100, 10, 20);
        assertFalse("entity Look Wrong", entity.isCollidingWith(other, 0));
        entity.setLookingRight(true);
        assertTrue("entity superpose and good face looking between entity", entity.isCollidingWith(other, 0));
    }

    @Test
    public void testIsCollidingWithAttackRange() {
        System.out.println("=== Test: testIsCollidingWithAttackRange ===");
        TestEntity other = new TestEntity(new Vector2(100, 0), 100, 10, 20);
        entity.setLookingRight(true);
        assertTrue("Entity superpose because of attackRange",entity.isCollidingWith(other, 100));
        assertFalse("Entity not superpose because of attackRange",entity.isCollidingWith(other, 0));
    }

    @After
    public void after() {
        System.out.println("=== After: Test completed. Cleaning up. ===\n");
    }

    @AfterClass
    public static void teardownClass() {
        System.out.println("\n=== AfterClass: All tests completed for Entity ===\n");
    }
}
