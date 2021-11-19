package net.demonly.stickychallenges.events;

import net.demonly.stickychallenges.StickyChallengesPlugin;
import net.demonly.stickychallenges.challenge.Challenge;
import net.demonly.stickychallenges.challenge.ChallengeContributor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;
import java.util.logging.Level;

public class EventPlayerDataLoad implements Listener {

    private final StickyChallengesPlugin plugin;

    public EventPlayerDataLoad(StickyChallengesPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent ev) {
        UUID uuid = ev.getPlayer().getUniqueId();
        Challenge current = plugin.getCurrentChallenge();
        Challenge previous = plugin.getPreviousChallenge();

        if (plugin.getContributors().stream().noneMatch(contributor -> contributor.getUuid().equals(uuid))) {
            ChallengeContributor contributor = plugin.getChallengeData().getContributor(uuid, current.getId());
            if (contributor == null) {
                contributor = new ChallengeContributor();
                contributor.setChallengeId(current.getId());
                contributor.setUuid(uuid);
                contributor.setClaimed(false);
                contributor.setAmount(0);
            }
            plugin.getContributors().add(contributor);
        }

        if (plugin.getPreviousContributors().stream().noneMatch(contributor -> contributor.getUuid().equals(uuid))) {
            if (plugin.getPreviousChallenge() != null) {
                ChallengeContributor previousContributor = plugin.getChallengeData().getContributor(uuid, previous.getId());
                if (previousContributor != null) {
                    plugin.getPreviousContributors().add(previousContributor);
                }
            }
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent ev) {
        UUID uuid = ev.getPlayer().getUniqueId();
        Challenge current = plugin.getCurrentChallenge();
        ChallengeContributor contributor = plugin.getContributors().stream().filter(contrib -> contrib.getUuid().equals(uuid)).findFirst().orElse(null);
        ChallengeContributor previousContributor = plugin.getPreviousContributors().stream().filter(contrib -> contrib.getUuid().equals(uuid)).findFirst().orElse(null);

        if (contributor != null) {
            plugin.getChallengeData().updateContributor(contributor);
            plugin.getContributors().remove(contributor);
        } else {
            plugin.getLogger().log(Level.SEVERE, "Contributor left and no data was found, ", uuid);
        }

        if (previousContributor != null) {
            plugin.getPreviousContributors().remove(previousContributor);
        }
    }
}
