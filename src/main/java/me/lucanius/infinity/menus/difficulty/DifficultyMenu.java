package me.lucanius.infinity.menus.difficulty;

import me.lucanius.infinity.menus.difficulty.buttons.DifficultyButton;
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
 * Created on 21/01/2022 at 20:24
 */
public class DifficultyMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return "&eSelect Difficulty";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        PlayerData playerData = this.getPlugin().getPlayerManager().getPlayerData(player.getUniqueId());

        this.getPlugin().getCosmeticsManager().getDifficulties().forEach(difficulty -> buttons.put(difficulty.getMenuSlot(), new DifficultyButton(playerData, difficulty)));

        buttons.put(18, new BackButton(new MainMenu()));

        this.fillEmptySlots(buttons, this.getPlugin().getPlaceholderGlass());

        return buttons;
    }

    @Override
    public int getSize() {
        return 3 * 9;
    }
}
