package me.lucanius.infinity.runnables;

import lombok.AllArgsConstructor;
import me.lucanius.infinity.Infinity;
import me.lucanius.infinity.player.PlayerData;
import me.lucanius.infinity.utils.Utils;
import net.citizensnpcs.api.ai.Navigator;
import net.citizensnpcs.api.ai.goals.TargetNearbyEntityGoal;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collections;

/**
 * Created by Lucanius
 * Project: Infinity
 * Created on 20/01/2022 at 21:21
 */
@AllArgsConstructor
public class NpcMechanicsRunnable extends BukkitRunnable {

    private final Infinity plugin;
    private final Player player;
    private final PlayerData playerData;
    private final NPC npc;

    @Override
    public void run() {
        if (!this.player.isOnline() || this.playerData == null || this.npc == null) {
            this.cancel();
            return;
        }

        if (!this.playerData.isAttacking()) {
            if (this.npc.getDefaultGoalController().isPaused()) {
                this.npc.getDefaultGoalController().setPaused(false);
                TargetNearbyEntityGoal targetNearbyEntityGoal = TargetNearbyEntityGoal.builder(this.npc).aggressive(true).radius(10.0D).targets(Collections.singleton(EntityType.PLAYER)).build();
                this.npc.getDefaultGoalController().addGoal(targetNearbyEntityGoal, 1);
                Navigator navigator = this.npc.getNavigator();
                navigator.getLocalParameters().baseSpeed(0.0F).attackDelayTicks(10).range(8.0F).attackRange(20.0D).stuckAction(((npc1, navigator1) -> false));

                Utils.runLater(this.plugin, () -> {
                    this.npc.getDefaultGoalController().clear();
                    this.npc.getDefaultGoalController().setPaused(true);
                    navigator.getLocalParameters().baseSpeed(0.0F).attackDelayTicks(10).range(8.0F).attackRange(8.0D).stuckAction(((npc1, navigator1) -> false));
                }, 2L);
            }
        }
    }
}
