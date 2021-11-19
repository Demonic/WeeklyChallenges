package net.demonly.stickychallenges.events;

import net.demonly.stickychallenges.StickyChallengesPlugin;
import net.demonly.stickychallenges.challenge.Challenge;
import net.demonly.stickychallenges.challenge.ChallengeContributor;
import net.demonly.stickychallenges.challenge.ChallengeType;
import net.demonly.stickychallenges.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;

import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;

public class EventChallenge {

    private static final Random RANDOM = new Random();
    private final StickyChallengesPlugin plugin;

    public EventChallenge(StickyChallengesPlugin plugin) {
        this.plugin = plugin;
    }

    public void startNewChallenge() {
        ConfigurationSection config = plugin.getChallengeConfig().getConfigurationSection("Challenges");
        int size = config.getKeys(false).size();
        plugin.getCurrentTopContributors().clear();

        // no challenge found
        if (plugin.getCurrentChallenge() == null) {

            String challengeName = config.getKeys(false)
                    .stream()
                    .skip(RANDOM.nextInt(size -1))
                    .findFirst()
                    .orElse(null);

            Challenge challenge = defineChallenge(
                    challengeName,
                    config.getLong(challengeName + ".amount"),
                    config.getString(challengeName + ".type"),
                    config.getString(challengeName + ".objective"),
                    System.currentTimeMillis(),
                    calcNextMonday(),
                    0,
                    1);

            plugin.getChallengeData().insertChallenge(challenge);
            plugin.setCurrentChallenge(challenge);
        } else {
            Challenge current = plugin.getCurrentChallenge();

            if (!plugin.getContributors().isEmpty()) {
                plugin.getPreviousContributors().clear();

                // Update to previous contributors and update current contributors
                ArrayList<ChallengeContributor> newContributors = new ArrayList<>();
                plugin.getContributors().forEach(contributor -> {
                    plugin.getPreviousContributors().add(contributor);
                    plugin.getChallengeData().updateContributor(contributor);

                    ChallengeContributor newContributor = new ChallengeContributor();
                    newContributor.setUuid(contributor.getUuid());
                    newContributor.setClaimed(false);
                    newContributor.setAmount(0);
                    newContributor.setChallengeId(current.getId() + 1);

                    newContributors.add(newContributor);
                });

                plugin.getContributors().removeIf(contributor -> contributor.getChallengeId() == current.getId());

                newContributors.forEach(contributor -> {
                    plugin.getContributors().add(contributor);
                });
            }

            String challengeName = config.getKeys(false)
                    .stream()
                    .filter(string -> !string.equalsIgnoreCase(current.getName()))
                    .skip(RANDOM.nextInt(size -1))
                    .findFirst()
                    .orElse(null);

            Challenge challenge = defineChallenge(
                    challengeName,
                    config.getLong(challengeName + ".amount"),
                    config.getString(challengeName + ".type"),
                    config.getString(challengeName + ".objective"),
                    System.currentTimeMillis(),
                    calcNextMonday(),
                    0,
                    current.getId() + 1);

            plugin.getPreviousTopContributors().clear();
            plugin.getChallengeData().getTopContributors(current.getId(), plugin.getPreviousTopContributors());
            plugin.setPreviousChallenge(current);
            plugin.getChallengeData().insertPreviousChallenge(current);
            plugin.getChallengeData().updateCurrentChallenge(challenge);

            plugin.setCurrentChallenge(challenge);
        }
    }

    public void checkChallengeInfo() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {

            checkIfChallengeCompleted(plugin.getCurrentChallenge());
            newChallenge();

        }, 0, 20L);
    }

    private void newChallenge() {
        if (plugin.getCurrentChallenge() != null) {
            if (plugin.getCurrentChallenge().getEndDate() - System.currentTimeMillis() <= 0) {
                try {
                    plugin.setChallengeCompleted(false);
                    startNewChallenge();
                } catch (Exception e) {
                    plugin.getLogger().log(Level.SEVERE, "ERROR STARTING NEW CHALLENGE", e);
                }
            }
        }
    }

    private void checkIfChallengeCompleted(Challenge challenge) {
        if (challenge != null && challenge.getCurrentAmount() >= challenge.getAmount() && !plugin.isChallengeCompleted()) {

            plugin.setChallengeCompleted(true);

            Bukkit.getOnlinePlayers().forEach(player -> {
                player.sendTitle(
                        Util.translate(plugin.getLang().getString("title_main")),
                        Util.translate(plugin.getLang().getString("title_sub")),
                        plugin.getConfig().getInt("title.fade_in"),
                        plugin.getConfig().getInt("title.stay"),
                        plugin.getConfig().getInt("title.fade_out"));
                player.playSound(player.getLocation(), Sound.valueOf(plugin.getConfig().getString("challenge_completed_sound")), 1f, 1f);
            });
        }
    }

    public void autoupdate() {
        plugin.setAutoupdate(Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            plugin.getChallengeData().getTopContributors(plugin.getCurrentChallenge().getId(), plugin.getCurrentTopContributors());
        }, 0, 20 * plugin.getConfig().getInt("autoupdate")).getTaskId());
    }

    public void autosave() {
        plugin.setAutosave(Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            plugin.getLogger().log(Level.INFO, "Auto saving challenge and contributor data");
            try {
                if (plugin.getCurrentChallenge() != null) {
                    plugin.getChallengeData().updateCurrentChallenge(plugin.getCurrentChallenge());
                    plugin.getContributors().forEach(contributor -> plugin.getChallengeData().updateContributor(contributor));
                }
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE, "ERROR SAVING CHALLENGE AND CONTRIBUTOR DATA", e);
            }
        }, 0, (20*60) * plugin.getConfig().getInt("autosave")).getTaskId());
    }

    private long calcNextMonday() {
        ZoneId z = ZoneId.of("America/New_York");
        ZoneOffset offset = z.getRules().getOffset(Instant.now());
        return LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY)).atStartOfDay(offset).toInstant().toEpochMilli();
    }

    private Challenge defineChallenge(String name, long amount, String type, String objective, long startDate, long endDate, long currentAmount, int id) {
        Challenge challenge = new Challenge();
        challenge.setName(name);
        challenge.setAmount(amount);
        challenge.setType(ChallengeType.valueOf(type));
        challenge.setObjective(objective);
        challenge.setStartDate(startDate);
        challenge.setEndDate(endDate);
        challenge.setCurrentAmount(currentAmount);
        challenge.setId(id);

        return challenge;
    }
}
