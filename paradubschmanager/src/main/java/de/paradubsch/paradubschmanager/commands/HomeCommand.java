package de.paradubsch.paradubschmanager.commands;

import de.craftery.ErrorOr;
import de.craftery.command.CraftingCommand;
import de.craftery.command.CraftingPlayer;
import de.craftery.command.PlayerOnly;
import de.paradubsch.paradubschmanager.models.Home;
import de.paradubsch.paradubschmanager.util.lang.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HomeCommand extends CraftingCommand {
    public static final String HOME_NOT_FOUND_ERROR = "Home not found";
    public static final String HOME_NOT_FOUND_BUT_ALTERNATIVE_ERROR = "Home not found, but alternative is existing";

    public HomeCommand() {
        super("Home Command");
        this.setIdentifier("home");
    }

    @Override
    @PlayerOnly
    public boolean execute(CraftingPlayer player, String[] args) {
        String homeName;
        if (args.length == 0) {
            homeName = "default";
        } else {
            homeName = args[0];
        }

        teleportHome(player, homeName);
        return true;
    }

    public static ErrorOr<Void> teleportHome(CraftingPlayer player, String homeName) {
        List<Home> homes = Home.getByPlayer(player);

        Optional<Home> predicate = homes.stream().filter(home_ -> home_.getName().equals(homeName)).findFirst();
        Optional<Home> alternative = homes.stream().filter(home_ -> home_.getName().equalsIgnoreCase(homeName)).findFirst();
        if (predicate.isPresent()) {
            Home home = predicate.get();

            player.teleportNormalized(home.getWorld(), home.getX(), home.getY(), home.getZ());

            player.sendMessage(Message.Info.CMD_HOME_TELEPORT, homeName);
            return ErrorOr.release(null);
        } else if (alternative.isPresent()) {
            Home home = alternative.get();
            player.sendMessage(Message.Info.CMD_HOME_MAYBE_WRONG_NAME, homeName, home.getName());
            return new ErrorOr<>(HOME_NOT_FOUND_BUT_ALTERNATIVE_ERROR);
        } else {
            player.sendMessage(Message.Error.CMD_HOME_NOT_FOUND, homeName);
            return new ErrorOr<>(HOME_NOT_FOUND_ERROR);
        }
    }

    @Override
    public List<String> tabComplete(CraftingPlayer player, String[] args) {
        return new ArrayList<>();
    }
}
