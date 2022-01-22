package me.lucanius.infinity.utils.menu.pagination;

import lombok.AllArgsConstructor;
import me.lucanius.infinity.utils.ItemBuilder;
import me.lucanius.infinity.utils.menu.Button;
import me.lucanius.infinity.utils.menu.PaginatedMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

@AllArgsConstructor
public class JumpToPageButton extends Button {

	private final int page;
	private final PaginatedMenu menu;
	private final boolean current;

	@Override
	public ItemStack getButtonItem(Player player) {
		return new ItemBuilder(Material.INK_SACK)
				.name("&fPage: &b" + this.page)
				.durability(this.current ? 10 : 8)
				.lore(Arrays.asList(
						" ",
						(this.current ? "&7Current page" : "&7Other page"))
				)
				.hideFlags()
				.build();
	}

	@Override
	public void clicked(Player player, int i, ClickType clickType, int hb) {
		this.menu.modPage(player, this.page - this.menu.getPage());
		playNeutral(player);
	}
}
