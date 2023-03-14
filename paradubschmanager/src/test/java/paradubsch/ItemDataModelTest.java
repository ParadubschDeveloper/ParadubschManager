package paradubsch;

import be.seeseemelk.mockbukkit.MockBukkit;
import de.paradubsch.paradubschmanager.ParadubschManager;
import de.paradubsch.paradubschmanager.models.ItemData;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ItemDataModelTest {
    @BeforeAll
    public static void setUp() {
        MockBukkit.mock().addPlayer();
        MockBukkit.load(ParadubschManager.class);
    }

    @AfterAll
    public static void tearDown() {
        MockBukkit.unmock();
    }

    @DisplayName("Test String getItemHash(ItemStack item)")
    @Test
    public void getItemHash() {
        ItemStack normalStone = new ItemStack(Material.STONE);
        ItemStack normalStone2 = new ItemStack(Material.STONE);
        normalStone2.setAmount(10);
        ItemStack normalStone3 = new ItemStack(Material.OAK_LOG);

        String normalStoneHash = ItemData.getItemHash(normalStone);
        String normalStone2Hash = ItemData.getItemHash(normalStone2);
        String normalStone3Hash = ItemData.getItemHash(normalStone3);
        assertEquals(normalStoneHash, normalStone2Hash);
        assertNotEquals(normalStoneHash, normalStone3Hash);
    }

    @DisplayName("Storing and saving ItemData")
    @Test
    public void saveAndStore() {
        ItemStack item = new ItemStack(Material.STONE);
        String hash = ItemData.getItemHash(item);
        ItemData itemData = new ItemData();
        itemData.setItemHash(hash);
        itemData.setItem(item);

        itemData.save();

        ItemData itemData2 = ItemData.getById(hash);
        assertNotNull(itemData2);
        assertEquals(itemData, itemData2);

        ItemStack item2 = itemData2.getItem();
        assertEquals(item, item2);
        assertEquals(hash, itemData2.getItemHash());
    }
}
