package store;

import java.util.HashMap;

class RedisHashMap {
    private final HashMap<String, String> hashMap;

    public RedisHashMap() {
        this.hashMap = new HashMap<>();
    }

    public String get(String key) {
        return hashMap.get(key);
    }

    public void set(String key, String value) {
        hashMap.put(key, value);
    }
}
