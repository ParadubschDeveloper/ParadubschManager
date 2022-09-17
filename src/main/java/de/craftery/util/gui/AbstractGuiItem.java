package de.craftery.util.gui;

import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;


public abstract class AbstractGuiItem extends GuiItem {
    @Override
    public void setItemHead(String headId) {
        super.setItemHead(headId);
        addPersistentIdentifier();
    }

    @Override
    public void setItemMaterial(Material material) {
        super.setItemMaterial(material);
        addPersistentIdentifier();
    }

    private void addPersistentIdentifier() {
        ItemMeta meta = getItemStack().getItemMeta();
        if (meta == null) return;
        meta.getPersistentDataContainer().set(GuiManager.itemIdentifier, new PersistentSerializableType(), this.getIdentifier());
        getItemStack().setItemMeta(meta);
    }
}
