package net.demonly.stickychallenges.events.challenges;

import net.demonly.stickychallenges.StickyChallengesPlugin;
import net.demonly.stickychallenges.challenge.Challenge;
import net.demonly.stickychallenges.challenge.ChallengeType;
import net.demonly.stickychallenges.util.Util;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.UUID;

public class EventPlace implements Listener {

    private final StickyChallengesPlugin plugin;

    public EventPlace(StickyChallengesPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent ev) {
        UUID uuid = ev.getPlayer().getUniqueId();
        Challenge current = plugin.getCurrentChallenge();

        if (current.getType() == ChallengeType.PLACE) {
            if (ev.getBlock().getType() == Material.getMaterial(current.getObjective())) {
                Util.updateChallengeContributor(current, plugin, uuid);
            }
        }
    }
}
