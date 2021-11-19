package net.demonly.stickychallenges.menu.items;

import net.demonly.stickychallenges.StickyChallengesPlugin;
import net.demonly.stickychallenges.challenge.Challenge;
import net.demonly.stickychallenges.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;
import java.util.stream.Collectors;

public class ItemCurrentChallenge {

    public static ItemStack get(StickyChallengesPlugin plugin) {
        ItemStack itemStack = new ItemStack(Material.valueOf(plugin.getLang().getString("item_current_challenge.material")));
        ItemMeta meta = itemStack.getItemMeta();
        Challenge current = plugin.getCurrentChallenge();
        String objectiveText = plugin.getChallengeConfig().getString("Challenges." + current.getName() + ".objective_text");
        String progressBar = Util.getProgressBar(plugin, current.getCurrentAmount(), current.getAmount());
        String timeLeft = Util.formatTimeLeft(current.getEndDate() - System.currentTimeMillis());

        meta.setLore(plugin.getLang().getStringList("item_current_challenge.lore")
                .stream()
                .map(string -> Util.translate(string)
                .replaceAll("%objective%", objectiveText)
                .replaceAll("%progressbar%", progressBar)
                .replaceAll("%currentamount%", Long.toString(current.getCurrentAmount()))
                .replaceAll("%maxamount%", Long.toString(current.getAmount()))
                .replaceAll("%time%", timeLeft)
                .replaceAll("%first%", Util.getPlayerName(plugin.getCurrentTopContributors().get(1)))
                .replaceAll("%second%", Util.getPlayerName(plugin.getCurrentTopContributors().get(2)))
                .replaceAll("%third%", Util.getPlayerName(plugin.getCurrentTopContributors().get(3))))
                .collect(Collectors.toList())
        );

        meta.setDisplayName(plugin.getLang().getString("item_current_challenge.name"));

        itemStack.setItemMeta(meta);
        return  itemStack;
    }
}
