package de.paradubsch.paradubschmanager.commands;

import de.craftery.craftinglib.CraftingLib;
import de.craftery.craftinglib.InternalMessages;
import de.craftery.craftinglib.command.CraftingCommand;
import de.craftery.craftinglib.command.CraftingPlayer;
import de.craftery.craftinglib.util.Expect;
import de.craftery.craftinglib.util.collectables.Collectable;
import de.paradubsch.paradubschmanager.util.lang.Message;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class CollectablesCommand extends CraftingCommand {
    public static final Map<UUID, String> playersInAddMode = new HashMap<>();

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

        if (args[0].equalsIgnoreCase("remove")) {
            removeCollectable(player, args);
            return true;
        }

        if (args[0].equalsIgnoreCase("tp")) {
            teleportToCollectable(player, args);
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
        }

        return true;
    }

    private Collectable validateCollectableFromArgs(CraftingPlayer player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(Message.Error.ID_NOT_PROVIDED);
            return null;
        }
        String id = args[1];
        Long idInt = Expect.parseLong(id);
        if (idInt == null) {
            player.sendMessage(Message.Error.ID_NOT_PROVIDED);
            return null;
        }
        return Collectable.getById(idInt);
    }

    private void teleportToCollectable(CraftingPlayer player, String[] args) {
        if (!player.isPlayer()) {
            player.sendMessage(InternalMessages.CMD_ONLY_FOR_PLAYERS);
            return;
        }
        Collectable collectable = validateCollectableFromArgs(player, args);
        if (collectable == null) {
            player.sendMessage(Message.Error.ID_NOT_PROVIDED);
            return;
        }
        player.teleportNormalized(collectable.getLocation());
        player.sendMessage(Message.Info.TELEPORTED_TO_COLLECTABLE, collectable.getId() + "");
    }

    private void removeCollectable(CraftingPlayer player, String[] args) {
        Collectable collectable = validateCollectableFromArgs(player, args);
        if (collectable == null) {
            player.sendMessage(Message.Error.ID_NOT_PROVIDED);
            return;
        }
        Collectable.removeCollectable(collectable);
        player.sendMessage(Message.Info.COLLECTABLE_REMOVED, collectable.getId() + "");
    }

    private void enableAddMode(CraftingPlayer player, String type) {
        if (!player.isPlayer()) {
            player.sendMessage(InternalMessages.CMD_ONLY_FOR_PLAYERS);
            return;
        }
        if (playersInAddMode.get(player.getUniqueId()) != null &&
                playersInAddMode.get(player.getUniqueId()).equalsIgnoreCase(type)) {
            playersInAddMode.remove(player.getUniqueId());
            player.sendMessage(Message.Info.ADD_MODE_DISABLED);
            return;
        }
        if (!CraftingLib.getRegisteredCollectableTypes().contains(type)) {
            player.sendMessage(Message.Error.COLLECTABLE_TYPE_NOT_FOUND, type);
            return;
        }
        playersInAddMode.put(player.getUniqueId(), type);
        player.sendMessage(Message.Info.ADD_MODE_ENABLED, type);
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
            l.add("remove");
            l.add("tp");
            return l;
        }
        if (args.length == 2 && !args[0].equalsIgnoreCase("remove") && !args[0].equalsIgnoreCase("tp")) {
            l.add("list");
            l.add("add");
        }
        return l;
    }
}
