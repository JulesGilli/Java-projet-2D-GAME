import classTest.entitiesTest.EntityTest;
import classTest.coreTest.GameStatTest;
import classTest.itemsTest.ItemTest;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class TestRunner {

    public static void runTest(Class<?> testClass) {
        Result result = JUnitCore.runClasses(testClass);
        for (Failure failure : result.getFailures()) {
            System.out.println("Test failed: " + failure.toString());
        }

        if (result.wasSuccessful()) {
            System.out.println("All tests passed successfully: " + testClass);
        } else {
            System.out.println("Some tests failed. Total failures: " + result.getFailureCount());
        }
    }

    public void runTests() {
        runTest(EntityTest.class);
        runTest(GameStatTest.class);
        runTest(ItemTest.class);
    }
}
