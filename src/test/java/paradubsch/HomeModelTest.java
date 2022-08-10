package paradubsch;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import de.paradubsch.paradubschmanager.ParadubschManager;
import de.paradubsch.paradubschmanager.models.Home;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.Serializable;

@Disabled("This was for testing purposes, maybe it will be used in the future")
public class HomeModelTest {

    @BeforeAll
    public static void setUp() {
        ServerMock server = MockBukkit.mock();
        MockBukkit.load(ParadubschManager.class);
        server.getScheduler().performOneTick();
    }

    @AfterAll
    public static void tearDown() {
        MockBukkit.unmock();
        System.out.println("Disabled");
    }

    @Test
    public void testHomeModel() {
        Home home = new Home();
        home.setName("test");
        Serializable id = home.save();
        System.out.println(Home.getById(id));
    }
}
