package de.paradubsch.paradubschmanager.commands;

import de.craftery.command.CraftCommand;
import de.craftery.command.CraftPlayer;
import de.craftery.command.FeatureDependent;
import de.craftery.command.PlayerOnly;
import de.craftery.util.features.HeadDatabaseFeature;
import de.craftery.util.gui.GuiManager;
import de.paradubsch.paradubschmanager.gui.window.BackpackGui;
import de.paradubsch.paradubschmanager.models.PlayerData;
import de.paradubsch.paradubschmanager.util.lang.Message;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BackpackCommand extends CraftCommand {
    public static final List<String> supervisedPlayers = new ArrayList<>();
    public BackpackCommand() {
        super("Backpack Command");
        this.setIdentifier("backpack");
    }

    @Override
    @PlayerOnly
    @FeatureDependent(features = {HeadDatabaseFeature.class})
    public boolean execute(CraftPlayer player, String[] args) {
        PlayerData target;
        if (args.length > 0 && player.hasPermission("paradubsch.backpack.others")) {
            target = PlayerData.getByName(args[0]);
            if (target == null) {
                player.sendMessage(Message.Error.CMD_PLAYER_NEVER_ONLINE);
                return true;
            }
        } else {
            target = PlayerData.getByPlayer(player);
        }

        if (supervisedPlayers.contains(target.getUuid())) {
            player.sendMessage(Message.Error.CMD_BACKPACK_CANT_GET_OPENED);
            return true;
        }

        if (!Objects.equals(player.getPlayer().getUniqueId().toString(), target.getUuid())) {
            supervisedPlayers.add(target.getUuid());
            Player onlinePlayer = Bukkit.getPlayer(target.getName());
            if (onlinePlayer != null) {
                onlinePlayer.closeInventory();
            }
        }

        GuiManager.entryGui(BackpackGui.class, player, target);
        return true;
    }

    @Override
    public List<String> tabComplete(CraftPlayer player, String[] args) {
        if (args.length == 1 && player.hasPermission("paradubsch.backpack.others")) {
            return null;
        }
        return new ArrayList<>();
    }
}
