package paradubsch;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import de.paradubsch.paradubschmanager.ParadubschManager;
import de.paradubsch.paradubschmanager.models.PlayerData;
import de.paradubsch.paradubschmanager.util.Hibernate;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PlayerJoinTest {
    private static ServerMock server;
    private static PlayerMock player;

    @BeforeAll
    public static void setUp() {
        server = MockBukkit.mock();
        MockBukkit.load(ParadubschManager.class);
        server.getScheduler().performOneTick();
    }

    @AfterAll
    public static void tearDown() {
        server.getScheduler().performTicks(20*3);
        server.getPluginManager().disablePlugins();
        MockBukkit.unmock();
    }

    @Order(1)
    @DisplayName("Test if a player can join the server")
    @Test
    public void playerJoin() {
        player = server.addPlayer();
        server.getScheduler().performTicks(5);
        Assertions.assertEquals(1, server.getOnlinePlayers().size());
    }

    @Order(2)
    @DisplayName("Test if the PlayerData is initialized properly")
    @Test
    public void playerHasData() {
        PlayerData playerData = Hibernate.getPlayerData(player);
        Assertions.assertNotNull(playerData);

        Assertions.assertNotNull(playerData.getUuid());
        Assertions.assertNotNull(playerData.getName());
        Assertions.assertNotNull(playerData.getLanguage());
        Assertions.assertNotNull(playerData.getChatPrefix());
        Assertions.assertNotNull(playerData.getNameColor());
        Assertions.assertNotNull(playerData.getDefaultChatColor());
        Assertions.assertEquals(0L, playerData.getPlaytime());
        Assertions.assertEquals(150L, playerData.getMoney());
        Assertions.assertEquals(2, playerData.getMaxHomes());
    }
}
