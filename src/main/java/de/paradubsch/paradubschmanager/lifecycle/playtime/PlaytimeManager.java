package de.paradubsch.paradubschmanager.lifecycle.playtime;

import de.paradubsch.paradubschmanager.ParadubschManager;
import de.paradubsch.paradubschmanager.config.ConfigurationManager;
import de.paradubsch.paradubschmanager.lifecycle.TabDecorationManager;
import de.paradubsch.paradubschmanager.models.PlayerData;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

public class PlaytimeManager implements Listener {
    private final Map<Player, Long> lastCached = new HashMap<>();

    public PlaytimeManager() {
        ParadubschManager.getInstance().getServer().getPluginManager().registerEvents(this, ParadubschManager.getInstance());
        Bukkit.getOnlinePlayers().forEach(player -> {
                lastCached.put(player, System.currentTimeMillis());
        });
        enableScheduler();
    }

    public long getPlaytime(Player player) {
        Long lastCache = lastCached.get(player);
        if (lastCache == null) throw new RuntimeException("Player is not Cached.- This must be an error!");
        PlayerData pd = PlayerData.getById(player.getUniqueId().toString());
        return pd.getPlaytime() + System.currentTimeMillis() - lastCache;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        lastCached.put(event.getPlayer(), System.currentTimeMillis());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        Long lastCache = lastCached.get(player);
        if (lastCache == null) {
            throw new RuntimeException("Player is not Cached.- This must be an error!");
        }
        PlayerData pd = PlayerData.getById(player.getUniqueId().toString());
        pd.setPlaytime(pd.getPlaytime() + System.currentTimeMillis() - lastCache);
        pd.saveOrUpdate();
        lastCached.remove(player);
    }

    private void enableScheduler () {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(ParadubschManager.getInstance(), () -> Bukkit.getOnlinePlayers().forEach(player -> {
            Long lastCache = lastCached.get(player);
            if (lastCache == null) {
                Bukkit.getLogger().log(Level.WARNING, "Player is not Cached.- This must be an error!");
                return;
            }
            PlayerData pd = PlayerData.getById(player.getUniqueId().toString());
            pd.setPlaytime(pd.getPlaytime() + System.currentTimeMillis() - lastCache);
            pd.saveOrUpdate();
            lastCached.put(player, System.currentTimeMillis());
            checkPassedGroups(player, pd.getPlaytime());
        }), 20*60*5, 20*60*5);
    }

    //This method is very temporary and will be improved in the future.
    private void checkPassedGroups(Player player, long time) {
        Bukkit.getScheduler().runTaskAsynchronously(ParadubschManager.getInstance(), () -> {
            LuckPerms api = ParadubschManager.getLuckPermsApi();
            if (api == null) return;
            UserManager userManager = api.getUserManager();
            CompletableFuture<User> userFuture = userManager.loadUser(player.getUniqueId());

            User lpUser = userFuture.join();
            List<String> beforeGroups = new ArrayList<>();
            lpUser.getNodes().stream().filter(NodeType.INHERITANCE::matches).forEach((node) -> {
                beforeGroups.add(node.getKey());
            });

            if (beforeGroups.size() > 1) {
                return;
            }

            String beforeGroupName = beforeGroups.get(0);

            // time > 2h || rank lapis
            if (time > 2*60*60*1000 && beforeGroupName.equals("group.default")) {
                applyGroup(player, "lapis");
                MessageAdapter.sendMessage(player, Message.Info.CMD_RANKED_UP_SUCCESSFUL, "Lapis");
            }

            //time > 60h || rank copper
            if (time > 60*60*60*1000 && beforeGroupName.equals("group.lapis")) {
                applyGroup(player, "copper");
                MessageAdapter.sendMessage(player, Message.Info.CMD_RANKED_UP_SUCCESSFUL, "Copper");
            }

            //time > 225h || rank lithium
            if (time > 225*60*60*1000 && beforeGroupName.equals("group.copper")) {
                applyGroup(player, "lithium");
                MessageAdapter.sendMessage(player, Message.Info.CMD_RANKED_UP_SUCCESSFUL, "Lithium");
            }

            //time > 505h || rank amethyst
            if (time > 505*60*60*1000 && beforeGroupName.equals("group.lithium")) {
                applyGroup(player, "amethyst");
                MessageAdapter.sendMessage(player, Message.Info.CMD_RANKED_UP_SUCCESSFUL, "Amethyst");
            }

            //time > 630h || rank gold
            if (time > 630*60*60*1000L && beforeGroupName.equals("group.amethyst")) {
                applyGroup(player, "gold");
                MessageAdapter.sendMessage(player, Message.Info.CMD_RANKED_UP_SUCCESSFUL, "Gold");
            }

            //time > 817h || rank platin
            if (time > 817*60*60*1000L && beforeGroupName.equals("group.gold")) {
                applyGroup(player, "platin");
                MessageAdapter.sendMessage(player, Message.Info.CMD_RANKED_UP_SUCCESSFUL, "Platin");
            }
        });
    }

    private void applyGroup(Player player, String group) {
        PlayerData target = PlayerData.getById(player.getUniqueId().toString());
        LuckPerms api = ParadubschManager.getLuckPermsApi();
        if (api == null) return;

        String prefix = ConfigurationManager.getConfig().getString("chatprefix." + group + ".prefix");
        String nameColor = ConfigurationManager.getConfig().getString("chatprefix." + group + ".namecolor", "&7");
        String chatColor = ConfigurationManager.getConfig().getString("chatprefix." + group + ".chatcolor", "&7");

        target.setChatPrefix(prefix);
        target.setNameColor(nameColor);
        target.setDefaultChatColor(chatColor);
        target.saveOrUpdate();

        UserManager userManager = api.getUserManager();
        CompletableFuture<User> userFuture = userManager.loadUser(player.getUniqueId());

        User lpUser = userFuture.join();
        InheritanceNode node = InheritanceNode.builder(group).value(true).build();
        lpUser.getNodes().stream().filter(NodeType.INHERITANCE::matches).forEach(lpUser.data()::remove);

        lpUser.data().add(node);
        api.getUserManager().saveUser(lpUser);
        TabDecorationManager.broadcastScoreboardTeams();
    }

}
