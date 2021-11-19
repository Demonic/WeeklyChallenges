package net.demonly.stickychallenges.menu.items;

import net.demonly.stickychallenges.StickyChallengesPlugin;
import net.demonly.stickychallenges.util.Util;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemCloseButton {

    public static ItemStack get(StickyChallengesPlugin plugin) {
        ItemStack itemStack = new ItemStack(Material.valueOf(plugin.getLang().getString("item_close.material")));
        ItemMeta meta = itemStack.getItemMeta();

        meta.setDisplayName(Util.translate(plugin.getLang().getString("item_close.name")));
        itemStack.setItemMeta(meta);

        return itemStack;
    }

}
