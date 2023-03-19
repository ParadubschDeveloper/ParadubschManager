package de.paradubsch.paradubschmanager.commands;

import de.craftery.command.CraftingCommand;
import de.craftery.command.CraftingPlayer;
import de.craftery.command.PlayerOnly;
import de.paradubsch.paradubschmanager.util.lang.Message;

import java.util.ArrayList;
import java.util.List;

public class AnvilCommand extends CraftingCommand {
    public AnvilCommand() {
        super("Anvil Command");
        this.setIdentifier("anvil");
    }

    @Override
    @PlayerOnly
    public boolean execute(CraftingPlayer player, String[] args) {
        player.openAnvil(player.getLocation(), true);
        player.sendMessage(Message.Info.CMD_ANVIL_OPENED);
        return true;
    }

    @Override
    public List<String> tabComplete(CraftingPlayer player, String[] args) {
        return new ArrayList<>();
    }
}
