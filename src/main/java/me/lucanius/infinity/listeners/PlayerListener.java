package me.lucanius.infinity.listeners;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.lucanius.infinity.Infinity;
import me.lucanius.infinity.utils.scoreboard.Assemble;
import me.lucanius.infinity.utils.scoreboard.AssembleBoard;
import me.lucanius.infinity.utils.scoreboard.events.AssembleBoardCreateEvent;
import me.lucanius.infinity.utils.scoreboard.events.AssembleBoardDestroyEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Created by Lucanius
 * Project: Infinity
 * Created on 20/01/2022 at 19:10
 */
@Getter
@AllArgsConstructor
public class PlayerListener implements Listener {

	private final Infinity plugin;
	private final Assemble assemble;

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (this.assemble.isCallEvents()) {
			AssembleBoardCreateEvent createEvent = new AssembleBoardCreateEvent(player);
			Bukkit.getPluginManager().callEvent(createEvent);
			if (createEvent.isCancelled()) {
				return;
			}
		}

		this.assemble.getBoards().put(player.getUniqueId(), new AssembleBoard(player, this.assemble));
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		if (this.assemble.isCallEvents()) {
			AssembleBoardDestroyEvent destroyEvent = new AssembleBoardDestroyEvent(player);
			Bukkit.getPluginManager().callEvent(destroyEvent);
			if (destroyEvent.isCancelled()) {
				return;
			}
		}

		this.assemble.getBoards().remove(player.getUniqueId());
		player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
	}
}
