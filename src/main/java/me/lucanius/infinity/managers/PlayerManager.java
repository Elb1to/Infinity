package me.lucanius.infinity.managers;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import me.lucanius.infinity.Infinity;
import me.lucanius.infinity.player.PlayerData;
import me.lucanius.infinity.utils.ManagerUtil;
import me.lucanius.infinity.utils.Utils;
import net.citizensnpcs.api.CitizensAPI;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Lucanius
 * Project: Infinity
 * Created on 20/01/2022 at 19:46
 */
public class PlayerManager extends ManagerUtil {

    private final Map<UUID, PlayerData> players = new ConcurrentHashMap<>();

    public PlayerManager(Infinity plugin) {
        super(plugin);
    }

    public PlayerData getAndCreatePlayerData(UUID uniqueId) {
        PlayerData playerData = new PlayerData(uniqueId);
        this.players.put(uniqueId, playerData);

        return playerData;
    }

    public PlayerData getPlayerData(UUID uniqueId) {
        return this.players.get(uniqueId);
    }

    public Collection<PlayerData> getAllPlayerData() {
        return this.players.values();
    }

    public void removePlayerData(UUID uniqueId) {
        PlayerData playerData = this.getPlayerData(uniqueId);
        if (playerData.getEditSession() != null) {
            playerData.getEditSession().undo(playerData.getEditSession());
        }
        CitizensAPI.getNPCRegistry().deregister(CitizensAPI.getNPCRegistry().getById(playerData.getNpcId()));
        playerData.clearPlacedBlocks();
        playerData.stopTimer();

        this.plugin.getKnockbackManager().getPlayerHits().remove(uniqueId);

        Utils.runAsync(this.plugin, () -> {
            if (playerData.isLoaded()) {
                this.savePlayerData(playerData);
            }

            this.players.remove(uniqueId);
        });
    }

    public void loadPlayerData(PlayerData playerData) {
        Document document = this.plugin.getMongoManager().getPlayers().find(Filters.eq("uniqueId", playerData.getUniqueId().toString())).first();

        if (document != null) {
            playerData.setNpcName(document.getString("npcName"));
            playerData.setNpcSkin(document.getString("npcSkin"));
            playerData.setMeleeItem(this.plugin.getCosmeticsManager().getMeleeByName(document.getString("meleeItem")));
            playerData.setBlockItem(this.plugin.getCosmeticsManager().getBlockByName(document.getString("blockItem")));
            playerData.setHighestTime(document.getDouble("highestTime"));
            playerData.setArena(this.plugin.getArenaManager().getByName(document.getString("currentArena")));
            playerData.setDifficulty(this.plugin.getCosmeticsManager().getDifficultyByName(document.getString("currentDifficulty")));
        }

        playerData.setLoaded(true);
    }

    public void savePlayerData(PlayerData playerData) {
        Document document = new Document();

        document.put("uniqueId", playerData.getUniqueId().toString());
        document.put("userName", Bukkit.getOfflinePlayer(playerData.getUniqueId()).getName());

        document.put("npcName", playerData.getNpcName());
        document.put("npcSkin", playerData.getNpcSkin());
        document.put("meleeItem", playerData.getMeleeItem().getName());
        document.put("blockItem", playerData.getBlockItem().getName());
        document.put("highestTime", playerData.getHighestTime());
        document.put("currentArena", playerData.getArena().getName());
        document.put("currentDifficulty", playerData.getDifficulty().getName());

        this.plugin.getMongoManager().getPlayers().replaceOne(Filters.eq("uniqueId", playerData.getUniqueId().toString()), document, new UpdateOptions().upsert(true));
    }

    public void resetInv(Player player, PlayerData playerData) {
        player.setGameMode(GameMode.SURVIVAL);

        player.getInventory().setItem(0, playerData.getMeleeItem().getUseableItem());
        player.getInventory().setItem(1, playerData.getBlockItem().getUseableItem());
        player.getInventory().setItem(8, this.plugin.getMainMenuItem());
    }
}
