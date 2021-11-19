package net.demonly.stickychallenges.events.challenges;

import net.demonly.stickychallenges.StickyChallengesPlugin;
import net.demonly.stickychallenges.challenge.Challenge;
import net.demonly.stickychallenges.challenge.ChallengeType;
import net.demonly.stickychallenges.util.Util;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.UUID;

public class EventKill implements Listener {

    private final StickyChallengesPlugin plugin;

    public EventKill(StickyChallengesPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onKill(EntityDeathEvent ev) {
        if (ev.getEntity().getKiller() != null){
            UUID uuid = ev.getEntity().getKiller().getUniqueId();
            Challenge current = plugin.getCurrentChallenge();

            if (current.getType() == ChallengeType.KILL) {
                if (ev.getEntity().getType() == EntityType.valueOf(current.getObjective())) {
                    Util.updateChallengeContributor(current, plugin, uuid);
                }
            }
        }
    }
}
