package me.lucanius.infinity.menus.block;

import me.lucanius.infinity.menus.block.buttons.BlockButton;
import me.lucanius.infinity.menus.main.MainMenu;
import me.lucanius.infinity.player.PlayerData;
import me.lucanius.infinity.utils.menu.Button;
import me.lucanius.infinity.utils.menu.PaginatedMenu;
import me.lucanius.infinity.utils.menu.pagination.BackButton;
import me.lucanius.infinity.utils.menu.pagination.PageButton;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lucanius
 * Project: Infinity
 * Created on 21/01/2022 at 21:27
 */
public class BlockMenu extends PaginatedMenu {

    @Override
    public String getPrePaginatedTitle(Player player) {
        return "&eSelect Block";
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        PlayerData playerData = this.getPlugin().getPlayerManager().getPlayerData(player.getUniqueId());

        this.getPlugin().getCosmeticsManager().getBlocks().forEach(block -> buttons.put(buttons.size(), new BlockButton(playerData, block)));

        return buttons;
    }

    @Override
    public Map<Integer, Button> getGlobalButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(0, new PageButton(-1, this));
        buttons.put(8, new PageButton(1, this));

        buttons.put(36, new BackButton(new MainMenu()));

        bottomTopButtons(false, buttons, this.getPlugin().getPlaceholderGlass());

        return buttons;
    }

    @Override
    public int getSize() {
        return 9 * 5;
    }

    @Override
    public int getMaxItemsPerPage(Player player) {
        return 9 * 3;
    }
}
