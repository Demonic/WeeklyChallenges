package net.demonly.stickychallenges.commands;

import net.demonly.stickychallenges.StickyChallengesPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandEndChallenge implements CommandExecutor {

    private final StickyChallengesPlugin plugin;

    public CommandEndChallenge(StickyChallengesPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String lbl, String[] args) {

        plugin.getCurrentChallenge().setEndDate(System.currentTimeMillis());
        sender.sendMessage("Ended the current challenge.");

        return true;
    }
}
