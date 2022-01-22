package me.lucanius.infinity.managers.cosmetics.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Lucanius
 * Project: Infinity
 * Created on 21/01/2022 at 16:57
 */
@Getter
@Setter
@AllArgsConstructor
public class Difficulty {

    private final String name;

    private String displayName;
    private ItemStack displayItem;
    private int menuSlot;
    private double horizontal;
    private double vertical;
    private String permission;
}
