import enums.RedisCommands;

public class RedisCommand {
    private RedisCommands type;
    private String payload;

    public RedisCommand(RedisCommands type, String payload) {
        this.type = type;
        this.payload = payload;
    }

    public RedisCommands getType() {
        return type;
    }

    public void setType(RedisCommands type) {
        this.type = type;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
}
