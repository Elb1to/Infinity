package me.lucanius.infinity.listeners;

import lombok.AllArgsConstructor;
import me.lucanius.infinity.Infinity;
import me.lucanius.infinity.menus.main.MainMenu;
import me.lucanius.infinity.player.PlayerData;
import me.lucanius.infinity.runnables.NpcMechanicsRunnable;
import me.lucanius.infinity.utils.CC;
import me.lucanius.infinity.utils.Utils;
import me.lucanius.infinity.utils.menu.Button;
import me.lucanius.infinity.utils.menu.Menu;
import me.lucanius.infinity.utils.scoreboard.Board;
import me.lucanius.infinity.utils.scoreboard.BoardManager;
import net.citizensnpcs.api.CitizensAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

/**
 * Created by Lucanius
 * Project: Infinity
 * Created on 20/01/2022 at 19:10
 */
@AllArgsConstructor
public class PlayerListener implements Listener {

	private final Infinity plugin;
	private final BoardManager board;

	@EventHandler
	public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
		UUID uniqueId = event.getUniqueId();
		Player player = Bukkit.getPlayer(uniqueId);
		if (player != null) {
			event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, CC.translate("&cPlease wait before re-logging!"));
			Utils.run(this.plugin, () -> player.kickPlayer(ChatColor.RED + "Duplicated login!"));
			return;
		}

		PlayerData playerData = this.plugin.getPlayerManager().getAndCreatePlayerData(uniqueId);
		Utils.runAsync(this.plugin, () -> this.plugin.getPlayerManager().loadPlayerData(playerData));
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		this.board.getBoardMap().put(event.getPlayer().getUniqueId(), new Board(player, this.board));

		PlayerData playerData = this.plugin.getPlayerManager().getPlayerData(player.getUniqueId());
		if (playerData == null) {
			player.sendMessage(CC.translate("&cContact an administrator immediately!"));
			return;
		}
		if (!playerData.isLoaded()) {
			Utils.runAsync(this.plugin, () -> this.plugin.getPlayerManager().loadPlayerData(playerData));
		}

		// Make sure the player has everyting loaded correctly before spawning.
		Utils.runLater(this.plugin, () -> {
			this.plugin.getArenaManager().loadArena(player, playerData, playerData.getArena());
			new NpcMechanicsRunnable(this.plugin, player, playerData, CitizensAPI.getNPCRegistry().getById(playerData.getNpcId())).runTaskTimer(this.plugin, 0L, 60L);
			playerData.startTimer();
		}, 5L);
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		this.board.getBoardMap().remove(player.getUniqueId());

		this.plugin.getPlayerManager().removePlayerData(player.getUniqueId());
	}

	@EventHandler
	public void onPlayerKick(PlayerKickEvent event) {
		Player player = event.getPlayer();
		this.board.getBoardMap().remove(player.getUniqueId());

		this.plugin.getPlayerManager().removePlayerData(player.getUniqueId());
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		ItemStack item = player.getItemInHand();
		if (item == null) {
			return;
		}

		if (item.isSimilar(this.plugin.getMainMenuItem())) {
			new MainMenu().openMenu(player);
		}
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		if (event.getCurrentItem() != null && event.getClickedInventory() == player.getInventory()) {
			event.setCancelled(true);
			return;
		}
		String name = player.getName();
		Menu openMenu = Menu.currentlyOpenedMenus.get(name);
		if (openMenu != null) {
			int slot = event.getSlot();
			ClickType click = event.getClick();
			if (slot != event.getRawSlot()) {
				if ((click == ClickType.SHIFT_LEFT || click == ClickType.SHIFT_RIGHT)) {
					event.setCancelled(true);
				}
				return;
			}

			if (openMenu.getButtons().containsKey(slot)) {
				Button button = openMenu.getButtons().get(slot);
				boolean cancel = button.shouldCancel(player, slot, click);
				if (!cancel && (click == ClickType.SHIFT_LEFT || click == ClickType.SHIFT_RIGHT)) {
					event.setCancelled(true);
					if (event.getCurrentItem() != null) {
						player.getInventory().addItem(event.getCurrentItem());
					}
				} else {
					event.setCancelled(cancel);
				}

				button.clicked(player, slot, click, event.getHotbarButton());

				if (Menu.currentlyOpenedMenus.containsKey(name)) {
					Menu newMenu = Menu.currentlyOpenedMenus.get(name);
					if (newMenu == openMenu && openMenu.isUpdateAfterClick()) {
						openMenu.setClosedByMenu(true);
						newMenu.openMenu(player);
					}
				} else if (button.shouldUpdate(player, slot, click)) {
					openMenu.setClosedByMenu(true);
					openMenu.openMenu(player);
				}
				if (event.isCancelled()) {
					this.plugin.getServer().getScheduler().runTaskLater(this.plugin, player::updateInventory, 1L);
				}
			} else {
				InventoryAction action = event.getAction();
				if ((click == ClickType.SHIFT_LEFT || click == ClickType.SHIFT_RIGHT || action == InventoryAction.MOVE_TO_OTHER_INVENTORY || action == InventoryAction.HOTBAR_MOVE_AND_READD || action == InventoryAction.HOTBAR_SWAP)) {
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		Player player = (Player) event.getPlayer();
		String name = player.getName();
		Menu openMenu = Menu.currentlyOpenedMenus.get(name);
		if (openMenu != null) {
			openMenu.onClose(player);
			Menu.currentlyOpenedMenus.remove(name);
		}
	}

	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void onFoodLevelChange(FoodLevelChangeEvent event) {
		event.setCancelled(true);
	}
}
