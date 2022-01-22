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
public class PageButton extends Button {

	private final int mod;
	private final PaginatedMenu menu;

	@Override
	public ItemStack getButtonItem(Player player) {
		return new ItemBuilder(Material.CARPET)
				.name(this.hasNext(player) ? (this.mod > 0 ? "&aNext Page" : "&cPrevious Page") : (this.mod > 0 ? "&6Last Page" : "&6First Page"))
				.durability(this.hasNext(player) ? (this.mod > 0 ? 5 : 14) : 8)
				.lore(Arrays.asList(
						" ",
						"&7Right Click to",
						"&7switch to a page.")
				)
				.hideFlags()
				.build();
	}

	@Override
	public void clicked(Player player, int i, ClickType clickType, int hb) {
		if (clickType == ClickType.RIGHT) {
			new ViewAllPagesMenu(this.menu).openMenu(player);
			playNeutral(player);
		} else {
			if (hasNext(player)) {
				this.menu.modPage(player, this.mod);
				playNeutral(player);
			} else {
				playFail(player);
			}
		}
	}

	private boolean hasNext(Player player) {
		int pg = this.menu.getPage() + this.mod;
		return pg > 0 && this.menu.getPages(player) >= pg;
	}
}
