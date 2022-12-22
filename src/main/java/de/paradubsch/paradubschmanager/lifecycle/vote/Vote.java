package de.paradubsch.paradubschmanager.lifecycle.vote;

import de.paradubsch.paradubschmanager.ParadubschManager;
import de.paradubsch.paradubschmanager.util.MessageAdapter;
import de.paradubsch.paradubschmanager.util.lang.Message;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class Vote {
    private int votesYes;
    private int votesNo;
    private boolean result;
    private List<CommandSender> voted;
    public void vote(String argument, World world){
        MessageAdapter.broadcastMessage(Message.Info.VOTE_STARTED, argument);
        voted = new ArrayList<>();
        TimeVote.isVoteRunning = true;
        Bukkit.getScheduler().runTaskLater(ParadubschManager.getInstance(), () ->{
            result = votesYes > votesNo;
            if(result){
                MessageAdapter.broadcastMessage(Message.Info.VOTE_ENDED, "&aAngenommen");
                        if(argument.equals("Tag")) {
                            MessageAdapter.broadcastMessage(Message.Info.VOTE_TIME_CHANGE, "Tag");
                            world.setTime(6000);
                        } else if (argument.equals("Nacht")) {
                            MessageAdapter.broadcastMessage(Message.Info.VOTE_TIME_CHANGE, "Nacht");
                            world.setTime(20000);
                        }
                    TimeVote.isVoteRunning = false;
                    return;
            } else {
                MessageAdapter.broadcastMessage(Message.Info.VOTE_ENDED, "&cAbgelehnt.");
            }
            TimeVote.isVoteRunning = false;
        },30L * 20L);
    }
    public boolean addVote(boolean vote, CommandSender sender){
        if(voted.contains(sender)){
            return false;
        }
        voted.add(sender);
        if(vote){
            votesYes++;
        }else {
            votesNo++;
        }
        return true;
    }
}
