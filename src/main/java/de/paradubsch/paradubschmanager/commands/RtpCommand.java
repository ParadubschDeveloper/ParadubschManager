package de.paradubsch.paradubschmanager.commands;

import de.craftery.util.gui.GuiManager;
import de.paradubsch.paradubschmanager.ParadubschManager;
import de.paradubsch.paradubschmanager.gui.window.RtpGui;
import de.paradubsch.paradubschmanager.lifecycle.jobs.JobManager;
import de.paradubsch.paradubschmanager.util.Expect;
import de.paradubsch.paradubschmanager.util.MessageAdapter;
import de.paradubsch.paradubschmanager.util.TimeCalculations;
import de.paradubsch.paradubschmanager.util.lang.Message;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RtpCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!Expect.playerSender(sender)) return true;
        Player player = (Player) sender;

        Long timeout = ParadubschManager.getInstance().getRtpTimeouts().get(player.getUniqueId());

        if (timeout != null && timeout > System.currentTimeMillis()) {
            MessageAdapter.sendMessage(player, Message.Info.COMMAND_TIMEOUT, TimeCalculations.timeMsToExpiration(timeout - System.currentTimeMillis(), MessageAdapter.getSenderLang(player)));
            return true;
        }
        GuiManager.entryGui(RtpGui.class, player);
        return true;
    }

    public static void rtp(Player player, World world, final int i) {
        if (i == 15) {
            MessageAdapter.sendMessage(player, Message.Error.CMD_RTP_NO_DESTINATION_FOUND);
            return;
        }
        Bukkit.getScheduler().runTaskLater(ParadubschManager.getInstance(), () -> {
            Random ran = new Random();
            int posNeg = posNeg();
            double x = ((ran.nextInt(2000000 - 5000) + 5000) * posNeg) + 0.5D;
            posNeg = posNeg();
            double z = ((ran.nextInt(2000000 - 5000) + 5000) * posNeg) + 0.5D;
            world.loadChunk((new Location(world, x, 1.0D, z)).getChunk());
            int y;
            if (world.getEnvironment() == World.Environment.NETHER) {
                y = netherY(world, (int) x, (int) z);
            } else {
                y = world.getHighestBlockYAt((int) x, (int) z);
            }

            Location loc = new Location(world, x, y, z);
            if (!loc.getBlock().getType().isOccluding() ||
                    !loc.clone().add(0.0D, 1.0D, 0.0D).getBlock().getType().isAir() ||
                    !loc.clone().add(0.0D, 2.0D, 0.0D).getBlock().getType().isAir()) {
                JobManager.sendActionBar(player, "Searching... " + i + "/15");
                rtp(player, world, i + 1);
            } else {
                loc.add(0.0D, 1.2D, 0.0D);
                ParadubschManager.getInstance().getRtpTimeouts().put(player.getUniqueId(), System.currentTimeMillis() + 600000L);
                player.teleport(loc);
            }
        }, 10);
    }

    private static int netherY(World world, int x, int z) {
        Location base = new Location(world, x, 0.0D, z);
        boolean foundAir = false;
        for (int i = 126; i >= 0; i--) {
            base.setY(i);
            if (base.getBlock().getType().equals(Material.AIR)) {
                foundAir = true;
            } else if (foundAir) {
                return i;
            }
        }
        return 0;
    }

    private static int posNeg() {
        Random ran = new Random();
        int a = ran.nextInt(2);
        if (a == 1)
            return 1;
        return -1;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return new ArrayList<>();
    }
}
