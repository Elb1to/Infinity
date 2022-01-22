package me.lucanius.infinity.providers;

import me.lucanius.infinity.Infinity;
import me.lucanius.infinity.player.PlayerData;
import me.lucanius.infinity.utils.CC;
import me.lucanius.infinity.utils.scoreboard.BoardAdapter;
import me.lucanius.infinity.utils.scoreboard.BoardStyle;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lucanius
 * Project: Infinity
 * Created on 20/01/2022 at 18:42
 */
public class ScoreboardProvider implements BoardAdapter {

    private final Infinity plugin;

    public ScoreboardProvider(Infinity plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getTitle(Player player) {
        return this.plugin.getScoreboardConfig().getString("TITLE");
    }

    @Override
    public List<String> getLines(Player player) {
        PlayerData playerData = this.plugin.getPlayerManager().getPlayerData(player.getUniqueId());

        return this.getPlayingLines(playerData);
    }

    @Override
    public BoardStyle getBoardStyle(Player player) {
        return BoardStyle.MODERN;
    }

    private List<String> getPlayingLines(PlayerData playerData) {
        List<String> lines = new ArrayList<>();
        DecimalFormat decimalFormat = new DecimalFormat("0.00");

        for (String string : this.plugin.getScoreboardConfig().getStringList("LINES")) {
            lines.add(string
                    .replace("<block-count>", String.valueOf(playerData.getPlacedBlocks().size()))
                    .replace("<current-time>", String.valueOf(decimalFormat.format(playerData.getElapsedTime())))
                    .replace("<highest-time>", String.valueOf(decimalFormat.format(playerData.getHighestTime())))
                    .replace("<difficulty>", playerData.getDifficulty().getDisplayName())
                    .replace("<current-hits>", String.valueOf(playerData.getCurrentHits()))
            );
        }

        return CC.translate(lines);
    }
}
