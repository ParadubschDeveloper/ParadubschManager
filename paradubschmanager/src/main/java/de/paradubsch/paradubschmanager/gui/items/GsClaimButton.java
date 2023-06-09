package de.paradubsch.paradubschmanager.gui.items;

import de.craftery.craftinglib.util.gui.GuiItem;
import de.craftery.craftinglib.PlayerData;
import de.paradubsch.paradubschmanager.models.SaveRequest;
import de.craftery.craftinglib.util.MessageAdapter;
import de.paradubsch.paradubschmanager.util.lang.Message;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class GsClaimButton extends GuiItem {
    @Override
    public void onClick(Player p) {
        p.closeInventory();
        if (SaveRequest.getByPlayer(p) != null) {
            MessageAdapter.sendMessage(p, Message.Error.ALREADY_OPEN_SAVE_REQUEST);
            return;
        }

        PlayerData pd = PlayerData.getByPlayer(p);

        SaveRequest saveRequest = new SaveRequest();
        saveRequest.setX((long) p.getLocation().getX());
        saveRequest.setY(p.getLocation().getBlockY());
        saveRequest.setZ((long) p.getLocation().getZ());
        saveRequest.setWorld(p.getLocation().getWorld().getName());
        saveRequest.setPlayerRef(p.getUniqueId().toString());
        Integer saveId = (Integer) saveRequest.save();
        pd.setOpenSaveRequest(saveId);

        pd.saveOrUpdate();

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission("paradubsch.save")) {
                MessageAdapter.sendMessage(player, Message.Info.GS_CLAIM_REQUEST, p.getName(), saveId.toString() + "");
            }
        }
    }

    @Override
    public void build() {
        this.setItemMaterial(Material.LIME_STAINED_GLASS_PANE);
        this.setDisplayName(Message.Gui.GS_CLAIM_TITLE);
        this.addLore(Message.Gui.GS_CLAIM_LORE);
    }
}
