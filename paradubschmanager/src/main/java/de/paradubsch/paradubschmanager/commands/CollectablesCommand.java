package de.paradubsch.paradubschmanager.commands;

import de.craftery.CraftingLib;
import de.craftery.command.CraftingCommand;
import de.craftery.command.CraftingPlayer;
import de.craftery.util.collectables.Collectable;
import de.paradubsch.paradubschmanager.util.lang.Message;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class CollectablesCommand extends CraftingCommand {
    public CollectablesCommand() {
        super("Collectables Command");
        this.setIdentifier("collectables");
    }

    @Override
    public boolean execute(CraftingPlayer player, String[] args) {
        if (args.length == 0) {
            showRegisteredCollectables(player);
            return true;
        }

        if (args.length == 1 || (args.length == 2 && args[1].equalsIgnoreCase("list"))) {
            showCollectablesForType(player, args[0]);
            return true;
        }

        switch (args[1]) {
            case "list": {
                showCollectablesForType(player, args[0]);
                break;
            }
            case "add": {
                enableAddMode(player, args[0]);
                break;
            }
            case "remove": {
                removeCollectable(player, args[0], args);
                break;
            }
            case "tp": {
                teleportToCollectable(player, args[0], args);
                break;
            }
        }

        return true;
    }

    private void teleportToCollectable(CraftingPlayer player, String type, String[] args) {
        // TODO: Implement
    }

    private void removeCollectable(CraftingPlayer player, String type, String[] args) {
        // TODO: Implement
    }

    private void enableAddMode(CraftingPlayer player, String type) {
        // TODO: Implement
    }

    private void showRegisteredCollectables(CraftingPlayer player) {
        String registered = StringUtils.join(CraftingLib.getRegisteredCollectableTypes(), "&7, &a");

        player.sendMessage(Message.Info.REGISTERED_COLLECTABLES, registered);
    }

    private void showCollectablesForType(CraftingPlayer player, String type) {
        if (!CraftingLib.getRegisteredCollectableTypes().contains(type)) {
            player.sendMessage(Message.Error.COLLECTABLE_TYPE_NOT_FOUND, type);
            return;
        }

        player.sendMessage(Message.Info.COLLECTABLE_LIST_HEADER, type);
        for (Collectable collectable : Collectable.getByType(type)) {
            player.sendMessage(Message.Info.COLLECTABLE_LIST_ENTITY, collectable.getId() + "");
        }
    }

    @Override
    public List<String> tabComplete(CraftingPlayer player, String[] args) {
        List<String> l = new ArrayList<>();
        if (args.length == 1) {
            l.addAll(CraftingLib.getRegisteredCollectableTypes());
            return l;
        }
        if (args.length == 2) {
            l.add("list");
            l.add("add");
            l.add("remove");
            l.add("tp");
        }
        return l;
    }
}
