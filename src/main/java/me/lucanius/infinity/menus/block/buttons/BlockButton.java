package me.lucanius.infinity.menus.block.buttons;

import lombok.AllArgsConstructor;
import me.lucanius.infinity.managers.cosmetics.type.PlayerBlock;
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
 * Created on 21/01/2022 at 21:28
 */
@AllArgsConstructor
public class BlockButton extends Button {

    private final PlayerData playerData;
    private final PlayerBlock block;

    @Override
    public ItemStack getButtonItem(Player player) {
        return this.block.getDisplayItem();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
        if (!this.block.getPermission().equalsIgnoreCase("") && !player.hasPermission(this.block.getPermission())) {
            player.sendMessage(CC.translate(Messages.NEED_RANK.toString()));
            playFail(player);
            return;
        }
        if (this.block == this.playerData.getBlockItem()) {
            player.sendMessage(CC.translate(Messages.ALREADY_USING.toString().replace("<object>", "Block")));
            playFail(player);
            return;
        }

        playNeutral(player);
        player.closeInventory();
        this.playerData.setBlockItem(this.block);
        this.getPlugin().getPlayerManager().resetInv(player, this.playerData);
    }
}
