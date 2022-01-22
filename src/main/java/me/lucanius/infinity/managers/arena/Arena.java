package me.lucanius.infinity.managers.arena;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Lucanius
 * Project: Infinity
 * Created on 20/01/2022 at 20:13
 */
@Getter
@Setter
@AllArgsConstructor
public class Arena {

    private final String name;
    private String schematicName;
    private Material npcSpawnBlock;
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;
    private boolean subtract;

    private int menuSlot;
    private String displayName;
    private ItemStack displayItem;
    private String permission;
}
