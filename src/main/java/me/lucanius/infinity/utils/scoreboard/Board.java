package me.lucanius.infinity.utils.scoreboard;

import lombok.Getter;
import me.lucanius.infinity.Infinity;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.*;

/**
 * Created by: ThatKawaiiSam
 * Project: Assemble
 */
@Getter
public class Board {

    private final List<BoardEntry> entries = new ArrayList<>();
    private final List<String> strings = new ArrayList<>();

    private Scoreboard scoreboard;
    private Objective objective;

    private final UUID uniqueId;
    private final BoardManager board;

    public Board(Player player, BoardManager board) {
        this.uniqueId = player.getUniqueId();
        this.board = board;
        this.setUp(player);
    }

    private static String getRandomColor() {
        Random random = Infinity.getInstance().getRandom();

        return colors().get(random.nextInt(colors().size() - 1)).toString();
    }

    private static List<ChatColor> colors() {
        List<ChatColor> chatColors = new ArrayList<>();

        Arrays.stream(ChatColor.values()).filter(ChatColor::isColor).forEach(chatColors::add);

        return chatColors;
    }

    public void setUp(Player player) {
        if (player.getScoreboard() != Bukkit.getScoreboardManager().getMainScoreboard()) {
            this.scoreboard = player.getScoreboard();
        } else {
            this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        }

        this.objective = this.scoreboard.registerNewObjective("Infinity", "dummy");
        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        this.objective.setDisplayName(this.board.getAdapter().getTitle(player));
        player.setScoreboard(this.scoreboard);
    }

    public String getUniqueString() {
        String string = getRandomColor();
        while (this.strings.contains(string)) {
            string = string + getRandomColor();
        }

        if (string.length() > 16) {
            return this.getUniqueString();
        }

        this.strings.add(string);
        return string;
    }

    public BoardEntry getEntryAtPosition(int position) {
        if (position >= this.entries.size()) {
            return null;
        }

        return this.entries.get(position);
    }
}

