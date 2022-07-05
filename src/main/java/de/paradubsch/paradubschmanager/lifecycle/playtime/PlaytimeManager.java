package de.paradubsch.paradubschmanager.lifecycle.playtime;

import de.paradubsch.paradubschmanager.ParadubschManager;
import de.paradubsch.paradubschmanager.config.ConfigurationManager;
import de.paradubsch.paradubschmanager.lifecycle.TabDecorationManager;
import de.paradubsch.paradubschmanager.models.PlayerData;
import de.paradubsch.paradubschmanager.util.Hibernate;
import de.paradubsch.paradubschmanager.util.MessageAdapter;
import de.paradubsch.paradubschmanager.util.lang.Message;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.InheritanceNode;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class PlaytimeManager implements Listener {
    private final Map<Player, PlaytimeInstance> cachedData = new HashMap<>();

    public PlaytimeManager() {
        ParadubschManager.getInstance().getServer().getPluginManager().registerEvents(this, ParadubschManager.getInstance());
        Bukkit.getOnlinePlayers().forEach(player -> CompletableFuture.supplyAsync(() -> Hibernate.getPlayerData(player))
                .thenAccept(pd -> {
                    PlaytimeInstance pi = new PlaytimeInstance();
                    pi.setPlaytime(pd.getPlaytime());
                    pi.setLastRecordTime(System.currentTimeMillis());
                    cachedData.put(player, pi);
                }));
        enableScheduler();
    }

    public long getPlaytime(Player player) {
        PlaytimeInstance pi = cachedData.get(player);
        if (pi == null) throw new RuntimeException("Player is not Cached.- This must be an error!");

        long newPlaytime = pi.getPlaytime() + System.currentTimeMillis() - pi.getLastRecordTime();
        pi.setPlaytime(newPlaytime);
        pi.setLastRecordTime(System.currentTimeMillis());
        cachedData.put(player, pi);
        return newPlaytime;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        CompletableFuture.supplyAsync(() -> Hibernate.getPlayerData(player))
                .thenAccept(pd -> {
                   PlaytimeInstance pi = new PlaytimeInstance();
                   pi.setPlaytime(pd.getPlaytime());
                   pi.setLastRecordTime(System.currentTimeMillis());
                   cachedData.put(player, pi);
                });
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        PlaytimeInstance pi = cachedData.get(player);
        if (pi == null) {
            return;
        }

        long newPlaytime = pi.getPlaytime() + System.currentTimeMillis() - pi.getLastRecordTime();

        pi.setPlaytime(newPlaytime);
        pi.setLastRecordTime(System.currentTimeMillis());
        cachedData.remove(player);

        CompletableFuture.supplyAsync(() -> Hibernate.getPlayerData(player))
                .thenApply(pd -> {
                    pd.setPlaytime(newPlaytime);
                    return pd;
                }).thenAcceptAsync(Hibernate::save);
    }

    private void enableScheduler () {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(ParadubschManager.getInstance(), () -> Bukkit.getOnlinePlayers().forEach(player -> {
            PlaytimeInstance pi = cachedData.get(player);
            if (pi == null) {
                return;
            }

            long newPlaytime = pi.getPlaytime() + System.currentTimeMillis() - pi.getLastRecordTime();

            pi.setPlaytime(newPlaytime);
            pi.setLastRecordTime(System.currentTimeMillis());
            cachedData.put(player, pi);

            checkPassedGroups(player, newPlaytime);

            CompletableFuture.supplyAsync(() -> Hibernate.getPlayerData(player))
                    .thenApply(pd -> {
                        pd.setPlaytime(newPlaytime);
                        return pd;
                    }).thenAcceptAsync(Hibernate::save);

        }), 20*60*5, 20*60*5);

    }

    //This method is very temporary and will be improved in the future.
    private void checkPassedGroups(Player player, long time) {

        // time > 2h || rank lapis
        if (time > 2*60*60*1000 && !player.hasPermission("group.lapis")) {
            applyGroup(player, "lapis");
            MessageAdapter.sendMessage(player, Message.Info.CMD_RANKED_UP_SUCCESSFUL, "Lapis");
        }

        //time > 60h || rank copper
        if (time > 60*60*60*1000 && !player.hasPermission("group.copper")) {
            applyGroup(player, "copper");
            MessageAdapter.sendMessage(player, Message.Info.CMD_RANKED_UP_SUCCESSFUL, "Copper");
        }

        //time > 225h || rank lithium
        if (time > 225*60*60*1000 && !player.hasPermission("group.lithium")) {
            applyGroup(player, "lithium");
            MessageAdapter.sendMessage(player, Message.Info.CMD_RANKED_UP_SUCCESSFUL, "Lithium");
        }

        //time > 505h || rank amethyst
        if (time > 505*60*60*1000 && !player.hasPermission("group.amethyst")) {
            applyGroup(player, "amethyst");
            MessageAdapter.sendMessage(player, Message.Info.CMD_RANKED_UP_SUCCESSFUL, "Amethyst");
        }

        //time > 630h || rank gold
        if (time > 630*60*60*1000L && !player.hasPermission("group.gold")) {
            applyGroup(player, "gold");
            MessageAdapter.sendMessage(player, Message.Info.CMD_RANKED_UP_SUCCESSFUL, "Gold");
        }

        //time > 817h || rank platin
        if (time > 817*60*60*1000L && !player.hasPermission("group.platin")) {
            applyGroup(player, "platin");
            MessageAdapter.sendMessage(player, Message.Info.CMD_RANKED_UP_SUCCESSFUL, "Platin");
        }
    }

    private void applyGroup(Player player, String group) {
        Bukkit.getScheduler().runTaskAsynchronously(ParadubschManager.getInstance(), () -> {
            PlayerData target = Hibernate.getPlayerData(player);
            LuckPerms api = ParadubschManager.getLuckPermsApi();
            if (api == null) return;

            String prefix = ConfigurationManager.getConfig().getString("chatprefix." + group + ".prefix");
            String nameColor = ConfigurationManager.getConfig().getString("chatprefix." + group + ".namecolor", "&7");
            String chatColor = ConfigurationManager.getConfig().getString("chatprefix." + group + ".chatcolor", "&7");

            target.setChatPrefix(prefix);
            target.setNameColor(nameColor);
            target.setDefaultChatColor(chatColor);
            Hibernate.save(target);

            UserManager userManager = api.getUserManager();
            CompletableFuture<User> userFuture = userManager.loadUser(player.getUniqueId());

            User lpUser = userFuture.join();
            InheritanceNode node = InheritanceNode.builder(group).value(true).build();
            lpUser.getNodes().stream().filter(NodeType.INHERITANCE::matches).forEach(lpUser.data()::remove);

            lpUser.data().add(node);
            api.getUserManager().saveUser(lpUser);
            TabDecorationManager.broadcastScoreboardTeams();
        });
    }

}
