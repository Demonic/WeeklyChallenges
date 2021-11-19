package net.demonly.stickychallenges.commands;

import net.demonly.stickychallenges.StickyChallengesPlugin;
import net.demonly.stickychallenges.challenge.Challenge;
import net.demonly.stickychallenges.util.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * @Deprecated
 * Deprecated info class used for development only.
 */
@Deprecated
public class CommandCurrentChallengeInfo implements CommandExecutor {

    private final StickyChallengesPlugin plugin;

    public CommandCurrentChallengeInfo(StickyChallengesPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String lbl, String[] args) {
        Challenge current = plugin.getCurrentChallenge();
        Challenge previous = plugin.getPreviousChallenge();
        if (current !=  null) {
            sender.sendMessage("=-=-=-=-=-= Challenge Information =-=-=-=-=-=");
            sender.sendMessage("Challenge Name: " + current.getName());
            sender.sendMessage("Challenge Current Amount: " + current.getCurrentAmount());
            sender.sendMessage("Challenge Objective amount: " + current.getAmount());
            sender.sendMessage("Challenge ID: " + current.getId());
            sender.sendMessage("Challenge Type: " + current.getType().name());
            sender.sendMessage("Challenge Objective: " + current.getObjective());
            sender.sendMessage("Challenge Start Date: " + current.getStartDate());
            sender.sendMessage("Challenge End Date: " + current.getEndDate());
            sender.sendMessage(Util.getProgressBar(plugin, current.getCurrentAmount(), current.getAmount()));
            sender.sendMessage("=-=-=-=-=-= Contributors Information =-=-=-=-=-=");
            plugin.getContributors().forEach(contributor -> {
                sender.sendMessage("Contribute UUID: " + contributor.getUuid().toString());
                sender.sendMessage("Contribute Amount: " + contributor.getAmount());
                sender.sendMessage("Challenge ID: " + contributor.getChallengeId());
            });
        }
        if (previous != null) {
            sender.sendMessage("");
            sender.sendMessage("=-=-=-=-=-= Previous Information =-=-=-=-=-=");
            sender.sendMessage("Challenge Name: " + previous.getName());
            sender.sendMessage("Challenge Current Amount: " + previous.getCurrentAmount());
            sender.sendMessage("Challenge Objective amount: " + previous.getAmount());
            sender.sendMessage("Challenge ID: " + previous.getId());
            sender.sendMessage("Challenge Type: " + previous.getType().name());
            sender.sendMessage("Challenge Objective: " + previous.getObjective());
            sender.sendMessage("Challenge Start Date: " + previous.getStartDate());
            sender.sendMessage("Challenge End Date: " + previous.getEndDate());
            sender.sendMessage(Util.getProgressBar(plugin, previous.getCurrentAmount(), previous.getAmount()));
            plugin.getPreviousContributors().forEach(contributor -> {
                sender.sendMessage("Contribute UUID: " + contributor.getUuid().toString());
                sender.sendMessage("Contribute Amount: " + contributor.getAmount());
                sender.sendMessage("Challenge ID: " + contributor.getChallengeId());
            });
        }
        return true;
    }
}
