package me.lucanius.infinity.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.apache.commons.codec.binary.Base64;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.MaterialData;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ItemBuilder {

	private final ItemStack is;

	public ItemBuilder(final Material mat) {
		this.is = new ItemStack(mat);
	}

	public ItemBuilder(final ItemStack is) {
		this.is = is;
	}

	public ItemBuilder(Material m, int amount) {
		this.is = new ItemStack(m, amount);
	}

	public ItemBuilder amount(final int amount) {
		this.is.setAmount(amount);

		return this;
	}

	public ItemBuilder name(final String name) {
		final ItemMeta meta = this.is.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
		this.is.setItemMeta(meta);

		return this;
	}

	public ItemBuilder lore(final String name) {
		final ItemMeta meta = this.is.getItemMeta();
		List<String> lore = meta.getLore();
		if (lore == null) {
			lore = new ArrayList<>();
		}

		lore.add(name);
		meta.setLore(lore);
		this.is.setItemMeta(meta);

		return this;
	}

	public ItemBuilder lore(final List<String> lore) {
		List<String> toSet = new ArrayList<>();
		ItemMeta meta = this.is.getItemMeta();

		for (String string : lore) {
			toSet.add(ChatColor.translateAlternateColorCodes('&', string));
		}

		meta.setLore(toSet);
		this.is.setItemMeta(meta);
		return this;
	}

	public ItemBuilder durability(final int durability) {
		this.is.setDurability((short) durability);

		return this;
	}

	public ItemBuilder head(String url) {
		SkullMeta headMeta = (SkullMeta) this.is.getItemMeta();
		GameProfile profile = new GameProfile(UUID.randomUUID(), null);
		byte[] encodedData = Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
		profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
		Field profileField;

		try {
			profileField = headMeta.getClass().getDeclaredField("profile");
			profileField.setAccessible(true);
			profileField.set(headMeta, profile);
		} catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e1) {
			e1.printStackTrace();
		}

		this.is.setItemMeta(headMeta);
		return this;
	}

	public ItemBuilder owner(String owner) {
		if (this.is.getType() == Material.SKULL_ITEM) {
			SkullMeta meta = (SkullMeta) this.is.getItemMeta();
			meta.setOwner(owner);
			this.is.setItemMeta(meta);
			return this;
		}

		throw new IllegalArgumentException("owner() only applicable for Skull Item");
	}

	@SuppressWarnings("deprecation")
	public ItemBuilder data(final int data) {
		this.is.setData(new MaterialData(this.is.getType(), (byte) data));

		return this;
	}

	public ItemBuilder enchantment(final Enchantment enchantment, final int level) {
		this.is.addUnsafeEnchantment(enchantment, level);
		return this;
	}

	public ItemBuilder enchantment(final Enchantment enchantment) {
		this.is.addUnsafeEnchantment(enchantment, 1);
		return this;
	}

	public ItemBuilder hideFlags() {
		final ItemMeta meta = this.is.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		this.is.setItemMeta(meta);

		return this;
	}

	public ItemBuilder hideEnchants() {
	    final ItemMeta meta = this.is.getItemMeta();
	    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		this.is.setItemMeta(meta);

	    return this;
    }

    public ItemBuilder hideUnbreakable() {
        final ItemMeta meta = this.is.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		this.is.setItemMeta(meta);

        return this;
    }

    public ItemBuilder addUnbreakable() {
        final ItemMeta meta = this.is.getItemMeta();
        meta.spigot().setUnbreakable(true);
		this.is.setItemMeta(meta);

        return this;
    }

	public ItemBuilder type(final Material material) {
		this.is.setType(material);
		return this;
	}

	public ItemBuilder clearLore() {
		final ItemMeta meta = this.is.getItemMeta();
		meta.setLore(new ArrayList<>());
		this.is.setItemMeta(meta);

		return this;
	}

	public ItemBuilder clearEnchantments() {
		for (final Enchantment e : this.is.getEnchantments().keySet()) {
			this.is.removeEnchantment(e);
		}

		return this;
	}

	public ItemBuilder color(Color color) {
		if (this.is.getType() == Material.LEATHER_BOOTS || this.is.getType() == Material.LEATHER_CHESTPLATE || this.is.getType() == Material.LEATHER_HELMET || this.is.getType() == Material.LEATHER_LEGGINGS) {
			LeatherArmorMeta meta = (LeatherArmorMeta) this.is.getItemMeta();
			meta.setColor(color);
			this.is.setItemMeta(meta);

			return this;
		} else {
			throw new IllegalArgumentException("color() only applicable for leather armor!");
		}
	}

	public ItemBuilder addEnchantments(Map<Enchantment, Integer> enchantments) {
		this.is.addEnchantments(enchantments);
		return this;
	}

	public ItemStack build() {
		return this.is;
	}
}
