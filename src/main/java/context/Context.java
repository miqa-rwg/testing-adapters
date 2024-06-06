package context;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.Thread.currentThread;


public class Context {

    private static final Map<String, Object> context = new ConcurrentHashMap<>();

    private Context() {
    }

    public static void put(String key, Object value) {
        context.put(unifyKeyByThread(key), value);
    }

    public static void remove(String key) {
        context.remove(unifyKeyByThread(key));
    }

    public static <T> T get(String key, Class<T> clazz) {
        try {
            return clazz.cast(context.get(unifyKeyByThread(key)));
        } catch (NullPointerException npe) {
            return null;
        }
    }

    public static void update(String key, Object value) {
        String wrappedKey = unifyKeyByThread(key);
        if (!context.containsKey(wrappedKey))
            context.put(wrappedKey, value);
        else {
            context.remove(wrappedKey);
            context.put(wrappedKey, value);
        }
    }

    private static String unifyKeyByThread(String key) {
        return currentThread().threadId() + ":" + key;
    }
}
