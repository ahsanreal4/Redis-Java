import constants.RedisParseSymbols;
import store.RedisStore;

public class Main {

    private static void setConfigurationProperties(String[] args) {
        // Invalid number of arguments
        if (args.length % 2 != 0) return;

        for (int i = 0; i < args.length; i += 2) {
            if (i == args.length - 1) break;

            String argument = args[i];

            String directoryPathArgument = RedisParseSymbols.ARGUMENT_PREPEND + RedisParseSymbols.DIRECTORY_PATH_SYMBOL;
            String databasePathArgument = RedisParseSymbols.ARGUMENT_PREPEND + RedisParseSymbols.Database_FILE_NAME_SMYBOL;

            if (argument.equals(directoryPathArgument)) {
                RedisStore.set(RedisParseSymbols.DIRECTORY_PATH_SYMBOL, args[i+1]);
            }
            else if (argument.equals(databasePathArgument)) {
                RedisStore.set(RedisParseSymbols.Database_FILE_NAME_SMYBOL, args[i+1]);
            }
        }
    }

    public static void main(String[] args){
      setConfigurationProperties(args);

      int PORT = 6379;

      Server server = new Server(PORT);
      server.start();
  }
}
