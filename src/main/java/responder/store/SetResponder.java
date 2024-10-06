package responder.store;

import constants.RedisArgumentSymbols;
import constants.RedisResponses;
import parser.Command;
import responder.Responder;
import store.RedisStore;

public class SetResponder extends Responder {
    @Override
    public String respondToCommand(Command command) {
        String payload = command.getPayload();

        String[] payloadSplit = payload.split(" ");

        if (payloadSplit.length < 2) {
            System.out.println("SET command needs a key and value to work");
            return null;
        }

        String key = payloadSplit[0];
        String value = payloadSplit[1];

        if (payloadSplit.length == 4) {
            String expirySymbol = payloadSplit[2].toUpperCase();

            if (!expirySymbol.equals(RedisArgumentSymbols.EXPIRY)) {
                System.out.println("Invalid Argument commend passed");
                return null;
            }

            String expiryTime = payloadSplit[3];

            int expiryInMilliSeconds = 0;

            try {
                expiryInMilliSeconds = Integer.parseInt(expiryTime);
            }
            catch (NumberFormatException exception) {
                System.out.println("Invalid integer passed as expiry argument");
                return null;
            }

            if (expiryInMilliSeconds < RedisArgumentSymbols.EXPIRY_MIN_TIME_IN_MILLISECONDS) {
                System.out.println("Expiry time cannot be less than " + RedisArgumentSymbols.EXPIRY_MIN_TIME_IN_MILLISECONDS);
                return null;
            }

            RedisStore.set(key, value);
            RedisStore.setExpiryTime(key, expiryInMilliSeconds);
        }
        else {
            RedisStore.set(key, value);
        }

        return RedisResponses.OK_RESPONSE;
    }
}
