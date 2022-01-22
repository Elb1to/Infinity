package me.lucanius.infinity.managers.cosmetics.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Lucanius
 * Project: Infinity
 * Created on 21/01/2022 at 20:20
 */
@Getter
@Setter
@AllArgsConstructor
public class PlayerBlock {

    private final String name;
    private String displayName;
    private ItemStack useableItem;
    private ItemStack displayItem;
    private String permission;
}
