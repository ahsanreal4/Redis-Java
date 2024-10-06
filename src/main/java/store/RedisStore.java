package store;

public class RedisStore {
    private final static RedisHashMap redisHashMap;

    static {
        redisHashMap = new RedisHashMap();
    }

    public static void set(String key, String value) {
        if (key == null || key.isEmpty()) {
            System.out.println("Empty key");
            return;
        }

        if (value == null || value.isEmpty()) {
            System.out.println("Empty value");
            return;
        }

        redisHashMap.set(key, value);
    }

    public static String get(String key) {
        if (key == null || key.isEmpty()) {
            System.out.println("Empty key");
            return null;
        }

        return redisHashMap.get(key);
    }

    public static void setExpiryTime(String key, int expiryTime) {
        redisHashMap.setExpiryTime(key, expiryTime);
    }

    public static void printAllKeyValuePairs() {
        redisHashMap.printAllKeyValuePairs();
    }
}
