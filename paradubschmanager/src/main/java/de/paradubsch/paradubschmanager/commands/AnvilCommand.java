package de.paradubsch.paradubschmanager.commands;

import de.craftery.command.CraftCommand;
import de.craftery.command.CraftPlayer;
import de.craftery.command.PlayerOnly;
import de.paradubsch.paradubschmanager.util.lang.Message;

import java.util.ArrayList;
import java.util.List;

public class AnvilCommand extends CraftCommand {
    public AnvilCommand() {
        super("Anvil Command");
        this.setIdentifier("anvil");
    }

    @Override
    @PlayerOnly
    public boolean execute(CraftPlayer player, String[] args) {
        player.openAnvil(player.getLocation(), true);
        player.sendMessage(Message.Info.CMD_ANVIL_OPENED);
        return true;
    }

    @Override
    public List<String> tabComplete(CraftPlayer player, String[] args) {
        return new ArrayList<>();
    }
}
