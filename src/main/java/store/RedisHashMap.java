package store;

import java.util.Date;
import java.util.HashMap;

class RedisHashMap {
    private final HashMap<String, String> hashMap;
    private final HashMap<String, Long> expiryMap;

    public RedisHashMap() {
        this.hashMap = new HashMap<>();
        this.expiryMap = new HashMap<>();
    }

    private void removeKeyIfExpired(String key) {
        long nowTime = new Date().getTime();
        Long expiryTime = expiryMap.get(key);

        if(expiryTime == null) return;

        if (nowTime >= expiryTime) {
            expiryMap.remove(key);
            hashMap.remove(key);
        }
    }

    public String get(String key) {
        removeKeyIfExpired(key);

        return hashMap.get(key);
    }

    public void set(String key, String value) {
        hashMap.put(key, value);
    }

    public void setExpiryTime(String key, int expiryTime) {
        if (expiryTime <= 0) return;

        long calculatedExpiryTime = new Date().getTime() + expiryTime;
        expiryMap.put(key, calculatedExpiryTime);
    }
}
