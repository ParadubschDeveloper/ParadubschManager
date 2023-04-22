package de.paradubsch.paradubschmanager.commands;

import de.craftery.craftinglib.util.Expect;
import de.craftery.craftinglib.util.MessageAdapter;
import de.paradubsch.paradubschmanager.util.lang.Message;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class NightvisonCommand implements CommandExecutor, TabCompleter {
    private static final PotionEffect nightvisionEffect = new PotionEffect(PotionEffectType.NIGHT_VISION, 999999999, 1, true, false);

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!Expect.playerSender(sender)) return true;
        Player p = (Player) sender;

        switch (args.length) {
            case 0: {
                if (p.hasPotionEffect(PotionEffectType.NIGHT_VISION)){
                    p.removePotionEffect(PotionEffectType.NIGHT_VISION);
                    MessageAdapter.sendMessage(sender, Message.Info.CMD_NIGHTVISION_DEACTIVATED);
                } else {
                    p.addPotionEffect(NightvisonCommand.nightvisionEffect);
                    MessageAdapter.sendMessage(sender, Message.Info.CMD_NIGHTVISION_ACTIVATED);
                }
                break;
            }
            case 1: {
                switch (args[0]){
                    case "an":
                    case "on": {
                        enableNightvisionForPlayer(p);
                        break;
                    }
                    case "aus":
                    case "off": {
                        disablenightVisonForPlayer(p);
                        break;
                    }
                    default:
                        MessageAdapter.sendMessage(sender, Message.Error.CMD_NIGHTVISION_WRONG_SYNTAX);
                        break;
                }
                break;
            }
            default: {
                MessageAdapter.sendMessage(sender, Message.Error.CMD_NIGHTVISION_WRONG_SYNTAX);
            }
        }
        return true;
    }
    private void enableNightvisionForPlayer(Player player) {
        if (player.hasPotionEffect(NightvisonCommand.nightvisionEffect.getType())) {
            MessageAdapter.sendMessage(player, Message.Error.CMD_NIGHTVISION_ALREADY_ON);
        } else {
            player.addPotionEffect(NightvisonCommand.nightvisionEffect);
            MessageAdapter.sendMessage(player, Message.Info.CMD_NIGHTVISION_ACTIVATED);
        }
    }

    private void disablenightVisonForPlayer(Player player){
        if (!player.hasPotionEffect(NightvisonCommand.nightvisionEffect.getType())){
            MessageAdapter.sendMessage(player, Message.Error.CMD_NIGHTVISION_ALREADY_OFF);
        } else {
            player.removePotionEffect(NightvisonCommand.nightvisionEffect.getType());
            MessageAdapter.sendMessage(player, Message.Info.CMD_NIGHTVISION_DEACTIVATED);
        }

    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> l = new ArrayList<>();
        if (args.length == 1) {
            l.add("on");
            l.add("off");
            return l;
        }

        return l;
    }
}


