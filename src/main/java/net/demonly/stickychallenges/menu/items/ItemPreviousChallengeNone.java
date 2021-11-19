package net.demonly.stickychallenges.menu.items;

import net.demonly.stickychallenges.StickyChallengesPlugin;
import net.demonly.stickychallenges.util.Util;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.stream.Collectors;

public class ItemPreviousChallengeNone {

    public static ItemStack get(StickyChallengesPlugin plugin) {
        ItemStack itemStack = new ItemStack(Material.valueOf(plugin.getLang().getString("item_previous_challenge.none.material")));
        ItemMeta meta = itemStack.getItemMeta();

        meta.setLore(plugin.getLang().getStringList("item_previous_challenge.none.lore")
                .stream().map(Util::translate).collect(Collectors.toList())
        );

        meta.setDisplayName(Util.translate(plugin.getLang().getString("item_previous_challenge.none.name")));

        itemStack.setItemMeta(meta);
        return  itemStack;
    }

}
