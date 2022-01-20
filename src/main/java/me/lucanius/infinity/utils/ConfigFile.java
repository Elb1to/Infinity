package me.lucanius.infinity.utils;

import lombok.Getter;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by Lucanius
 * Project: Infinity
 * Created on 20/01/2022 at 18:16
 */
public class ConfigFile extends YamlConfiguration {

    @Getter private final File file;

    public ConfigFile(JavaPlugin plugin, String name) {
        this.file = new File(plugin.getDataFolder(), name);
        if (!this.file.exists()) {
            plugin.saveResource(name, false);
        }

        try {
            this.load(this.file);
        } catch (IOException | InvalidConfigurationException exception) {
            exception.printStackTrace();
        }
    }

    public void save() {
        try {
            this.save(this.file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public int getInt(String path) {
        return super.getInt(path, 0);
    }

    @Override
    public double getDouble(String path) {
        return super.getDouble(path, 0.0);
    }

    @Override
    public boolean getBoolean(String path) {
        return super.getBoolean(path, false);
    }

    public String getUncoloredString(String path) {
        return super.getString(path, null);
    }

    public String getUncoloredString(String path, String def) {
        return super.getString(path, def);
    }

    @Override
    public String getString(String path) {
        return CC.translate(super.getString(path, "&cString at path &c&l'" + path + "' &cnot found.").replace("<new-line>", "\n"));
    }

    public String getString(String path, boolean ignored) {
        return super.getString(path, null);
    }

    @Override
    public List<String> getStringList(String path) {
        return CC.translate(super.getStringList(path));
    }

    public List<String> getStringList(String path, boolean ignored) {
        if (!super.contains(path)) {
            return null;
        }

        return CC.translate(super.getStringList(path));
    }

    public List<String> getStringList(String path, List<String> def) {
        if (!super.contains(path)) {
            return def;
        }

        return CC.translate(super.getStringList(path));
    }
}
