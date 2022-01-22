package me.lucanius.infinity.listeners;

import lombok.AllArgsConstructor;
import me.lucanius.infinity.Infinity;
import me.lucanius.infinity.player.PlayerData;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

/**
 * Created by Lucanius
 * Project: Infinity
 * Created on 21/01/2022 at 19:03
 */
@AllArgsConstructor
public class WorldListener implements Listener {

    private final Infinity plugin;

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        PlayerData playerData = this.plugin.getPlayerManager().getPlayerData(player.getUniqueId());

        playerData.getPlacedBlocks().add(block);
        player.getItemInHand().setAmount(block.getType().getMaxStackSize());
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        PlayerData playerData = this.plugin.getPlayerManager().getPlayerData(player.getUniqueId());

        if (!playerData.getPlacedBlocks().contains(block)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockExplode(BlockExplodeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        event.setCancelled(true);
    }
}
