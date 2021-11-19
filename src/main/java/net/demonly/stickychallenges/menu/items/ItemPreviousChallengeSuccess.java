package net.demonly.stickychallenges.menu.items;

import net.demonly.stickychallenges.StickyChallengesPlugin;
import net.demonly.stickychallenges.challenge.Challenge;
import net.demonly.stickychallenges.util.Util;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.stream.Collectors;

public class ItemPreviousChallengeSuccess {

    public static ItemStack get(StickyChallengesPlugin plugin) {
        ItemStack itemStack = new ItemStack(Material.valueOf(plugin.getLang().getString("item_previous_challenge.success.material")));
        ItemMeta meta = itemStack.getItemMeta();
        Challenge previous = plugin.getPreviousChallenge();
        String objectiveText = plugin.getChallengeConfig().getString("Challenges." + previous.getName() + ".objective_text");
        String progressBar = Util.getProgressBar(plugin, previous.getCurrentAmount(), previous.getAmount());
        String timeLeft = Util.formatTimeLeft(previous.getEndDate() - System.currentTimeMillis());

        meta.setLore(plugin.getLang().getStringList("item_previous_challenge.success.lore")
                .stream()
                .map(string -> Util.translate(string)
                        .replaceAll("%objective%", objectiveText)
                        .replaceAll("%progressbar%", progressBar)
                        .replaceAll("%currentamount%", Long.toString(previous.getCurrentAmount()))
                        .replaceAll("%maxamount%", Long.toString(previous.getAmount()))
                        .replaceAll("%first%", Util.getPlayerName(plugin.getPreviousTopContributors().get(1)))
                        .replaceAll("%second%", Util.getPlayerName(plugin.getPreviousTopContributors().get(2)))
                        .replaceAll("%third%", Util.getPlayerName(plugin.getPreviousTopContributors().get(3))))
                .collect(Collectors.toList())
        );

        meta.setDisplayName(Util.translate(plugin.getLang().getString("item_previous_challenge.success.name")));

        itemStack.setItemMeta(meta);
        return  itemStack;
    }

}
