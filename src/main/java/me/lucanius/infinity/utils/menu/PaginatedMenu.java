package me.lucanius.infinity.utils.menu;

import lombok.Getter;
import me.lucanius.infinity.utils.menu.pagination.PageButton;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public abstract class PaginatedMenu extends Menu {

	@Getter private int page = 1;

	{
		this.setUpdateAfterClick(false);
	}

	@Override
	public String getTitle(Player player) {
		return this.getPrePaginatedTitle(player);
	}

	public final void modPage(Player player, int mod) {
		this.page += mod;
		this.getButtons().clear();
		this.openMenu(player);
	}

	public final int getPages(Player player) {
		int buttonAmount = this.getAllPagesButtons(player).size();

		if (buttonAmount == 0) {
			return 1;
		}

		return (int) Math.ceil(buttonAmount / (double) this.getMaxItemsPerPage(player));
	}

	@Override
	public final Map<Integer, Button> getButtons(Player player) {
		int maxItemsPage = this.getMaxItemsPerPage(player);

		int minIndex = (int) ((double) (this.page - 1) * maxItemsPage);
		int maxIndex = (int) ((double) (this.page) * maxItemsPage);

		HashMap<Integer, Button> buttons = new HashMap<>();

		buttons.put(0, new PageButton(-1, this));
		buttons.put(8, new PageButton(1, this));

		for (Map.Entry<Integer, Button> entry : this.getAllPagesButtons(player).entrySet()) {
			int ind = entry.getKey();
			if (ind >= minIndex && ind < maxIndex) {
				ind -= (int) ((double) (maxItemsPage) * (page - 1)) - 9;
				buttons.put(ind, entry.getValue());
			}
		}

		Map<Integer, Button> global = getGlobalButtons(player);
		if (global != null) {
			buttons.putAll(global);
		}

		return buttons;
	}

	public int getMaxItemsPerPage(Player player) {
		return 18;
	}

	public Map<Integer, Button> getGlobalButtons(Player player) {
		return null;
	}

	protected void bottomTopButtons(boolean full, Map<Integer, Button> buttons, ItemStack itemStack) {
		IntStream.range(0, getSize()).filter(slot -> buttons.get(slot) == null).forEach(slot -> {
			if (slot < 9 || slot > getSize() - 10 || full && (slot % 9 == 0 || (slot + 1) % 9 == 0)) {
				buttons.put(slot, new Button() {
					@Override
					public ItemStack getButtonItem(Player player) {
						return itemStack;
					}
				});
			}
		});
	}

	public abstract String getPrePaginatedTitle(Player player);

	public abstract Map<Integer, Button> getAllPagesButtons(Player player);
}
