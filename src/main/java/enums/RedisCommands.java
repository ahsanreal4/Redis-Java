package enums;

public enum RedisCommands {
    PING, ECHO, SET, GET;


    public static RedisCommands fromString(String command) {
        for (RedisCommands c : RedisCommands.values()) {
            if (c.name().equalsIgnoreCase(command)) {
                return c; // Return the matching enum constant
            }
        }
        return null; // Return null if no match is found
    }
}
