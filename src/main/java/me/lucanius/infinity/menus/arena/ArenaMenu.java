package me.lucanius.infinity.menus.arena;

import me.lucanius.infinity.menus.arena.buttons.ArenaButton;
import me.lucanius.infinity.menus.main.MainMenu;
import me.lucanius.infinity.player.PlayerData;
import me.lucanius.infinity.utils.menu.Button;
import me.lucanius.infinity.utils.menu.Menu;
import me.lucanius.infinity.utils.menu.pagination.BackButton;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lucanius
 * Project: Infinity
 * Created on 21/01/2022 at 20:32
 */
public class ArenaMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return "&eSelect Arena";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        PlayerData playerData = this.getPlugin().getPlayerManager().getPlayerData(player.getUniqueId());

        this.getPlugin().getArenaManager().getArenas().forEach(arena -> buttons.put(arena.getMenuSlot(), new ArenaButton(playerData, arena)));

        buttons.put(18, new BackButton(new MainMenu()));

        this.fillEmptySlots(buttons, this.getPlugin().getPlaceholderGlass());

        return buttons;
    }

    @Override
    public int getSize() {
        return 3 * 9;
    }
}
