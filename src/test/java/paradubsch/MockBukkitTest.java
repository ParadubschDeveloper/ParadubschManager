package paradubsch;

import be.seeseemelk.mockbukkit.MockBukkit;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Disabled("This is not working. Maybe in the future.")
public class MockBukkitTest {
    @DisplayName("Test if MockBukkit can load 3rd party plugins")
    @Test
    public void loadDependencies() {
        MockBukkit.mock();
        MockBukkit.loadJar("testDependencies/ProtocolLib.jar");
    }

}
