package me.lucanius.infinity.utils.menu.pagination;

import lombok.AllArgsConstructor;
import me.lucanius.infinity.utils.ItemBuilder;
import me.lucanius.infinity.utils.menu.Button;
import me.lucanius.infinity.utils.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

@AllArgsConstructor
public class BackButton extends Button {

	private final Menu back;

	@Override
	public ItemStack getButtonItem(Player player) {
		return new ItemBuilder(Material.BED)
				.name("&cGo Back")
				.lore(Arrays.asList(
						" ",
						"&7Click to go back.")
				)
				.hideFlags()
				.build();
	}

	@Override
	public void clicked(Player player, int i, ClickType clickType, int hb) {
		this.back.openMenu(player);
		playNeutral(player);
	}
}
