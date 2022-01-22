package me.lucanius.infinity.utils;

import me.lucanius.infinity.Infinity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * Created by Lucanius
 * Project: Infinity
 * Created on 20/01/2022 at 18:27
 */
public class Utils {

    public static List<Player> getOnlinePlayers() {
        return new ArrayList<>(Bukkit.getOnlinePlayers());
    }

    public static void log(Infinity plugin, String message) {
        plugin.getLogger().log(Level.INFO, message);
    }

    public static List<Block> getNearbyBlocks(Location location, int radius) {
        List<Block> blocks = new ArrayList<>();
        for (int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
            for (int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++) {
                for (int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
                    blocks.add(location.getWorld().getBlockAt(x, y, z));
                }
            }
        }

        return blocks;
    }

    public static void run(Infinity plugin, Callable callable) {
        plugin.getServer().getScheduler().runTask(plugin, callable::call);
    }

    public static void runAsync(Infinity plugin, Callable callable) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, callable::call);
    }

    public static void runLater(Infinity plugin, Callable callable, long delay) {
        plugin.getServer().getScheduler().runTaskLater(plugin, callable::call, delay);
    }

    public static void runLaterAsync(Infinity plugin, Callable callable, long delay) {
        plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, callable::call, delay);
    }

    public static void runTimer(Infinity plugin, Callable callable, long delay, long interval) {
        plugin.getServer().getScheduler().runTaskTimer(plugin, callable::call, delay, interval);
    }

    public static void runTimerAsync(Infinity plugin, Callable callable, long delay, long interval) {
        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, callable::call, delay, interval);
    }

    public interface Callable {
        void call();
    }
}
