package me.lucanius.infinity.player;

import com.sk89q.worldedit.EditSession;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.lucanius.infinity.Infinity;
import me.lucanius.infinity.managers.arena.Arena;
import me.lucanius.infinity.managers.cosmetics.type.PlayerBlock;
import me.lucanius.infinity.managers.cosmetics.type.Difficulty;
import me.lucanius.infinity.managers.cosmetics.type.PlayerMelee;
import me.lucanius.infinity.runnables.NpcMechanicsRunnable;
import me.lucanius.infinity.runnables.PlayerTimer;
import me.lucanius.infinity.utils.Utils;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Lucanius
 * Project: Infinity
 * Created on 20/01/2022 at 19:18
 */
@Getter
@Setter
@RequiredArgsConstructor
public class PlayerData {

    private final Infinity plugin = Infinity.getInstance();
    private final UUID uniqueId;

    private ArrayList<Block> placedBlocks = new ArrayList<>();

    private boolean loaded;
    private boolean changingName = false;
    private boolean changingSkin = false;

    private String npcName = "&b&lInfinity";
    private String npcSkin = "Lucanius";
    private PlayerMelee meleeItem = this.plugin.getCosmeticsManager().getMelees().get(0);
    private PlayerBlock blockItem = this.plugin.getCosmeticsManager().getBlocks().get(0);

    private PlayerTimer timer;
    private boolean attacking;

    private int currentHits;
    private double highestTime = 0.00D;

    private int npcId;

    private Arena arena = this.plugin.getArenaManager().getArenas().get(0);
    private Difficulty difficulty = this.plugin.getCosmeticsManager().getDifficulties().get(0);

    private Location teleportLocation;
    private EditSession editSession;

    public void clearPlacedBlocks() {
        this.placedBlocks.forEach(block -> block.getLocation().getBlock().setType(Material.AIR));
        this.placedBlocks.clear();
    }

    public void startTimer() {
        if (this.timer == null) {
            this.timer = new PlayerTimer(this.plugin, this, Bukkit.getPlayer(this.uniqueId));
        }
    }

    public void stopTimer() {
        if (this.timer != null) {
            this.timer.cancel();
            this.timer = null;
        }
    }

    public void resetTimer() {
        if (this.timer != null) {
            this.timer.setElapsedTime(0.00);
        }
    }

    public double getElapsedTime() {
        if (this.timer != null) {
            return this.timer.getElapsedTime();
        }

        return 0.00D;
    }

    public void resetPlayer(Player player) {
        player.teleport(this.teleportLocation);

        NPC npc = CitizensAPI.getNPCRegistry().getById(this.getNpcId());
        npc.getNavigator().getLocalParameters().baseSpeed(0.0F).attackDelayTicks(10).range(8.0F).attackRange(8.0D).stuckAction((npc1, navigator) -> false);
        npc.getDefaultGoalController().clear();
        npc.getDefaultGoalController().setPaused(false);

        if (this.getElapsedTime() > this.highestTime) {
            this.highestTime = this.getElapsedTime();
        }

        Utils.run(this.plugin, this::clearPlacedBlocks);

        this.attacking = false;
        this.plugin.getPlayerManager().resetInv(player, this);
        this.currentHits = 0;
        this.resetTimer();
    }

    public void setNewArena(Player player, Arena newArena) {
        this.arena = newArena;
        if (this.editSession != null) {
            this.editSession.undo(this.editSession);
        }
        CitizensAPI.getNPCRegistry().deregister(CitizensAPI.getNPCRegistry().getById(this.npcId));
        this.clearPlacedBlocks();
        this.plugin.getKnockbackManager().getPlayerHits().remove(this.uniqueId);
        this.plugin.getArenaManager().loadArena(player, this, newArena);

        new NpcMechanicsRunnable(this.plugin, player, this, CitizensAPI.getNPCRegistry().getById(this.npcId)).runTaskTimer(this.plugin, 0L, 60L);
    }
}
