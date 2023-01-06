package net.tickmc.lccutils.managers.configuration;

import net.tickmc.lccutils.LccUtils;
import net.tickmc.lccutils.utilities.Debug;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class ConfigurationManager {

    public static @Nullable Map<String, ?> getMap(FileConfiguration config, String... paths) {
        ConfigurationSection section = config.getConfigurationSection(String.join(".", paths));
        if (section == null) {
            return null;
        }
        return section.getValues(false);
    }

    public static @Nullable FileConfiguration getConfiguration(String path, String defaultConfig) {
        File newFile = new File(LccUtils.getPlugin().getDataFolder(), path);
        if (!newFile.exists()) {
            Debug.log("Creating new configuration file " + path + "...");
            newFile.getParentFile().mkdirs();
            try {
                newFile.createNewFile();
                FileWriter writer = new FileWriter(newFile);
                writer.write(defaultConfig);
                writer.flush();
                writer.close();
            } catch (IOException e) {
                Debug.error("Failed to create new configuration file " + path + "!");
            }
        } else {
            Debug.devLog("Configuration file " + path + " already exists.");
        }

        YamlConfiguration newConfig = new YamlConfiguration();
        try {
            newConfig.load(newFile);
        } catch (IOException | InvalidConfigurationException e) {
            Debug.error("Failed to load configuration file " + path + "!");
            Debug.error(e);
            return null;
        }
        return newConfig;
    }
}