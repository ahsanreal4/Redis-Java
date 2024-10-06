package constants;

public class RedisParseSymbols {
    /*
        ======================================
                SIMPLE STRINGS
        ======================================
     */
    public final static String SIMPLE_STRING = "+";
    public final static String SIMPLE_ERROR = "-";
    public final static String INTEGERS = ":";

    /*
        ======================================
                       Arrays
        ======================================
     */
    public final static String ARRAYS = "*";
    public final static String BULK_STRINGS = "$";
    /*
        ======================================
                        Strings
        ======================================
     */
    public final static String STRING_END_SYMBOL = "\r\n";
    /*
        ======================================
               Configuration Properties
        ======================================
     */
    public final static String ARGUMENT_PREPEND = "--";
    public final static String DIRECTORY_PATH_SYMBOL = "dir";
    public final static String Database_FILE_NAME_SMYBOL = "dbfilename";

    public final static String[] REDIS_PARSE_SYMBOLS_ARRAY = {
            SIMPLE_STRING, SIMPLE_ERROR, INTEGERS, ARRAYS, BULK_STRINGS
    };
}
