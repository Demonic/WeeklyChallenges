package net.demonly.stickychallenges;

import com.github.dig.gui.GuiOpener;
import com.github.dig.gui.GuiRegistry;
import net.demonly.stickychallenges.challenge.Challenge;
import net.demonly.stickychallenges.challenge.ChallengeContributor;
import net.demonly.stickychallenges.commands.CommandEndChallenge;
import net.demonly.stickychallenges.commands.CommandWeeklyChallenge;
import net.demonly.stickychallenges.configuration.Config;
import net.demonly.stickychallenges.events.EventChallenge;
import net.demonly.stickychallenges.events.EventPlayerDataLoad;
import net.demonly.stickychallenges.events.challenges.*;
import net.demonly.stickychallenges.menu.MenuChallenge;
import net.demonly.stickychallenges.sql.ChallengeData;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;

public class StickyChallengesPlugin extends JavaPlugin {

    private final ArrayList<ChallengeContributor> contributors = new ArrayList<>();
    private final ArrayList<ChallengeContributor> previousContributors = new ArrayList<>();
    private final EventChallenge eventChallenge = new EventChallenge(this);
    private final HashMap<Integer, UUID> currentTopContributors =  new HashMap<>();
    private final HashMap<Integer, UUID> previousTopContributors = new HashMap<>();
    private boolean challengeCompleted = false;
    private GuiOpener guiOpener;
    private GuiRegistry guiRegistry;
    private Challenge currentChallenge;
    private Challenge previousChallenge;
    private ChallengeData challengeData;
    private Config config;
    private Config lang;
    private Config challenges;
    private int autosave;
    private int autoupdate;

    @Override
    public void onEnable() {
        loadConfigs();
        loadChallengeData();

        getServer().getPluginManager().registerEvents(new EventBreak(this), this);
        getServer().getPluginManager().registerEvents(new EventCraft(this), this);
        getServer().getPluginManager().registerEvents(new EventPlace(this), this);
        getServer().getPluginManager().registerEvents(new EventKill(this), this);
        getServer().getPluginManager().registerEvents(new EventFish(this), this);

        getCommand("endchallenge").setExecutor(new CommandEndChallenge(this));
        getCommand("weeklychallenge").setExecutor(new CommandWeeklyChallenge(this));

        eventChallenge.checkChallengeInfo();
        eventChallenge.autosave();
        eventChallenge.autoupdate();

        getServer().getPluginManager().registerEvents(new EventPlayerDataLoad(this), this);

        guiRegistry = new GuiRegistry(this);
        guiOpener = new GuiOpener(guiRegistry);
    }

    @Override
    public void onDisable() {
        contributors.forEach(contributor -> challengeData.updateContributor(contributor));
        challengeData.updateCurrentChallenge(currentChallenge);
        Bukkit.getScheduler().cancelTask(autosave);
        Bukkit.getScheduler().cancelTask(autoupdate);
    }

    public void loadConfigs() {
        config = new Config(new File(getDataFolder(), "config.yml"), this);
        lang = new Config(new File(getDataFolder(), "lang.yml"), this);
        challenges = new Config(new File(getDataFolder(), "challenges.yml"), this);
    }

    public void loadChallengeData() {
        getLogger().log(Level.INFO, "Initializing challenge data");
        challengeData = new ChallengeData(this);
        challengeData.init();

        currentChallenge = challengeData.getCurrentChallenge();
        if (currentChallenge == null) {
            getLogger().log(Level.INFO, "Challenge data not found. Loading new challenge");
            new EventChallenge(this).startNewChallenge();
        } else {
            getLogger().log(Level.INFO, "Challenge data found, loading challenge");

            if (currentChallenge.getId() > 1) {
                getLogger().log(Level.INFO, "Previous challenge data found, loading challenge");
                previousChallenge = challengeData.getPreviousChallenge(currentChallenge.getId() - 1);
                challengeData.getTopContributors(currentChallenge.getId() - 1, previousTopContributors);
            }
        }
    }

    public HashMap<Integer, UUID> getCurrentTopContributors () {
        return this.currentTopContributors;
    }

    public HashMap<Integer, UUID> getPreviousTopContributors() {
        return this.previousTopContributors;
    }

    public void setAutosave(int autosave) {
        this.autosave = autosave;
    }

    public void setAutoupdate(int autoupdate) {
        this.autoupdate = autoupdate;
    }

    public ArrayList<ChallengeContributor> getContributors() {
        return this.contributors;
    }

    public ArrayList<ChallengeContributor> getPreviousContributors() {
        return previousContributors;
    }

    public ChallengeData getChallengeData() {
        return this.challengeData;
    }

    public Challenge getCurrentChallenge() {
        return this.currentChallenge;
    }

    public void setCurrentChallenge(Challenge challenge) {
        this.currentChallenge = challenge;
    }

    public Challenge getPreviousChallenge() {
        return this.previousChallenge;
    }

    public void setPreviousChallenge(Challenge challenge) {
        this.previousChallenge = challenge;
    }

    public FileConfiguration getLang() {
        return this.lang.get();
    }

    public FileConfiguration getConfig() {
        return this.config.get();
    }

    public FileConfiguration getChallengeConfig() {
        return this.challenges.get();
    }

    public GuiOpener getGuiOpener() {
        return this.guiOpener;
    }

    public boolean isChallengeCompleted() {
        return this.challengeCompleted;
    }

    public void setChallengeCompleted(boolean completed) {
        this.challengeCompleted = completed;
    }

}
