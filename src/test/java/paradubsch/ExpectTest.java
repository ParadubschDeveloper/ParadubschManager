package paradubsch;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.command.ConsoleCommandSenderMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import de.paradubsch.paradubschmanager.ParadubschManager;
import de.paradubsch.paradubschmanager.util.Expect;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class ExpectTest {
    private static ServerMock server;
    private static PlayerMock player;

    @BeforeAll
    public static void setUp() {
        server = MockBukkit.mock();
        MockBukkit.load(ParadubschManager.class);
        server.getScheduler().performOneTick();
        player = server.addPlayer();
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

    @DisplayName("Test Boolean playerString(@Nullable String player)")
    @Test
    public void playerString() {
        assertFalse(Expect.playerString(null));
        assertFalse(Expect.playerString(""));
        assertFalse(Expect.playerString("            "));
        assertFalse(Expect.playerString("e4"));
        assertFalse(Expect.playerString("Crafter Y"));
        assertFalse(Expect.playerString("Name%"));
        assertFalse(Expect.playerString("12345678901234567"));

        assertTrue(Expect.playerString(player.getName()));
        assertTrue(Expect.playerString("Crafter_Y"));
        assertTrue(Expect.playerString("1234567890123456"));
    }

    @DisplayName("Test Boolean argLen(Integer len, String[] args)")
    @Test
    public void argLen() {
        assertTrue(Expect.argLen(0, null));
        assertFalse(Expect.argLen(1, null));

        String[] testArgs2 = new String[0];
        assertTrue(Expect.argLen(0, testArgs2));
        assertFalse(Expect.argLen(5, testArgs2));

        String[] testArgs3 = {"test"};
        assertTrue(Expect.argLen(1, testArgs3));
        assertFalse(Expect.argLen(0, testArgs3));
        assertFalse(Expect.argLen(2, testArgs3));

        String[] testArgs4 = {"test", "test2", "test3"};
        assertTrue(Expect.argLen(3, testArgs4));
        assertFalse(Expect.argLen(1, testArgs4));
        assertFalse(Expect.argLen(52, testArgs4));
    }

    @DisplayName("Test Boolean minArgs(@NotNull Integer len, @Nullable String[] args)")
    @Test
    public void minArgs() {
        assertTrue(Expect.minArgs(0, null));
        assertFalse(Expect.minArgs(1, null));

        String[] testArgs2 = new String[0];
        assertTrue(Expect.minArgs(0, testArgs2));
        assertFalse(Expect.minArgs(5, testArgs2));

        String[] testArgs3 = {"test"};
        assertTrue(Expect.minArgs(1, testArgs3));
        assertTrue(Expect.minArgs(0, testArgs3));
        assertFalse(Expect.minArgs(2, testArgs3));

        String[] testArgs4 = {"test", "test2", "test3"};
        assertTrue(Expect.minArgs(3, testArgs4));
        assertTrue(Expect.minArgs(2, testArgs4));
        assertTrue(Expect.minArgs(1, testArgs4));
        assertFalse(Expect.minArgs(52, testArgs4));
    }

    @DisplayName("Test Boolean playerSender (CommandSender sender)")
    @Test
    public void playerSender() {
        ConsoleCommandSenderMock consoleMock = (ConsoleCommandSenderMock) server.getConsoleSender();
        assertTrue(Expect.playerSender(player));
        assertFalse(Expect.playerSender(consoleMock));
        //TODO: Test that a message got send
    }

    @DisplayName("Test Boolean colorCode(String code)")
    @Test
    public void colorCode() {
        assertTrue(Expect.colorCode("&0"));
        assertTrue(Expect.colorCode("&1"));
        assertTrue(Expect.colorCode("&2"));
        assertTrue(Expect.colorCode("&3"));
        assertTrue(Expect.colorCode("&4"));
        assertTrue(Expect.colorCode("&5"));
        assertTrue(Expect.colorCode("&6"));
        assertTrue(Expect.colorCode("&7"));
        assertTrue(Expect.colorCode("&8"));
        assertTrue(Expect.colorCode("&9"));
        assertTrue(Expect.colorCode("&a"));
        assertTrue(Expect.colorCode("&b"));
        assertTrue(Expect.colorCode("&c"));
        assertTrue(Expect.colorCode("&d"));
        assertTrue(Expect.colorCode("&e"));
        assertTrue(Expect.colorCode("&f"));
        assertTrue(Expect.colorCode("&k"));
        assertTrue(Expect.colorCode("&l"));
        assertTrue(Expect.colorCode("&m"));
        assertTrue(Expect.colorCode("&n"));
        assertTrue(Expect.colorCode("&o"));
        assertTrue(Expect.colorCode("&r"));

        assertFalse(Expect.colorCode("&x"));
        assertFalse(Expect.colorCode("a"));
        assertFalse(Expect.colorCode("aeeee"));
        assertFalse(Expect.colorCode("$a"));
    }
}
