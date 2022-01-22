package me.lucanius.infinity.utils;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lucanius
 * Project: Infinity
 * Created on 20/01/2022 at 18:15
 */
public class CC {

    public static String scoreboardBar = ChatColor.translateAlternateColorCodes('&', "&7&m-*------------------*-");
    public static String chatBar = ChatColor.translateAlternateColorCodes('&', "&7&m-&3&m*&7&m----------------------------------------&3&m*&7&m-");

    public static String translate(String message) {
        return ChatColor.translateAlternateColorCodes('&', message)
                .replace("<chat-bar>", chatBar)
                .replace("<scoreboard-bar>", scoreboardBar);
    }

    public static List<String> translate(List<String> lines) {
        List<String> strings = new ArrayList<>();
        for (String line : lines) {
            strings.add(ChatColor.translateAlternateColorCodes('&', line)
                    .replace("<chat-bar>", chatBar)
                    .replace("<scoreboard-bar>", scoreboardBar)
            );
        }

        return strings;
    }

    public static List<String> translate(String[] lines) {
        List<String> strings = new ArrayList<>();
        for (String line : lines) {
            if (line != null) {
                strings.add(ChatColor.translateAlternateColorCodes('&', line)
                        .replace("<chat-bar>", chatBar)
                        .replace("<scoreboard-bar>", scoreboardBar)
                );
            }
        }

        return strings;
    }
}
