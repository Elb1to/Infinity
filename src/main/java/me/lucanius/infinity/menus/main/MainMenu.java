package me.lucanius.infinity.menus.main;

import me.lucanius.infinity.menus.arena.ArenaMenu;
import me.lucanius.infinity.menus.block.BlockMenu;
import me.lucanius.infinity.menus.difficulty.DifficultyMenu;
import me.lucanius.infinity.menus.main.buttons.ChangeNameOrSkinButton;
import me.lucanius.infinity.menus.main.buttons.MenuButton;
import me.lucanius.infinity.menus.melee.MeleeMenu;
import me.lucanius.infinity.player.PlayerData;
import me.lucanius.infinity.utils.ItemBuilder;
import me.lucanius.infinity.utils.menu.Button;
import me.lucanius.infinity.utils.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lucanius
 * Project: Infinity
 * Created on 21/01/2022 at 19:54
 */
public class MainMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return "&eMain Menu";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        PlayerData playerData = this.getPlugin().getPlayerManager().getPlayerData(player.getUniqueId());

        buttons.put(11, new ChangeNameOrSkinButton(true, false, playerData));
        buttons.put(13, new ChangeNameOrSkinButton(false, true, playerData));
        buttons.put(15, new MenuButton(new BlockMenu(), "infinity.changeblock",
                new ItemBuilder(Material.COBBLESTONE)
                        .name("&b&lChange Block")
                        .lore(Arrays.asList(
                                " ",
                                "&fCurrent Block: &b" + playerData.getBlockItem().getDisplayName(),
                                " ",
                                "&aClick to change your block!")
                        ).hideFlags().build()));
        buttons.put(29, new MenuButton(new ArenaMenu(), "infinity.changearena",
                new ItemBuilder(Material.GRASS)
                        .name("&b&lChange Arena")
                        .lore(Arrays.asList(
                                " ",
                                "&fCurrent Arena: &b" + playerData.getArena().getDisplayName(),
                                " ",
                                "&aClick to change your arena!")
                        ).hideFlags().build()));
        buttons.put(31, new MenuButton(new MeleeMenu(), "infinity.changemelee",
                new ItemBuilder(Material.STICK)
                        .name("&b&lChange Melee")
                        .lore(Arrays.asList(
                                " ",
                                "&fCurrent Melee: &b" + playerData.getMeleeItem().getDisplayName(),
                                " ",
                                "&aClick to change your melee!")
                        ).hideFlags().build()));
        buttons.put(33, new MenuButton(new DifficultyMenu(), "infinity.changedifficulty",
                new ItemBuilder(Material.DIAMOND_SWORD)
                        .name("&b&lChange Difficulty")
                        .lore(Arrays.asList(
                                " ",
                                "&fCurrent Difficulty: &b" + playerData.getDifficulty().getDisplayName(),
                                " ",
                                "&aClick to change your difficulty!")
                        ).hideFlags().build()));

        this.fillEmptySlots(buttons, this.getPlugin().getPlaceholderGlass());

        return buttons;
    }

    @Override
    public int getSize() {
        return 5 * 9;
    }
}
