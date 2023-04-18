package de.paradubsch.paradubschmanager.commands;

import de.craftery.craftinglib.util.ConfigurationManager;
import de.craftery.craftinglib.messaging.lang.Language;
import de.paradubsch.paradubschmanager.ParadubschManager;
import de.paradubsch.paradubschmanager.lifecycle.TabDecorationManager;
import de.craftery.craftinglib.util.Expect;
import de.craftery.craftinglib.util.MessageAdapter;
import de.paradubsch.paradubschmanager.util.TimeCalculations;
import de.paradubsch.paradubschmanager.util.lang.Message;
import org.bukkit.Bukkit;
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
import java.util.UUID;

public class TimeVoteCommand implements CommandExecutor, TabCompleter {
    private boolean isVoteRunning = false;
    private final List<UUID> currentAttendees = new ArrayList<>();
    private int yesVotes = 0;
    private int noVotes = 0;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!Expect.playerSender(sender)) return true;
        Player player = (Player) sender;

        if (!Expect.minArgs(2, args)){
            MessageAdapter.sendMessage(sender,Message.Error.CMD_TIMEVOTE_WRONG_SYNTAX);
            return true;
        }

        switch (args[0]){
            case "create": {
                createVote(player, args[1]);
                break;
            }
            case "vote": {
                vote(player, args[1]);
                break;
            }
            default: {
                MessageAdapter.sendMessage(sender, Message.Error.CMD_TIMEVOTE_WRONG_SYNTAX);
            }
        }
        return true;
    }

    private void createVote(Player player, String inp) {
        if (!player.hasPermission("paradubsch.timevote.createvote")){
            MessageAdapter.sendMessage(player, Message.Error.NO_PERMISSION);
            return;
        }

        if (isVoteRunning){
            MessageAdapter.sendMessage(player, Message.Error.CMD_TIMEVOTE_ALREADY_RUNNING);
            return;
        }

        Long timeout = ParadubschManager.getInstance().getTimevoteTimeouts().get(player.getUniqueId());
        if (timeout != null && timeout > System.currentTimeMillis()) {
            MessageAdapter.sendMessage(player, Message.Error.CMD_TIMEVOTE_COOLDOWN,
                    TimeCalculations.timeMsToExpiration(timeout - System.currentTimeMillis(), MessageAdapter.getSenderLang(player)));
            return;
        }

        VoteSubject subject = determineVoteSubject(inp);

        if (subject == null){
            MessageAdapter.sendMessage(player, Message.Error.CMD_TIMEVOTE_WRONG_SYNTAX);
            return;
        }

        ParadubschManager.getInstance().getTimevoteTimeouts().put(player.getUniqueId(), System.currentTimeMillis() + ConfigurationManager.getLong("timevote.createCooldown"));

        startVote(subject, player.getWorld());
    }

    private void vote(Player player, String inp) {
        if (!isVoteRunning){
            MessageAdapter.sendMessage(player, Message.Error.CMD_TIMEVOTE_NOT_RUNNING);
            return;
        }

        VoteType voteType = determineVoteType(inp);
        if (voteType == null) {
            MessageAdapter.sendMessage(player,Message.Error.CMD_TIMEVOTE_WRONG_SYNTAX);
            return;
        }

        if (currentAttendees.contains(player.getUniqueId())){
            MessageAdapter.sendMessage(player, Message.Error.CMD_TIMEVOTE_ALREADY_VOTED);
            return;
        }

        addVote(voteType, player);
    }

    private void startVote(VoteSubject argument, World world) {
        world.getPlayers().forEach(worldPlayer -> {
            MessageAdapter.sendMessage(worldPlayer, Message.Info.VOTE_STARTED,
                    TabDecorationManager.getWorldName(world, MessageAdapter.getSenderLang(worldPlayer)),
                    getSubjectTranslation(argument, MessageAdapter.getSenderLang(worldPlayer))
            );
        });
        currentAttendees.clear();
        isVoteRunning = true;
        yesVotes = 0;
        noVotes = 0;

        Bukkit.getScheduler().runTaskLater(ParadubschManager.getInstance(), () -> {
            if(yesVotes >= noVotes){
                if(argument == VoteSubject.DAY) {
                    world.setTime(6000);
                } else {
                    world.setTime(20000);
                }

                world.getPlayers().forEach(worldPlayer -> {
                    MessageAdapter.sendMessage(worldPlayer, Message.Info.VOTE_TIME_CHANGE,
                            TabDecorationManager.getWorldName(world, MessageAdapter.getSenderLang(worldPlayer)),
                            getSubjectTranslation(argument, MessageAdapter.getSenderLang(worldPlayer))
                    );
                });

            } else {
                world.getPlayers().forEach(worldPlayer -> {
                    MessageAdapter.sendMessage(worldPlayer, Message.Info.VOTE_DECLINED);
                });
            }
            isVoteRunning = false;
        },30L * 20L);
    }

    private void addVote(VoteType vote, Player player){
        currentAttendees.add(player.getUniqueId());
        if (vote == VoteType.YES){
            MessageAdapter.sendMessage(player,Message.Info.CMD_VOTE_VOTED_YES);
            yesVotes++;
        } else {
            MessageAdapter.sendMessage(player,Message.Info.CMD_VOTE_VOTED_NO);
            noVotes++;
        }
    }

    private @Nullable VoteSubject determineVoteSubject(String subj) {
        switch (subj.toLowerCase()) {
            case "day":
            case "tag": return VoteSubject.DAY;
            case "night":
            case "nacht": return VoteSubject.NIGHT;
            default: return null;
        }
    }

    private @Nullable VoteType determineVoteType(String answer) {
        switch (answer.toLowerCase()) {
            case "ja":
            case "yes": return VoteType.YES;
            case "no":
            case "nein": return VoteType.NO;
            default: return null;
        }
    }

    private String getSubjectTranslation(VoteSubject subject, Language lang) {
        switch (subject) {
            case DAY: return ParadubschManager.getInstance().getLanguageManager().getString(Message.Constant.DAY_SINGULAR, lang);
            case NIGHT: return ParadubschManager.getInstance().getLanguageManager().getString(Message.Constant.NIGHT, lang);
            default: return "";
        }
    }

    public enum VoteType {
        YES,
        NO
    }

    public enum VoteSubject {
        DAY,
        NIGHT
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> returner = new ArrayList<>();

        if (args.length == 1) {
            returner.add("vote");
            if (sender.hasPermission("paradubsch.timevote.createvote")) {
                returner.add("create");
            }
            return returner;
        }

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("vote")) {
                returner.add("Ja");
                returner.add("Nein");
                return returner;
            }
            if (args[0].equalsIgnoreCase("create") && sender.hasPermission("paradubsch.timevote.createvote")) {
                returner.add("Tag");
                returner.add("Nacht");
                return returner;
            }
        }
        return returner;
    }
}
