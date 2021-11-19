package net.demonly.stickychallenges.util;

import com.google.common.base.Strings;
import net.demonly.stickychallenges.StickyChallengesPlugin;
import net.demonly.stickychallenges.challenge.Challenge;
import net.demonly.stickychallenges.challenge.ChallengeContributor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class Util {

    public static void updateChallengeContributor(Challenge challenge, StickyChallengesPlugin plugin, UUID uuid) {
        challenge.setCurrentAmount(Long.sum(challenge.getCurrentAmount(), 1));
        ChallengeContributor contributor = plugin.getContributors().stream().filter(contrib -> contrib.getUuid().equals(uuid)).findFirst().orElse(null);
        if (contributor != null) {
            contributor.setAmount(Long.sum(contributor.getAmount(), 1));
        } else {
            plugin.getLogger().log(Level.WARNING, "Contributor data was not found. Contribute not counted for " + uuid.toString());
        }
    }

    public static String translate(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static String getProgressBar(StickyChallengesPlugin plugin, long current, long max) {
        String symbol = plugin.getConfig().getString("Progress_bar.symbol");
        String completedColor = plugin.getConfig().getString("Progress_bar.completed_color");
        String normalColor = plugin.getConfig().getString("Progress_bar.normal_color");

        float percent = (float) current / max;
        int totalBars = plugin.getConfig().getInt("Progress_bar.amount");
        int progressBars = (int) (totalBars * percent);

        if (progressBars > totalBars) {
            progressBars = totalBars;
        }

        return translate(Strings.repeat("" + completedColor + symbol, progressBars)
                + Strings.repeat("" + normalColor + symbol, totalBars - progressBars));
    }

    public static String getPlayerName(UUID uuid) {
        if (uuid != null) {
            String name = Bukkit.getOfflinePlayer(uuid).getName();
            if (name != null) {
                return name;
            } else {
                return "";
            }
        }
        return "";
    }

    public static String formatTimeLeft(long duration) {
        StringBuilder builder = new StringBuilder();

        long days = TimeUnit.MILLISECONDS.toDays(duration);
        long hours = TimeUnit.MILLISECONDS.toHours(duration);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(duration);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(duration);

        seconds -= TimeUnit.MINUTES.toSeconds(minutes);
        minutes -= TimeUnit.HOURS.toMinutes(hours);
        hours -= TimeUnit.DAYS.toHours(days);

        if (days > 0) {
            builder.append(days).append(" day");
            if (days > 1) {
                builder.append("s");
            }
        }

        if (hours > 0) {
            if (builder.length() != 0) {
                builder.append(" ");
            }
            builder.append(hours).append(" hour");
            if (hours > 1) {
                builder.append("s");
            }
        }

        if (minutes > 0) {
            if (builder.length() != 0) {
                builder.append(" ");
            }
            builder.append(minutes).append(" min");
            if (minutes > 1) {
                builder.append("s");
            }
        }

        if (seconds > 0) {
            if (builder.length() != 0) {
                builder.append(" ");
            }
            builder.append(seconds).append(" sec");
            if (seconds > 1) {
                builder.append("s");
            }
        }

        if (duration == 0) {
            builder.append("0 sec");
        }

        return builder.toString();
    }
}