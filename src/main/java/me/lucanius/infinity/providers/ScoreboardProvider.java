package me.lucanius.infinity.providers;

import me.lucanius.infinity.utils.scoreboard.AssembleAdapter;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Created by Lucanius
 * Project: Infinity
 * Created on 20/01/2022 at 18:42
 */
public class ScoreboardProvider implements AssembleAdapter {

    @Override
    public String getTitle(Player player) {
        return null;
    }

    @Override
    public List<String> getLines(Player player) {
        return null;
    }
}
