package me.lucanius.infinity.utils.scoreboard;

import org.bukkit.entity.Player;

import java.util.List;

public interface AssembleAdapter {

	String getTitle(Player player);

	List<String> getLines(Player player);

}
