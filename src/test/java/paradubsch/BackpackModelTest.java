package paradubsch;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import de.paradubsch.paradubschmanager.ParadubschManager;
import de.paradubsch.paradubschmanager.models.Backpack;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BackpackModelTest {
    private static ServerMock server;
    private static PlayerMock player1;
    private static PlayerMock player2;

    @BeforeAll
    public static void setUp() {
        server = MockBukkit.mock();
        player1 = server.addPlayer();
        player2 = server.addPlayer();
        MockBukkit.load(ParadubschManager.class);
    }

    @AfterAll
    public static void tearDown() {
        MockBukkit.unmock();
    }

    @DisplayName("Test Backpack Model")
    @Test
    public void backpack() {
        Backpack backpack1 = Backpack.getByPlayer(player1);
        Backpack backpack2 = Backpack.getByPlayer(player2);
        assertEquals(1, backpack1.getId());
        assertEquals(2, backpack2.getId());

        assertEquals(27, backpack1.getMaxSlots());
        assertEquals(player1.getUniqueId().toString(), backpack1.getPlayerRef());
        assertEquals(0, backpack1.getSlots().size());
        assertEquals(0, backpack1.getItems().size());

        ItemStack item1 = new ItemStack(Material.STONE, 24);
        ItemStack item2 = new ItemStack(Material.DIRT, 64);
        ItemStack item3 = new ItemStack(Material.GRASS_BLOCK, 1);

        backpack1.getItems().add(item1);
        backpack1.getItems().add(item2);
        backpack1.getItems().add(item3);

        Backpack.storeByPlayer(player1, backpack1);

        Backpack backpack1Reloaded = Backpack.getByPlayer(player1);
        assertEquals(1, backpack1Reloaded.getId());
        assertEquals(27, backpack1Reloaded.getMaxSlots());
        assertEquals(player1.getUniqueId().toString(), backpack1Reloaded.getPlayerRef());
        assertEquals(3, backpack1Reloaded.getSlots().size());
        assertEquals(3, backpack1Reloaded.getItems().size());
        assertTrue(backpack1Reloaded.getItems().contains(item1));
        assertTrue(backpack1Reloaded.getItems().contains(item2));
        assertTrue(backpack1Reloaded.getItems().contains(item3));

        ItemStack item4 = new ItemStack(Material.COBBLESTONE, 64);
        backpack1Reloaded.getItems().add(item4);
        backpack1Reloaded.getItems().add(item1);
        backpack1Reloaded.getItems().add(item3);

        Backpack.storeByPlayer(player1, backpack1Reloaded);

        Backpack backpack1Reloaded2 = Backpack.getByPlayer(player1);

        assertEquals(6, backpack1Reloaded2.getSlots().size());
        assertEquals(6, backpack1Reloaded2.getItems().size());

        assertTrue(backpack1Reloaded.getItems().contains(item4));
        int item1Stacks = (int) backpack1Reloaded2.getItems().stream().filter(i -> i.isSimilar(item1)).count();
        assertEquals(2, item1Stacks);

        int item3Stacks = (int) backpack1Reloaded2.getItems().stream().filter(i -> i.isSimilar(item3)).count();
        assertEquals(2, item3Stacks);

        backpack1Reloaded2.getItems().remove(item1);
        backpack1Reloaded2.getItems().remove(item4);
        ItemStack item5 = item4.clone();
        item5.setAmount(32);
        backpack1Reloaded2.getItems().add(item5);

        Backpack.storeByPlayer(player1, backpack1Reloaded2);

        Backpack backpack1Reloaded3 = Backpack.getByPlayer(player1);

        assertEquals(5, backpack1Reloaded3.getSlots().size());
        assertEquals(5, backpack1Reloaded3.getItems().size());

        assertTrue(backpack1Reloaded3.getItems().contains(item1));
        assertTrue(backpack1Reloaded3.getItems().contains(item2));
        assertTrue(backpack1Reloaded3.getItems().contains(item3));
        assertTrue(backpack1Reloaded3.getItems().contains(item5));

    }
}
