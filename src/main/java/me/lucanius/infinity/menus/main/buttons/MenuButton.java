package me.lucanius.infinity.menus.main.buttons;

import lombok.AllArgsConstructor;
import me.lucanius.infinity.utils.CC;
import me.lucanius.infinity.utils.Messages;
import me.lucanius.infinity.utils.menu.Button;
import me.lucanius.infinity.utils.menu.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Lucanius
 * Project: Infinity
 * Created on 21/01/2022 at 20:31
 */
@AllArgsConstructor
public class MenuButton extends Button {

    private final Menu menu;
    private final String permission;
    private final ItemStack itemStack;

    @Override
    public ItemStack getButtonItem(Player player) {
        return this.itemStack;
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
        if (!player.hasPermission(this.permission)) {
            player.sendMessage(CC.translate(Messages.NEED_RANK.toString()));
            playFail(player);
            return;
        }

        playNeutral(player);
        this.menu.openMenu(player);
    }
}
