package net.demonly.stickychallenges.configuration;

import com.google.common.io.ByteStreams;
import net.demonly.stickychallenges.StickyChallengesPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class Config {
    private final File file;
    private FileConfiguration config;
    private final StickyChallengesPlugin plugin;

    public Config(File file, StickyChallengesPlugin plugin) {
        this.file = file;
        this.plugin = plugin;
        this.load();
    }

    public FileConfiguration get() {
        return config;
    }

    private void load() {
        try {
            if (!file.exists()) {
                plugin.getDataFolder().mkdirs();
                try {
                    InputStream in = plugin.getResource(file.getName());
                    OutputStream out = new FileOutputStream(file);
                    ByteStreams.copy(in, out);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            config = YamlConfiguration.loadConfiguration(file);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            config.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
