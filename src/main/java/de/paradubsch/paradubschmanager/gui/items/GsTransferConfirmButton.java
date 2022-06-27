package de.paradubsch.paradubschmanager.gui.items;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.craftery.util.gui.GuiItem;
import de.paradubsch.paradubschmanager.ParadubschManager;
import de.paradubsch.paradubschmanager.models.PlayerData;
import de.paradubsch.paradubschmanager.util.MessageAdapter;
import de.paradubsch.paradubschmanager.util.lang.Message;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class GsTransferConfirmButton extends GuiItem {
    @Override
    public void onClick(Player p) {
        PlayerData pd = (PlayerData) this.args.get(1);
        ProtectedRegion region = (ProtectedRegion) this.args.get(2);

        OfflinePlayer ofPl = Bukkit.getOfflinePlayer(UUID.fromString(pd.getUuid()));
        region.getOwners().clear();
        region.getOwners().addPlayer(ParadubschManager.getInstance().getWorldGuardPlugin().wrapOfflinePlayer(ofPl));

        p.closeInventory();
        MessageAdapter.sendMessage(p, Message.Info.GS_TRANSFER_SUCCESSFUL, pd.getName());
    }

    @Override
    public void build() {
        this.setItemMaterial(Material.LIME_STAINED_GLASS_PANE);
        this.setDisplayName(Message.Gui.GS_TRANSFER_BUTTON_TITLE);

        String name = (String) this.args.get(0);
        this.addLore(Message.Gui.GS_TRANSFER_BUTTON_LORE, name);
    }
}
