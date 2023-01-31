package frc.robot.commands.rests.restUtils;

public class RESTAssertions {
    private static void fail() {
        throw new RESTAssertionException();
    }

    private static void fail(String message) {
        throw new RESTAssertionException(message);
    }

    private static void fail(Object expected, Object actual, String message) {
        fail(String.format("Expected: %s :: Actual: %s\n%s",
            expected.toString(), actual.toString(), message != null ? message : ""));
    }

    private static void fail(Object expected, Object actual) {
        fail(expected, actual, null);
    }

    public static void assertTrue(boolean condition) {
        if (!condition) {
            fail(true, false);
        }
    }

    public static void assertTrue(boolean condition, String message) {
        if (!condition) {
            fail(true, false, message);
        }
    }

    public static void assertTrue(boolean condition, String expected, String actual) {
        if (!condition) {
            fail(expected, actual);
        }
    }

    public static void assertFalse(boolean condition) {
        if (condition) {
            fail(false, true);
        }
    }

    public static void assertFalse(boolean condition, String message) {
        if (condition) {
            fail(false, true, message);
        }
    }

    public static void assertFalse(boolean condition, String expected, String actual) {
        if (!condition) {
            fail(expected, actual);
        }
    }

    public static void assertNull(Object actual, String message) {
        if (actual != null) {
            fail("null", actual, message);
        }
    }
    public static void assertNull(Object actual) {
        assertNull(actual, null);
    }

    public static void assertNotNull(Object actual, String message) {
        if (actual == null) {
            fail(actual, "null", message);
        }
    }

    public static void assertNotNull(Object actual) {
        assertNotNull(actual, null);
    }

    public static void assertEquals(int expected, int actual, int delta, String message) {
        if (!(expected >= actual - delta && expected <= actual + delta)) {
            fail(expected, actual, message);
        }
    }

    public static void assertEquals(int expected, Integer actual, int delta, String message) {
        if (!(expected >= actual.intValue() - delta && expected <= actual.intValue() + delta)) {
            fail(expected, actual, message);
        }
    }

    public static void assertEquals(Integer expected, int actual, int delta, String message) {
        if (!(expected.intValue() >= actual - delta && expected.intValue() <= actual + delta)) {
            fail(expected, actual, message);
        }
    }

    public static void assertEquals(Integer expected, Integer actual, int delta, String message) {
        if (!(expected.intValue() >= actual.intValue() - delta && expected.intValue() <= actual.intValue() + delta)) {
            fail(expected, actual, message);
        }
    }

    public static void assertEquals(int expected, int actual, int delta) {
        assertEquals(expected, actual, delta, null);
    }

    public static void assertEquals(int expected, Integer actual, int delta) {
        assertEquals(expected, actual, delta, null);
    }

    public static void assertEquals(Integer expected, int actual, int delta) {
        assertEquals(expected, actual, delta, null);
    }

    public static void assertEquals(Integer expected, Integer actual, int delta) {
        assertEquals(expected, actual, delta, null);
    }

    public static void assertEquals(int expected, int actual) {
        assertEquals(expected, actual, 0, null);
    }

    public static void assertEquals(int expected, Integer actual) {
        assertEquals(expected, actual, 0, null);
    }

    public static void assertEquals(Integer expected, int actual) {
        assertEquals(expected, actual, 0, null);
    }

    public static void assertEquals(Integer expected, Integer actual) {
        assertEquals(expected, actual, 0, null);
    }

    public static void assertEquals(double expected, double actual, double delta, String message) {
        if (!(expected >= actual - delta && expected <= actual + delta)) {
            fail(expected, actual, message);
        }
    }

    public static void assertEquals(double expected, Double actual, double delta, String message) {
        if (!(expected >= actual.doubleValue() - delta && expected <= actual.doubleValue() + delta)) {
            fail(expected, actual, message);
        }
    }

    public static void assertEquals(Double expected, double actual, double delta, String message) {
        if (!(expected.doubleValue() >= actual - delta && expected.doubleValue() <= actual + delta)) {
            fail(expected, actual, message);
        }
    }

    public static void assertEquals(Double expected, Double actual, double delta, String message) {
        if (!(expected.doubleValue() >= actual.doubleValue() - delta && expected.doubleValue() <= actual.doubleValue() + delta)) {
            fail(expected, actual, message);
        }
    }

    public static void assertEquals(double expected, double actual, double delta) {
        assertEquals(expected, actual, delta, null);
    }

    public static void assertEquals(double expected, Double actual, double delta) {
        assertEquals(expected, actual, delta, null);
    }

    public static void assertEquals(Double expected, double actual, double delta) {
        assertEquals(expected, actual, delta, null);
    }

    public static void assertEquals(Double expected, Double actual, double delta) {
        assertEquals(expected, actual, delta, null);
    }

    public static void assertEquals(double expected, double actual) {
        assertEquals(expected, actual, 0, null);
    }

    public static void assertEquals(double expected, Double actual) {
        assertEquals(expected, actual, 0, null);
    }

    public static void assertEquals(Double expected, double actual) {
        assertEquals(expected, actual, 0, null);
    }

    public static void assertEquals(Double expected, Double actual) {
        assertEquals(expected, actual, 0, null);
    }

    public static void assertEquals(byte expected, byte actual, String message) {
        if (expected != actual) {
            fail(expected, actual, message);
        }
    }

    public static void assertEquals(byte expected, Byte actual, String message) {
        if (expected != actual.byteValue()) {
            fail(expected, actual, message);
        }
    }

    public static void assertEquals(Byte expected, byte actual, String message) {
        if (expected.byteValue() != actual) {
            fail(expected, actual, message);
        }
    }

    public static void assertEquals(Byte expected, Byte actual, String message) {
        if (expected.byteValue() != actual.byteValue()) {
            fail(expected, actual, message);
        }
    }

    public static void assertEquals(byte expected, byte actual) {
        assertEquals(expected, actual, null);
    }

    public static void assertEquals(byte expected, Byte actual) {
        assertEquals(expected, actual, null);
    }

    public static void assertEquals(Byte expected, byte actual) {
        assertEquals(expected, actual, null);
    }

    public static void assertEquals(Byte expected, Byte actual) {
        assertEquals(expected, actual, null);
    }

    public static void assertEquals(char expected, char actual, String message) {
        if (expected != actual) {
            fail(expected, actual, message);
        }
    }

    public static void assertEquals(char expected, Character actual, String message) {
        if (expected != actual.charValue()) {
            fail(expected, actual, message);
        }
    }

    public static void assertEquals(Character expected, char actual, String message) {
        if (expected.charValue() != actual) {
            fail(expected, actual, message);
        }
    }

    public static void assertEquals(Character expected, Character actual, String message) {
        if (expected.charValue() != actual.charValue()) {
            fail(expected, actual, message);
        }
    }

    public static void assertEquals(char expected, char actual) {
        assertEquals(expected, actual, null);
    }

    public static void assertEquals(char expected, Character actual) {
        assertEquals(expected, actual, null);
    }

    public static void assertEquals(Character expected, char actual) {
        assertEquals(expected, actual, null);
    }

    public static void assertEquals(Character expected, Character actual) {
        assertEquals(expected, actual, null);
    }

    public static void assertEquals(Object expected, Object actual, String message) {
        if (!expected.equals(actual)) {
            fail(expected, actual, message);
        }
    }

    public static void assertEquals(Object expected, Object actual) {
        assertEquals(expected, actual, null);
    }

    public static void assertNotEquals(int expected, int actual, int delta, String message) {
        if (expected >= actual - delta && expected <= actual + delta) {
            fail(expected, actual, message);
        }
    }

    public static void assertNotEquals(int expected, Integer actual, int delta, String message) {
        if (expected >= actual.intValue() - delta && expected <= actual.intValue() + delta) {
            fail(expected, actual, message);
        }
    }

    public static void assertNotEquals(Integer expected, int actual, int delta, String message) {
        if (expected.intValue() >= actual - delta && expected.intValue() <= actual + delta) {
            fail(expected, actual, message);
        }
    }

    public static void assertNotEquals(Integer expected, Integer actual, int delta, String message) {
        if (expected.intValue() >= actual.intValue() - delta && expected.intValue() <= actual.intValue() + delta) {
            fail(expected, actual, message);
        }
    }

    public static void assertNotEquals(int expected, int actual, int delta) {
        assertNotEquals(expected, actual, delta, null);
    }

    public static void assertNotEquals(int expected, Integer actual, int delta) {
        assertNotEquals(expected, actual, delta, null);
    }

    public static void assertNotEquals(Integer expected, int actual, int delta) {
        assertNotEquals(expected, actual, delta, null);
    }

    public static void assertNotEquals(Integer expected, Integer actual, int delta) {
        assertNotEquals(expected, actual, delta, null);
    }

    public static void assertNotEquals(int expected, int actual) {
        assertNotEquals(expected, actual, 0, null);
    }

    public static void assertNotEquals(int expected, Integer actual) {
        assertNotEquals(expected, actual, 0, null);
    }

    public static void assertNotEquals(Integer expected, int actual) {
        assertNotEquals(expected, actual, 0, null);
    }

    public static void assertNotEquals(Integer expected, Integer actual) {
        assertNotEquals(expected, actual, 0, null);
    }

    public static void assertNotEquals(double expected, double actual, double delta, String message) {
        if (expected >= actual - delta && expected <= actual + delta) {
            fail(expected, actual, message);
        }
    }

    public static void assertNotEquals(double expected, Double actual, double delta, String message) {
        if (expected >= actual.doubleValue() - delta && expected <= actual.doubleValue() + delta) {
            fail(expected, actual, message);
        }
    }

    public static void assertNotEquals(Double expected, double actual, double delta, String message) {
        if (expected.doubleValue() >= actual - delta && expected.doubleValue() <= actual + delta) {
            fail(expected, actual, message);
        }
    }

    public static void assertNotEquals(Double expected, Double actual, double delta, String message) {
        if (expected.doubleValue() >= actual.doubleValue() - delta && expected.doubleValue() <= actual.doubleValue() + delta) {
            fail(expected, actual, message);
        }
    }

    public static void assertNotEquals(double expected, double actual, double delta) {
        assertNotEquals(expected, actual, delta, null);
    }

    public static void assertNotEquals(double expected, Double actual, double delta) {
        assertNotEquals(expected, actual, delta, null);
    }

    public static void assertNotEquals(Double expected, double actual, double delta) {
        assertNotEquals(expected, actual, delta, null);
    }

    public static void assertNotEquals(Double expected, Double actual, double delta) {
        assertNotEquals(expected, actual, delta, null);
    }

    public static void assertNotEquals(double expected, double actual) {
        assertNotEquals(expected, actual, 0, null);
    }

    public static void assertNotEquals(double expected, Double actual) {
        assertNotEquals(expected, actual, 0, null);
    }

    public static void assertNotEquals(Double expected, double actual) {
        assertNotEquals(expected, actual, 0, null);
    }

    public static void assertNotEquals(Double expected, Double actual) {
        assertNotEquals(expected, actual, 0, null);
    }

    public static void assertNotEquals(byte expected, byte actual, String message) {
        if (expected == actual) {
            fail(expected, actual, message);
        }
    }

    public static void assertNotEquals(byte expected, Byte actual, String message) {
        if (expected == actual.byteValue()) {
            fail(expected, actual, message);
        }
    }

    public static void assertNotEquals(Byte expected, byte actual, String message) {
        if (expected.byteValue() == actual) {
            fail(expected, actual, message);
        }
    }

    public static void assertNotEquals(Byte expected, Byte actual, String message) {
        if (expected.byteValue() == actual.byteValue()) {
            fail(expected, actual, message);
        }
    }

    public static void assertNotEquals(byte expected, byte actual) {
        assertNotEquals(expected, actual, null);
    }

    public static void assertNotEquals(byte expected, Byte actual) {
        assertNotEquals(expected, actual, null);
    }

    public static void assertNotEquals(Byte expected, byte actual) {
        assertNotEquals(expected, actual, null);
    }

    public static void assertNotEquals(Byte expected, Byte actual) {
        assertNotEquals(expected, actual, null);
    }

    public static void assertNotEquals(char expected, char actual, String message) {
        if (expected == actual) {
            fail(expected, actual, message);
        }
    }

    public static void assertNotEquals(char expected, Character actual, String message) {
        if (expected == actual.charValue()) {
            fail(expected, actual, message);
        }
    }

    public static void assertNotEquals(Character expected, char actual, String message) {
        if (expected.charValue() == actual) {
            fail(expected, actual, message);
        }
    }

    public static void assertNotEquals(Character expected, Character actual, String message) {
        if (expected.charValue() == actual.charValue()) {
            fail(expected, actual, message);
        }
    }

    public static void assertNotEquals(char expected, char actual) {
        assertNotEquals(expected, actual, null);
    }

    public static void assertNotEquals(char expected, Character actual) {
        assertNotEquals(expected, actual, null);
    }

    public static void assertNotEquals(Character expected, char actual) {
        assertNotEquals(expected, actual, null);
    }

    public static void assertNotEquals(Character expected, Character actual) {
        assertNotEquals(expected, actual, null);
    }

    public static void assertNotEquals(Object expected, Object actual, String message) {
        if (expected.equals(actual)) {
            fail(expected, actual, message);
        }
    }

    public static void assertNotEquals(Object expected, Object actual) {
        assertNotEquals(expected, actual, null);
    }

    public static void assertSame(Object expected, Object actual, String message) {
        if (expected != actual) {
            fail(expected.getClass().getSimpleName() + ": " + expected.hashCode(),
                actual.getClass().getSimpleName() + ": " + actual.hashCode(),
                message
            );
        }
    }

    public static void assertSame(Object expected, Object actual) {
        assertSame(expected, actual, null);
    }

    public static void assertNotSame(Object unexpected, Object actual, String message) {
        if (unexpected == actual) {
            fail(unexpected.getClass().getSimpleName() + ": " +  unexpected.hashCode(),
                actual.getClass().getSimpleName() + ": " +  actual.hashCode(),
                message
            );        }
    }

    public static void assertNotSame(Object unexpected, Object actual) {
        assertNotSame(unexpected, actual, null);
    }
}
