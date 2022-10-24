package de.paradubsch.paradubschmanager.commands;

import de.paradubsch.paradubschmanager.ParadubschManager;
import de.paradubsch.paradubschmanager.config.ConfigurationManager;
import de.paradubsch.paradubschmanager.lifecycle.TabDecorationManager;
import de.paradubsch.paradubschmanager.models.PlayerData;
import de.paradubsch.paradubschmanager.util.Expect;
import de.paradubsch.paradubschmanager.util.MessageAdapter;
import de.paradubsch.paradubschmanager.util.lang.Message;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.InheritanceNode;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class RankCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Bukkit.getScheduler().runTaskAsynchronously(ParadubschManager.getInstance(), () -> {
            if (!Expect.minArgs(1, args)) {
                MessageAdapter.sendMessage(sender, Message.Error.CMD_PLAYER_NOT_PROVIDED);
                return;
            }

            PlayerData target = PlayerData.getByName(args[0]);

            if (target == null) {
                MessageAdapter.sendMessage(sender, Message.Error.CMD_PLAYER_NEVER_ONLINE, args[0]);
                return;
            }

            if (!Expect.minArgs(2, args)) {
                MessageAdapter.sendMessage(sender, Message.Error.CMD_RANK_NOT_PROVIDED);
                return;
            }

            String rankForPlayer = args[1];

            LuckPerms api = ParadubschManager.getLuckPermsApi();
            if (api == null) return;
            Set<Group> groups = api.getGroupManager().getLoadedGroups();

            if (groups.stream().noneMatch(group -> group.getName().equals(rankForPlayer))) {
                MessageAdapter.sendMessage(sender, Message.Error.CMD_RANK_NOT_FOUND, rankForPlayer);
                return;
            }

            String prefix = ConfigurationManager.getConfig().getString("chatprefix." + rankForPlayer + ".prefix");
            String nameColor = ConfigurationManager.getConfig().getString("chatprefix." + rankForPlayer + ".namecolor", "&7");
            String chatColor = ConfigurationManager.getConfig().getString("chatprefix." + rankForPlayer + ".chatcolor", "&7");

            target.setChatPrefix(prefix);
            target.setNameColor(nameColor);
            target.setDefaultChatColor(chatColor);
            target.saveOrUpdate();

            UserManager userManager = api.getUserManager();
            CompletableFuture<User> userFuture = userManager.loadUser(UUID.fromString(target.getUuid()));

            User lpUser = userFuture.join();
            InheritanceNode node = InheritanceNode.builder(rankForPlayer).value(true).build();
            lpUser.getNodes().stream().filter(NodeType.INHERITANCE::matches).forEach(lpUser.data()::remove);

            lpUser.data().add(node);
            api.getUserManager().saveUser(lpUser);
            TabDecorationManager.broadcastScoreboardTeams();
            MessageAdapter.sendMessage(sender, Message.Info.CMD_RANKED_SUCCESSFUL, rankForPlayer, target.getName());
        });
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) return null;
        if (args.length == 2) {
            LuckPerms api = ParadubschManager.getLuckPermsApi();
            if (api == null) return new ArrayList<>();
            Set<Group> groups = api.getGroupManager().getLoadedGroups();
            List<String> l = new ArrayList<>();
            for (Group g : groups) {
                l.add(g.getName());
            }
            return l;
        }

        return new ArrayList<>();
    }
}
