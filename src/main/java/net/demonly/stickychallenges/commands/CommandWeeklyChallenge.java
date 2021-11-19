package net.demonly.stickychallenges.commands;

import net.demonly.stickychallenges.StickyChallengesPlugin;
import net.demonly.stickychallenges.challenge.Challenge;
import net.demonly.stickychallenges.menu.MenuChallenge;
import net.demonly.stickychallenges.util.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandWeeklyChallenge implements CommandExecutor {

    private final StickyChallengesPlugin plugin;

    public CommandWeeklyChallenge(StickyChallengesPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String lbl, String[] args) {

        boolean canReload = sender.hasPermission("stickchallenges.reload");
        boolean canEnd = sender.hasPermission("stickchallenges.end");

        if (!(args.length > 0)) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                plugin.getGuiOpener().open(p, new MenuChallenge(plugin));
            }
        } else {
            if (args[0].equalsIgnoreCase("reload") && canReload) {
                sender.sendMessage(Util.translate(plugin.getLang().getString("command_reload")));
                plugin.loadConfigs();
            } else if (args[0].equalsIgnoreCase("end") && canEnd) {
                sender.sendMessage(Util.translate(plugin.getLang().getString("command_end")));
                plugin.getCurrentChallenge().setEndDate(System.currentTimeMillis());
            } else if (canEnd || canReload) {
                sender.sendMessage(Util.translate(plugin.getLang().getString("command_invalid")));
            } else {
                sender.sendMessage(Util.translate(plugin.getLang().getString("command_denied")));
            }
        }

        return true;
    }
}
