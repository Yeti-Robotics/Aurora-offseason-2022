package frc.robot.commands.rests.restUtils;

public class RESTassertions {
    private static void fail() {
        throw new RESTAssertionException();
    }

    private static void fail(String message) {
        throw new RESTAssertionException(message);
    }

    private static void fail(Object expected, Object actual) {
        throw new RESTAssertionException(String.format("Expected: %s :: Actual: %s", expected.toString(), actual.toString()));
    }

    public static void assertEquals(boolean condition) {
        if (!condition) {
            fail(true, false);
        }
    }

    public static void assertNotEqual(boolean condition) {
        if (condition) {
            fail(false, true);
        }
    }
}
