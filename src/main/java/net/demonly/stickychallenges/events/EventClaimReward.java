package net.demonly.stickychallenges.events;

import net.demonly.stickychallenges.StickyChallengesPlugin;
import net.demonly.stickychallenges.challenge.Challenge;
import net.demonly.stickychallenges.challenge.ChallengeContributor;
import net.demonly.stickychallenges.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class EventClaimReward {

    private final StickyChallengesPlugin plugin;

    public EventClaimReward(StickyChallengesPlugin plugin) {
        this.plugin = plugin;
    }

    public void claimReward(Player player) {
        ChallengeContributor contributor = plugin.getPreviousContributors().stream().filter(contrib -> contrib.getUuid().equals(player.getUniqueId())).findFirst().orElse(null);
        Challenge previous = plugin.getPreviousChallenge();
        ConsoleCommandSender console = Bukkit.getConsoleSender();
        if (contributor != null && previous != null && contributor.getAmount() > 0 && !contributor.isClaimed()) {
            plugin.getPreviousContributors().remove(contributor);

            contributor.setClaimed(true);

            plugin.getPreviousContributors().add(contributor);
            plugin.getChallengeData().updateContributor(contributor);

            if (plugin.getPreviousTopContributors().get(1).equals(player.getUniqueId())) {
                plugin.getChallengeConfig().getStringList("Challenges."+previous.getName()+".rewards.first").forEach(command ->{
                    Bukkit.dispatchCommand(console, command.replaceAll("%player%", player.getName()));
                });
            } else if (plugin.getPreviousTopContributors().get(2).equals(player.getUniqueId())) {
                plugin.getChallengeConfig().getStringList("Challenges."+previous.getName()+".rewards.second").forEach(command ->{
                    Bukkit.dispatchCommand(console, command.replaceAll("%player%", player.getName()));
                });
            } else if (plugin.getPreviousTopContributors().get(3).equals(player.getUniqueId())) {
                plugin.getChallengeConfig().getStringList("Challenges."+previous.getName()+".rewards.third").forEach(command ->{
                    Bukkit.dispatchCommand(console, command.replaceAll("%player%", player.getName()));
                });
            } else {
                plugin.getChallengeConfig().getStringList("Challenges."+previous.getName()+".rewards.default").forEach(command ->{
                    Bukkit.dispatchCommand(console, command.replaceAll("%player%", player.getName()));
                });
            }
        } else {
            player.sendMessage(Util.translate(plugin.getLang().getString("reward_claimed_not_found")));
        }
    }
}
