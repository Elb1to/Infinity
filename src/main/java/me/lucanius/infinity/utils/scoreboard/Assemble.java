package me.lucanius.infinity.utils.scoreboard;

import lombok.Getter;
import lombok.Setter;
import me.lucanius.infinity.utils.Utils;
import me.lucanius.infinity.utils.scoreboard.events.AssembleBoardCreateEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Getter @Setter
public class Assemble {

	private final JavaPlugin plugin;

	private AssembleAdapter adapter;
	private AssembleThread thread;
	private AssembleStyle assembleStyle = AssembleStyle.MODERN;

	private Map<UUID, AssembleBoard> boards;

	private long ticks = 2;
	private boolean hook = false, debugMode = true, callEvents = true;

	private final ChatColor[] chatColorCache = ChatColor.values();

	public Assemble(JavaPlugin plugin, AssembleAdapter adapter) {
		if (plugin == null) {
			throw new RuntimeException("Assemble can not be instantiated without a plugin instance!");
		}

		this.plugin = plugin;
		this.adapter = adapter;
		this.boards = new ConcurrentHashMap<>();

		this.setup();
	}

	public void setup() {
		if (this.thread != null) {
			this.thread.stop();
			this.thread = null;
		}

		for (Player player : Utils.getOnlinePlayers()) {
			if (this.callEvents) {
				AssembleBoardCreateEvent createEvent = new AssembleBoardCreateEvent(player);

				Bukkit.getPluginManager().callEvent(createEvent);
				if (createEvent.isCancelled()) {
					continue;
				}
			}

			this.boards.putIfAbsent(player.getUniqueId(), new AssembleBoard(player, this));
		}

		this.thread = new AssembleThread(this);
	}

	public void cleanup() {
		if (this.thread != null) {
			this.thread.stop();
			this.thread = null;
		}

		for (UUID uuid : this.boards.keySet()) {
			Player player = Bukkit.getPlayer(uuid);

			if (player == null || !player.isOnline()) {
				continue;
			}

			this.boards.remove(uuid);
			player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
		}
	}
}
