package me.lucanius.infinity.menus.main.buttons;

import lombok.AllArgsConstructor;
import me.lucanius.infinity.player.PlayerData;
import me.lucanius.infinity.utils.CC;
import me.lucanius.infinity.utils.ItemBuilder;
import me.lucanius.infinity.utils.Messages;
import me.lucanius.infinity.utils.menu.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

/**
 * Created by Lucanius
 * Project: Infinity
 * Created on 21/01/2022 at 19:55
 */
@AllArgsConstructor
public class ChangeNameOrSkinButton extends Button {

    private final boolean name;
    private final boolean skin;
    private final PlayerData playerData;

    @Override
    public ItemStack getButtonItem(Player player) {
        if (this.name) {
            return new ItemBuilder(Material.NAME_TAG)
                    .name("&b&lChange Npc Name")
                    .lore(Arrays.asList(
                            "&7&oSupports color codes!",
                            " ",
                            "&fCurrent Name: &b" + this.playerData.getNpcName(),
                            " ",
                            "&aClick to change your npc name!")
                    )
                    .hideFlags()
                    .build();
        }
        if (this.skin) {
            return new ItemBuilder(Material.SKULL_ITEM)
                    .name("&b&lChange Npc Skin")
                    .lore(Arrays.asList(
                            " ",
                            "&fCurrent Skin: &b" + this.playerData.getNpcSkin(),
                            " ",
                            "&aClick to change your npc skin!")
                    )
                    .hideFlags()
                    .build();
        }
        return null;
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
        if (this.name && !player.hasPermission("infinity.changename")) {
            player.sendMessage(CC.translate(Messages.NEED_RANK.toString()));
            playFail(player);
            return;
        }

        if (this.skin && !player.hasPermission("infinity.changeskin")) {
            player.sendMessage(CC.translate(Messages.NEED_RANK.toString()));
            playFail(player);
            return;
        }

        playNeutral(player);
        player.closeInventory();
        if (this.name) {
            if (this.playerData.isChangingSkin()) {
                player.sendMessage(CC.translate("&cPlease finish setting your npc skin first or type &c&l'cancel' &cto cancel!"));
                return;
            }
            this.playerData.setChangingName(true);
            player.sendMessage(CC.translate("&aSet your npc name by typing it out in chat or type &a&l'cancel' &ato cancel!"));
        }
        if (this.skin) {
            if (this.playerData.isChangingName()) {
                player.sendMessage(CC.translate("&cPlease finish setting your npc name first or type &c&l'cancel' &cto cancel!"));
                return;
            }
            player.sendMessage(CC.translate("&aSet your npc skin by typing it out in chat or type &a&l'cancel' &ato cancel!"));
            this.playerData.setChangingSkin(true);
        }
    }
}
