package net.demonly.stickychallenges.menu.items;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemBorder {

    public static ItemStack get() {
        ItemStack itemStack = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta meta = itemStack.getItemMeta();

        meta.setDisplayName(" ");
        itemStack.setItemMeta(meta);

        return itemStack;
    }
}