package me.lucanius.infinity;

import lombok.Getter;
import me.lucanius.infinity.listeners.ChatListener;
import me.lucanius.infinity.listeners.EntityListener;
import me.lucanius.infinity.listeners.PlayerListener;
import me.lucanius.infinity.listeners.WorldListener;
import me.lucanius.infinity.managers.MongoManager;
import me.lucanius.infinity.managers.PlayerManager;
import me.lucanius.infinity.managers.arena.ArenaManager;
import me.lucanius.infinity.managers.cosmetics.CosmeticsManager;
import me.lucanius.infinity.managers.knockback.KnockbackManager;
import me.lucanius.infinity.providers.ScoreboardProvider;
import me.lucanius.infinity.utils.CC;
import me.lucanius.infinity.utils.ConfigFile;
import me.lucanius.infinity.utils.ItemBuilder;
import me.lucanius.infinity.utils.Messages;
import me.lucanius.infinity.utils.command.CommandManager;
import me.lucanius.infinity.utils.scoreboard.BoardManager;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by Lucanius
 * Project: Infinity
 * Created on 20/01/2022 at 18:00
 */
@Getter
public final class Infinity extends JavaPlugin {

    @Getter private static Infinity instance;

    private ConfigFile settingsConfig, messagesConfig, arenaConfig, scoreboardConfig, cosmeticsConfig;
    private final File schematicsFolder = new File(this.getDataFolder() + File.separator + "schematics");

    private final Random random = new Random();

    private BoardManager boardManager;
    private Location currentLocation;

    private MongoManager mongoManager;
    private PlayerManager playerManager;
    private CommandManager commandManager;
    private ArenaManager arenaManager;
    private KnockbackManager knockbackManager;
    private CosmeticsManager cosmeticsManager;

    private ItemStack placeholderGlass;
    private ItemStack mainMenuItem;

    @Override
    public void onEnable() {
        instance = this;

        if (!this.getDescription().getAuthors().contains("Elb1to") || !this.getDescription().getAuthors().contains("Lucanius") || !this.getDescription().getName().equals("Infinity")) {
            this.getServer().getConsoleSender().sendMessage(CC.translate("&cSKIDDER XDDDDD"));
            this.getServer().getPluginManager().disablePlugins();
        }

        this.loadConfigs();

        this.getServer().getConsoleSender().sendMessage(CC.chatBar);
        this.getServer().getConsoleSender().sendMessage(CC.translate("&b&lInfinity&r &7- &a" + this.getDescription().getVersion()));
        this.getServer().getConsoleSender().sendMessage(CC.translate("&7* &fAuthors: &b" + StringUtils.join(this.getDescription().getAuthors(), "&7, &b")));
        this.getServer().getConsoleSender().sendMessage(CC.translate("&7* &fGitHub: &bhttps://github.com/FrozedClubDevelopment/Infinity"));
        this.getServer().getConsoleSender().sendMessage(CC.chatBar);

        this.loadManagers();
        this.loadProviders();
        this.loadListeners();

        try {
            this.commandManager.loadCommands();
        } catch (InvocationTargetException | IllegalAccessException exception) {
            exception.printStackTrace();
        }

        this.currentLocation = new Location(this.getServer().getWorld("world"), 0, 64, 0);
    }

    @Override
    public void onDisable() {
        this.playerManager.getAllPlayerData().forEach(playerData -> {
            if (playerData.getEditSession() != null) {
                playerData.getEditSession().undo(playerData.getEditSession());
            }
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "npc remove " + playerData.getNpcId());
            playerData.clearPlacedBlocks();

            this.playerManager.savePlayerData(playerData);
        });

        this.mongoManager.disconnect();
    }

    private void loadManagers() {
        this.mongoManager = new MongoManager(this);
        this.playerManager = new PlayerManager(this);
        this.commandManager = new CommandManager(this);
        this.arenaManager = new ArenaManager(this);
        this.knockbackManager = new KnockbackManager(this);
        this.cosmeticsManager = new CosmeticsManager(this);
    }

    private void loadListeners() {
        Arrays.asList(
                new PlayerListener(this, this.boardManager), new EntityListener(this), new WorldListener(this),
                new ChatListener(this)
        ).forEach(listener -> this.getServer().getPluginManager().registerEvents(listener, this));
    }

    private void loadConfigs() {
        if (!this.schematicsFolder.exists()) {
            this.schematicsFolder.mkdir();
        }

        this.settingsConfig = new ConfigFile(this, "settings.yml");
        this.messagesConfig = new ConfigFile(this, "messages.yml");
        this.arenaConfig = new ConfigFile(this, "arenas.yml");
        this.scoreboardConfig = new ConfigFile(this, "scoreboard.yml");
        this.cosmeticsConfig = new ConfigFile(this, "cosmetics.yml");

        this.loadMessages();

        this.placeholderGlass = new ItemBuilder(Material.STAINED_GLASS_PANE).durability(15).name("&b").hideFlags().build();
        this.mainMenuItem = new ItemBuilder(Material.WATCH).name("&bMain Menu &7(Right Click)").hideFlags().build();
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

        CC.scoreboardBar = Messages.SCOREBOARD_BAR.toString();
        CC.chatBar = Messages.CHAT_BAR.toString();

        this.messagesConfig.save();
    }

    private void loadProviders() {
        this.boardManager = new BoardManager(this, new ScoreboardProvider(this), 2);
    }
}
