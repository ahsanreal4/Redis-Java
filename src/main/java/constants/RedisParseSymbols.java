package constants;

public class RedisParseSymbols {
    public static String SIMPLE_STRING = "+";

    public static String SIMPLE_ERROR = "-";
    public static String INTEGERS = ":";
    public static String ARRAYS = "*";
    public static String BULK_STRINGS = "$";

    public static String[] REDIS_PARSE_SYMBOLS_ARRAY = {SIMPLE_STRING, SIMPLE_ERROR, INTEGERS, ARRAYS, BULK_STRINGS};
}
