package de.paradubsch.paradubschmanager;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import com.google.common.collect.Lists;
import de.craftery.craftinglib.CraftingLib;
import de.craftery.craftinglib.util.gui.GuiManager;
import de.paradubsch.paradubschmanager.gui.window.BackpackGui;
import de.paradubsch.paradubschmanager.models.Backpack;
import de.paradubsch.paradubschmanager.util.lang.Message;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Disabled("This is not working. Maybe in the future.")
public class BackpackGuiTest {
    private static ServerMock server;
    private static PlayerMock player;

    @BeforeAll
    public static void setUp() {
        server = MockBukkit.mock();
        MockBukkit.load(CraftingLib.class);
        MockBukkit.load(ParadubschManager.class);
        player = server.addPlayer();
        fillPlayerBackpack();
    }

    @AfterAll
    public static void tearDown() {
        server.getScheduler().cancelTasks(ParadubschManager.getInstance());
        MockBukkit.unmock();
    }

    @Test
    public void openInventoryAndViewContent() {
        GuiManager.entryGui(BackpackGui.class, player);

        player.assertInventoryView(Message.Gui.BACKPACK_TITLE.getDefault(), InventoryType.CHEST);

        InventoryView inventory = player.getOpenInventory();
        int itemCount = 0;
        for (ItemStack itemStack : inventory.getTopInventory().getContents())
            if (itemStack != null) itemCount++;
        assertEquals(itemCount, 27);

    }

    private static void fillPlayerBackpack() {
        // Page: 1
        ItemStack item1 = new ItemStack(Material.STONE, 10);
        ItemStack item2 = new ItemStack(Material.IRON_SWORD);
        ItemStack item3 = new ItemStack(Material.OAK_LOG, 56);
        ItemStack item4 = new ItemStack(Material.CHEST, 41);
        // Fill the rest with items of the first backpack page
        ItemStack item5 = new ItemStack(Material.SPRUCE_PLANKS, 64);

        // Page: 2
        ItemStack item6 = new ItemStack(Material.CARROT, 64);
        ItemStack item7 = new ItemStack(Material.CHEST, 1);
        ItemStack item8 = new ItemStack(Material.DIAMOND_CHESTPLATE);

        List<ItemStack> items = Lists.newArrayList();
        items.add(item1);
        items.add(item2);
        items.add(item3);
        items.add(item4);
        for (int i = 0; i < 23; i++) {
            items.add(item5);
        }
        items.add(item6);
        items.add(item7);
        items.add(item8);

        Backpack backpack = Backpack.getByPlayer(player);
        backpack.setItems(items);
        Backpack.storeByPlayer(player, backpack);
    }

}
