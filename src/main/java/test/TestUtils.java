package test;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Utility methods for test classes.
 */
public final class TestUtils {
    private TestUtils() {
    }

    /**
     * Returns a random element from the given list.
     *
     * @param list list of elements
     * @param <T>  element type
     * @return a random element from list
     * @throws IllegalStateException if list is null or empty
     */
    public static <T> T getRandom(List<T> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalStateException("Lista vazia");
        }
        int index = ThreadLocalRandom.current().nextInt(list.size());
        return list.get(index);
    }
}
