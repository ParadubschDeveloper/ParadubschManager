package de.paradubsch.paradubschmanager.lifecycle.vote;

import de.paradubsch.paradubschmanager.util.Expect;
import de.paradubsch.paradubschmanager.util.MessageAdapter;
import de.paradubsch.paradubschmanager.util.lang.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TimeVote implements CommandExecutor, TabCompleter {
    static boolean isVoteRunning = false;
    private static final int COOLDOWN = 15;
    private static long cooldownTimestamp = 0;
    Vote vote = new Vote();
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!Expect.minArgs(2, args)){
            return true;
        }
        if(!Expect.playerSender(sender)){
            return true;
        }
        Player p = (Player) sender;

        switch (args[0]){
            case "create": {
                if(isVoteRunning){
                    MessageAdapter.sendMessage(sender, Message.Error.CMD_VOTE_ALREADY_RUNNING);
                    return true;
                }
                if(!sender.hasPermission("paradubsch.createvote")){
                    MessageAdapter.sendMessage(sender, Message.Error.CMD_VOTE_CREATE_NO_PERMISSION);
                    return true;
                }
                long timeLeft = System.currentTimeMillis() - cooldownTimestamp;
                if(TimeUnit.MILLISECONDS.toMinutes(timeLeft) < COOLDOWN ){
                    MessageAdapter.sendMessage(sender, Message.Error.CMD_VOTE_COOLDOWN, String.valueOf(COOLDOWN - TimeUnit.MILLISECONDS.toMinutes(timeLeft)));
                    return true;
                }
                if(args[1].equalsIgnoreCase("tag") || args[1].equalsIgnoreCase("nacht")) {
                    cooldownTimestamp = System.currentTimeMillis();
                    if(args[1].equalsIgnoreCase("tag")){
                        vote.vote("Tag", p.getWorld());
                    } else{
                        vote.vote("Nacht", p.getWorld());
                    }
                }
                break;
            }
            case "vote": {
                if(!isVoteRunning){
                    MessageAdapter.sendMessage(sender, Message.Error.CMD_VOTE_NOT_RUNNING);
                    return true;
                }
                switch (args[1].toLowerCase()){
                    case "yes":{}
                    case "ja":{
                        if(vote.addVote(true, sender)){
                            MessageAdapter.sendMessage(sender,Message.Info.CMD_VOTE_VOTED_YES);
                            break;
                        }
                        MessageAdapter.sendMessage(sender,Message.Error.CMD_VOTE_ALREADY_VOTED);
                        break;
                    }
                    case "no":{}
                    case "nein":{
                        if(vote.addVote(false, sender)){
                            MessageAdapter.sendMessage(sender,Message.Info.CMD_VOTE_VOTED_NO);
                            break;
                        }
                        MessageAdapter.sendMessage(sender,Message.Error.CMD_VOTE_ALREADY_VOTED);
                        break;
                }
                    default: {
                        MessageAdapter.sendMessage(sender,Message.Error.CMD_VOTE_WRONG_SYNTAX);
                        break;
                    }
                }
                break;
            }
            default: {
                MessageAdapter.sendMessage(sender, Message.Error.CMD_VOTE_WRONG_SYNTAX);
                break;
            }
        }
        return true;
    }
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> argsBasic = new ArrayList<>();
        List<String> argsVote = new ArrayList<>();
        List<String> argsCreate = new ArrayList<>();
        List<String> argsEmpty = new ArrayList<>();
        argsBasic.add("vote");
        if(sender.hasPermission("paradubsch.createvote")){
            argsBasic.add("create");
        }
        argsVote.add("ja");
        argsVote.add("nein");
        argsCreate.add("tag");
        argsCreate.add("nacht");
        if(args.length == 1){
            return argsBasic;
        }
        if(args.length >= 3){
            return argsEmpty;
        }
        switch (args[0]){
            case "create":{
                return argsCreate;
            }
            case "vote": {
                return argsVote;
            }
            default:{
                return argsEmpty;
            }
        }
    }
}
