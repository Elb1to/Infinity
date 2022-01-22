package me.lucanius.infinity.managers.arena;

import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.schematic.SchematicFormat;
import lombok.Getter;
import me.lucanius.infinity.Infinity;
import me.lucanius.infinity.player.PlayerData;
import me.lucanius.infinity.utils.*;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.LookClose;
import net.citizensnpcs.trait.SkinTrait;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lucanius
 * Project: Infinity
 * Created on 20/01/2022 at 20:13
 */
public class ArenaManager extends ManagerUtil {

    @Getter private final List<Arena> arenas = new ArrayList<>();
    private final ConfigFile config = this.plugin.getArenaConfig();

    public ArenaManager(Infinity plugin) {
        super(plugin);
        this.loadArenas();
    }

    private void loadArenas() {
        this.config.getConfigurationSection("ARENAS").getKeys(false).forEach(string -> {
            String root = "ARENAS." + string + ".";

            Arena arena = new Arena(
                    string,
                    this.config.getString(root + "SCHEMATIC"),
                    Material.valueOf(this.config.getString(root + "NPC-SPAWN-BLOCK")),
                    this.config.getDouble(root + "OFFSET.X"),
                    this.config.getDouble(root + "OFFSET.Y"),
                    this.config.getDouble(root + "OFFSET.Z"),
                    this.config.getFloat(root + "OFFSET.YAW"),
                    this.config.getFloat(root + "OFFSET.PITCH"),
                    this.config.getBoolean(root + "OFFSET.SUBTRACT"),
                    this.config.getInt(root + "MENU-SLOT"),
                    this.config.getString(root + "DISPLAY-NAME"),
                    ItemUtil.createItem(
                            Material.valueOf(this.config.getString(root + "DISPLAY-ITEM.MATERIAL")),
                            this.config.getInt(root + "DISPLAY-ITEM.DATA"),
                            this.config.getString(root + "DISPLAY-ITEM.NAME"),
                            this.config.getStringList(root + "DISPLAY-ITEM.LORE")),
                    this.config.getString("PERMISSION")
            );

            this.arenas.add(arena);
        });
    }

    public Arena getByName(String name) {
        return this.arenas.stream().filter(arena -> arena.getName().equalsIgnoreCase(name)).findFirst().orElse(this.arenas.get(0));
    }

    @SuppressWarnings("deprecation")
    public void loadArena(Player player, PlayerData playerData, Arena arena) {
        this.plugin.getPlayerManager().resetInv(player, playerData);

        EditSession playerSession = playerData.getEditSession();
        if (playerSession != null) {
            playerSession.undo(playerSession);
        }

        this.plugin.getCurrentLocation().add(100, 0, 0);

        EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(new BukkitWorld(this.plugin.getCurrentLocation().getWorld()), (WorldEdit.getInstance().getConfiguration()).maxChangeLimit);
        playerData.setEditSession(editSession);
        Vector vector = new Vector(this.plugin.getCurrentLocation().getBlockX(), this.plugin.getCurrentLocation().getBlockY(), this.plugin.getCurrentLocation().getBlockZ());
        File file = new File(this.plugin.getSchematicsFolder(), arena.getSchematicName());
        SchematicFormat schematicFormat = SchematicFormat.getFormat(file);

        try {
            CuboidClipboard cuboidClipboard = schematicFormat.load(file);
            cuboidClipboard.paste(editSession, vector, true);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        List<Block> nearbyBlocks = Utils.getNearbyBlocks(this.plugin.getCurrentLocation(), 25);
        Block finalBlock = null;
        for (Block block : nearbyBlocks) {
            if (block.getType() == arena.getNpcSpawnBlock()) {
                finalBlock = block;
            }
        }
        if (finalBlock == null) {
            player.sendMessage(CC.translate("&cContact an administrator immediately!"));
            return;
        }

        Location location = finalBlock.getLocation();
        // Getting normalized location
        location.add(0.5, 1, 0.5);
        this.spawnNpc(playerData, location);

        Location playerLocation;
        if (arena.isSubtract()) {
            playerLocation = location.subtract(arena.getX(), arena.getY(), arena.getZ());
        } else {
            playerLocation = location.add(arena.getX(), arena.getY(), arena.getZ());
        }
        playerLocation.setYaw(arena.getYaw());
        playerLocation.setPitch(arena.getPitch());

        playerData.setTeleportLocation(playerLocation);
        playerData.setArena(arena);

        player.teleport(playerLocation);
    }

    public void spawnNpc(PlayerData playerData, Location location) {
        NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, CC.translate(playerData.getNpcName()));
        npc.spawn(location);
        npc.data().setPersistent("collidable", false);
        npc.getOrAddTrait(SkinTrait.class).setSkinName(playerData.getNpcSkin());
        npc.getOrAddTrait(LookClose.class).lookClose(true);
        npc.setProtected(false);

        LivingEntity entity = (LivingEntity) npc.getEntity();
        if (entity != null && entity.getEquipment() != null) {
            entity.getEquipment().setItemInHand(new ItemBuilder(Material.STICK).enchantment(Enchantment.KNOCKBACK, 3).build());
        }

        playerData.setNpcId(npc.getId());
    }
}
