package me.lucanius.infinity;

import lombok.Getter;
import me.lucanius.infinity.listeners.PlayerListener;
import me.lucanius.infinity.providers.ScoreboardProvider;
import me.lucanius.infinity.utils.CC;
import me.lucanius.infinity.utils.ConfigFile;
import me.lucanius.infinity.utils.Messages;
import me.lucanius.infinity.utils.command.CommandManager;
import me.lucanius.infinity.utils.scoreboard.Assemble;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

/**
 * Created by Lucanius
 * Project: Infinity
 * Created on 20/01/2022 at 18:00
 */
@Getter
public final class Infinity extends JavaPlugin {

    @Getter private static Infinity instance;

    private ConfigFile settingsConfig, messagesConfig, scoreboardConfig, menusConfig;

    private Assemble scoreboard;

    private CommandManager commandManager;

    @Override
    public void onEnable() {
        instance = this;

        this.loadConfigs();

        this.getServer().getConsoleSender().sendMessage(Messages.CHAT_BAR.toString());
        this.getServer().getConsoleSender().sendMessage(CC.translate("&b&lInfinity&r &7- &a" + this.getDescription().getVersion()));
        this.getServer().getConsoleSender().sendMessage(CC.translate("&7* &fAuthors: &b" + StringUtils.join(this.getDescription().getAuthors(), "&7, &b")));
        this.getServer().getConsoleSender().sendMessage(CC.translate("&7* &fGitHub: &bhttps://github.com/FrozedClubDevelopment/Infinity"));
        this.getServer().getConsoleSender().sendMessage(Messages.CHAT_BAR.toString());

        this.loadManagers();
        this.loadListeners();
        this.loadProviders();

        try {
            this.commandManager.loadCommands();
        } catch (InvocationTargetException | IllegalAccessException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void onDisable() {

    }

    private void loadConfigs() {
        this.settingsConfig = new ConfigFile(this, "settings.yml");
        this.messagesConfig = new ConfigFile(this, "messages.yml");
        this.scoreboardConfig = new ConfigFile(this, "scoreboard.yml");
        this.menusConfig = new ConfigFile(this, "menus.yml");

        this.loadMessages();
    }

    private void loadMessages() {
        if (this.messagesConfig == null) {
            return;
        }

        Messages.setConfigFile(this.messagesConfig);
        Arrays.stream(Messages.values()).forEach(message -> {
            if (this.messagesConfig.getString(message.getPath(), true) == null) {
                if (message.getString() != null) {
                    this.messagesConfig.set(message.getPath(), message.getString());
                } else if (message.getStringList() != null && this.messagesConfig.getStringList(message.getPath(), true) == null) {
                    this.messagesConfig.set(message.getPath(), message.getStringList());
                }
            }
        });

        this.messagesConfig.save();
    }

    private void loadManagers() {
        this.commandManager = new CommandManager(this);
    }

    private void loadListeners() {
        Arrays.asList(
                new PlayerListener(this, this.scoreboard)
        ).forEach(listener -> this.getServer().getPluginManager().registerEvents(listener, this));
    }

    private void loadProviders() {
        this.scoreboard = new Assemble(this, new ScoreboardProvider());
        this.scoreboard.setTicks(1);
    }
}
