package net.demonly.stickychallenges.events.challenges;

import net.demonly.stickychallenges.StickyChallengesPlugin;
import net.demonly.stickychallenges.challenge.Challenge;
import net.demonly.stickychallenges.challenge.ChallengeType;
import net.demonly.stickychallenges.util.Util;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

import java.util.UUID;

public class EventFish implements Listener {

    private final StickyChallengesPlugin plugin;

    public EventFish(StickyChallengesPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSuccessfulFish(PlayerFishEvent ev) {
        UUID uuid = ev.getPlayer().getUniqueId();
        Challenge current = plugin.getCurrentChallenge();

        if (current.getType() == ChallengeType.FISH) {
            if (ev.getCaught().getType() == EntityType.valueOf(current.getObjective())) {
                Util.updateChallengeContributor(current, plugin, uuid);
            }
        }
    }
}
