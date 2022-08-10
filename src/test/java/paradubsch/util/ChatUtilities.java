package paradubsch.util;

import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;

import static org.junit.jupiter.api.Assertions.fail;

public class ChatUtilities {
    public static String waitNextMessage(ServerMock server, PlayerMock player) {
        String nextMsg;

        int trys = 0;
        while(true) {
            trys++;
            server.getScheduler().performOneTick();
            nextMsg = player.nextMessage();
            if(nextMsg != null) {
                break;
            }
            if(trys > 1000) {
                fail("No message received");
                break;
            }
        }
        System.out.println("Message '"+ nextMsg +"' received after " + trys + " trys");
        return nextMsg;
    }
}
