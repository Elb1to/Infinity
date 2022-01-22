package me.lucanius.infinity.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/**
 * Created by Lucanius
 * Project: Infinity
 * Created on 20/01/2022 at 20:53
 */
public class ItemUtil {

    public static ItemStack createItem(Material material, int durability, String name, List<String> lore) {
        ItemStack itemStack = new ItemStack(material);
        itemStack.setDurability((short) durability);

        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(CC.translate(name));
        itemMeta.setLore(CC.translate(lore));
        itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }
}
