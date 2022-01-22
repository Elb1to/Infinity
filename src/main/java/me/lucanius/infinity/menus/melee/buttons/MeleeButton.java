package me.lucanius.infinity.menus.melee.buttons;

import lombok.AllArgsConstructor;
import me.lucanius.infinity.managers.cosmetics.type.PlayerMelee;
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
public class MeleeButton extends Button {

    private final PlayerData playerData;
    private final PlayerMelee melee;

    @Override
    public ItemStack getButtonItem(Player player) {
        return this.melee.getDisplayItem();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
        if (!this.melee.getPermission().equalsIgnoreCase("") && !player.hasPermission(this.melee.getPermission())) {
            player.sendMessage(CC.translate(Messages.NEED_RANK.toString()));
            playFail(player);
            return;
        }
        if (this.melee == this.playerData.getMeleeItem()) {
            player.sendMessage(CC.translate(Messages.ALREADY_USING.toString().replace("<object>", "Melee")));
            playFail(player);
            return;
        }

        playNeutral(player);
        player.closeInventory();
        this.playerData.setMeleeItem(this.melee);
        this.getPlugin().getPlayerManager().resetInv(player, this.playerData);
    }
}
