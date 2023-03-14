package de.paradubsch.paradubschmanager.lifecycle;

import de.paradubsch.paradubschmanager.ParadubschManager;
import de.paradubsch.paradubschmanager.models.logging.CommandLog;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandListener implements Listener {
    public CommandListener() {
        ParadubschManager.getInstance().getServer().getPluginManager().registerEvents(this, ParadubschManager.getInstance());
    }

    @EventHandler
    public void PlayerCommand(PlayerCommandPreprocessEvent event) {
        // TODO: Command spam protection
        String command = event.getMessage();
        if (command.startsWith("/")) {
            command = command.substring(1);
        }
        if (command.isEmpty()) return;
        CommandLog.logCommandExecution(event.getPlayer(), command);
    }
}
