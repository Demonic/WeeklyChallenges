package net.demonly.stickychallenges.events.challenges;

import net.demonly.stickychallenges.StickyChallengesPlugin;
import net.demonly.stickychallenges.challenge.Challenge;
import net.demonly.stickychallenges.challenge.ChallengeType;
import net.demonly.stickychallenges.util.Util;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;

import java.util.UUID;

public class EventCraft implements Listener {

    private final StickyChallengesPlugin plugin;

    public EventCraft(StickyChallengesPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onCraftItem(CraftItemEvent ev) {
        UUID uuid = ev.getWhoClicked().getUniqueId();
        Challenge current = plugin.getCurrentChallenge();

        if (current.getType() == ChallengeType.CRAFT && ev.getCurrentItem() != null) {
            if (ev.getCurrentItem().getType() == Material.getMaterial(current.getObjective())) {
                Util.updateChallengeContributor(current, plugin, uuid);
            }
        }
    }
}
