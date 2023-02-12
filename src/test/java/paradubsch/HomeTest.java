package paradubsch;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import de.craftery.ErrorOr;
import de.paradubsch.paradubschmanager.ParadubschManager;
import de.paradubsch.paradubschmanager.commands.HomeCommand;
import de.paradubsch.paradubschmanager.commands.SethomeCommand;
import de.paradubsch.paradubschmanager.commands.DelhomeCommand;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class HomeTest {

    private static ServerMock server;
    @BeforeAll
    public static void setUp() {
        server = MockBukkit.mock();
        MockBukkit.load(ParadubschManager.class);
    }

    @AfterAll
    public static void tearDown() {
        MockBukkit.unmock();
    }

    @DisplayName("Test the sethome command")
    @Test
    public void sethomeCommand() {
        PlayerMock player = server.addPlayer();
        // normal home creation
        ErrorOr<Void> result = SethomeCommand.setHome(player, "test", false);
        assertFalse(result.isError());

        // home override without -> confirm
        result = SethomeCommand.setHome(player, "test", false);
        assertTrue(result.isError());
        assertEquals(result.getErrorMessage(), SethomeCommand.HOME_EXISTING_ERROR);

        // home override
        result = SethomeCommand.setHome(player, "test", true);
        assertFalse(result.isError());

        result = SethomeCommand.setHome(player, "test2", false);
        assertFalse(result.isError());

        // reaching max home cap
        result = SethomeCommand.setHome(player, "test3", false);
        assertTrue(result.isError());
        assertEquals(result.getErrorMessage(), SethomeCommand.HOME_LIMIT_REACHED_ERROR);
    }

    @DisplayName("Test the home command")
    @Test
    public void homeCommand() {
        PlayerMock player = server.addPlayer();

        SethomeCommand.setHome(player, "test", false);
        SethomeCommand.setHome(player, "test3", false);

        ErrorOr<Void> result = HomeCommand.teleportHome(player, "test");
        assertFalse(result.isError());

        result = HomeCommand.teleportHome(player, "testNotExisting");
        assertTrue(result.isError());
        assertEquals(result.getErrorMessage(), HomeCommand.HOME_NOT_FOUND_ERROR);

        result = HomeCommand.teleportHome(player, "TeSt3");
        assertTrue(result.isError());
        assertEquals(result.getErrorMessage(), HomeCommand.HOME_NOT_FOUND_BUT_ALTERNATIVE_ERROR);
    }

    @DisplayName("Test the delhome command")
    @Test
    public void delhomeCommand() {
        PlayerMock player = server.addPlayer();

        SethomeCommand.setHome(player, "test", false);
        SethomeCommand.setHome(player, "test3", false);

        ErrorOr<Void> result = DelhomeCommand.deleteHome(player, "test");
        assertFalse(result.isError());

        result = DelhomeCommand.deleteHome(player, "testNotExisting");
        assertTrue(result.isError());
        assertEquals(result.getErrorMessage(), DelhomeCommand.HOME_NOT_FOUND_ERROR);

        result = DelhomeCommand.deleteHome(player, "TeSt3");
        assertTrue(result.isError());
        assertEquals(result.getErrorMessage(), DelhomeCommand.HOME_NOT_FOUND_BUT_ALTERNATIVE_ERROR);

        // this should fail, because the home was deleted.
        result = DelhomeCommand.deleteHome(player, "test");
        assertTrue(result.isError());
        assertEquals(result.getErrorMessage(), DelhomeCommand.HOME_NOT_FOUND_ERROR);
    }
}
