package me.lucanius.infinity.menus.difficulty.buttons;

import lombok.AllArgsConstructor;
import me.lucanius.infinity.managers.cosmetics.type.Difficulty;
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
 * Created on 21/01/2022 at 20:24
 */
@AllArgsConstructor
public class DifficultyButton extends Button {

    private final PlayerData playerData;
    private final Difficulty difficulty;

    @Override
    public ItemStack getButtonItem(Player player) {
        return this.difficulty.getDisplayItem();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
        if (!this.difficulty.getPermission().equalsIgnoreCase("") && !player.hasPermission(this.difficulty.getPermission())) {
            player.sendMessage(CC.translate(Messages.NEED_RANK.toString()));
            playFail(player);
            return;
        }
        if (this.difficulty == this.playerData.getDifficulty()) {
            player.sendMessage(CC.translate(Messages.ALREADY_USING.toString().replace("<object>", "Difficulty")));
            playFail(player);
            return;
        }

        playNeutral(player);
        player.closeInventory();
        this.playerData.setDifficulty(this.difficulty);
    }
}
