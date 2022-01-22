package me.lucanius.infinity.menus.arena.buttons;

import lombok.AllArgsConstructor;
import me.lucanius.infinity.managers.arena.Arena;
import me.lucanius.infinity.player.PlayerData;
import me.lucanius.infinity.utils.CC;
import me.lucanius.infinity.utils.Messages;
import me.lucanius.infinity.utils.menu.Button;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Lucanius
 * Project: Infinity
 * Created on 21/01/2022 at 20:33
 */
@AllArgsConstructor
public class ArenaButton extends Button {

    private final PlayerData playerData;
    private final Arena arena;

    @Override
    public ItemStack getButtonItem(Player player) {
        return this.arena.getDisplayItem();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
        if (!this.arena.getPermission().equalsIgnoreCase("") && !player.hasPermission(this.arena.getPermission())) {
            player.sendMessage(CC.translate(Messages.NEED_RANK.toString()));
            playFail(player);
            return;
        }
        if (this.arena == this.playerData.getArena()) {
            player.sendMessage(CC.translate(Messages.ALREADY_USING.toString().replace("<object>", "Arena")));
            playFail(player);
            return;
        }

        playNeutral(player);
        player.closeInventory();
        this.playerData.setNewArena(player, this.arena);
    }
}
