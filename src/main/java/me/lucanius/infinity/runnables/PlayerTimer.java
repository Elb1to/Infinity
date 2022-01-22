package me.lucanius.infinity.runnables;

import lombok.Getter;
import lombok.Setter;
import me.lucanius.infinity.Infinity;
import me.lucanius.infinity.player.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by Lucanius
 * Project: Infinity
 * Created on 21/01/2022 at 18:36
 */
@Getter
@Setter
public class PlayerTimer extends BukkitRunnable {

    private final Infinity plugin;
    private final PlayerData playerData;
    private final Player player;

    private double elapsedTime = 0.00D;

    public PlayerTimer(Infinity plugin, PlayerData playerData, Player player) {
        this.plugin = plugin;
        this.playerData = playerData;
        this.player = player;

        this.runTaskTimerAsynchronously(this.plugin, 0L, 1L);
    }

    @Override
    public void run() {
        if (this.playerData.isAttacking()) {
            this.elapsedTime += 0.05D;
        }

        if (this.player.getLocation().getY() <= 56) {
            this.playerData.resetPlayer(this.player);
        }
    }
}
