package de.craftery.util;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class TestMocks {
    public static byte[] serializeItemStack(ItemStack itemStack) {
        try {
            return itemStack.serializeAsBytes();
        } catch (Exception e) {
            Bukkit.getLogger().warning("Bukkit serialization failed. This should only happen in tests;");
            return itemStack.getType().name().getBytes();
        }
    }

    public static ItemStack deserializeItemStack(byte[] bytes) {
        try {
            return ItemStack.deserializeBytes(bytes);
        } catch (Exception e) {
            Bukkit.getLogger().warning("Bukkit deserialization failed. This should only happen in tests;");
            return new ItemStack(Material.valueOf(new String(bytes)));
        }
    }
}
