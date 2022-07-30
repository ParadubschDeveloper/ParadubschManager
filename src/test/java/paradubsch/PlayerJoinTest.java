package paradubsch;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import de.paradubsch.paradubschmanager.ParadubschManager;
import de.paradubsch.paradubschmanager.models.PlayerData;
import de.paradubsch.paradubschmanager.util.Hibernate;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PlayerJoinTest {
    private static ServerMock server;
    private static PlayerMock player;

    @BeforeAll
    public static void setUp() {
        server = MockBukkit.mock();
        MockBukkit.load(ParadubschManager.class);
        server.getScheduler().performOneTick();
        server.getScheduler().waitAsyncTasksFinished();
        server.getScheduler().waitAsyncEventsFinished();
    }

    @AfterAll
    public static void tearDown() {
        server.getScheduler().waitAsyncTasksFinished();
        server.getScheduler().waitAsyncEventsFinished();
        System.out.println("Disabling plugin");
        server.getPluginManager().disablePlugins();
        System.out.println("Disabling server");
        MockBukkit.unmock();
        System.out.println("Disabled");
    }

    @Order(1)
    @DisplayName("Test if a player can join the server")
    @Test
    public void playerJoin() {
        player = server.addPlayer();
        server.getScheduler().waitAsyncTasksFinished();
        server.getScheduler().waitAsyncEventsFinished();
        assertEquals(1, server.getOnlinePlayers().size());
    }

    @Order(2)
    @DisplayName("Test if the PlayerData is initialized properly")
    @Test
    public void playerHasData() {
        PlayerData playerData = Hibernate.getPlayerData(player);
        assertNotNull(playerData);

        assertNotNull(playerData.getUuid());
        assertNotNull(playerData.getName());
        assertNotNull(playerData.getLanguage());
        assertNotNull(playerData.getChatPrefix());
        assertNotNull(playerData.getNameColor());
        assertNotNull(playerData.getDefaultChatColor());
        assertEquals(0L, playerData.getPlaytime());
        assertEquals(150L, playerData.getMoney());
        assertEquals(2, playerData.getMaxHomes());
    }
}
