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

    public static String translate(String message) {
        return ChatColor.translateAlternateColorCodes('&', message)
                .replace("<chat-bar>", Messages.CHAT_BAR.toString())
                .replace("<scoreboard-bar>", Messages.SCOREBOARD_BAR.toString());
    }

    public static List<String> translate(List<String> lines) {
        List<String> strings = new ArrayList<>();
        for (String line : lines) {
            strings.add(ChatColor.translateAlternateColorCodes('&', line)
                    .replace("<chat-bar>", Messages.CHAT_BAR.toString())
                    .replace("<scoreboard-bar>", Messages.SCOREBOARD_BAR.toString())
            );
        }

        return strings;
    }

    public static List<String> translate(String[] lines) {
        List<String> strings = new ArrayList<>();
        for (String line : lines) {
            if (line != null) {
                strings.add(ChatColor.translateAlternateColorCodes('&', line)
                        .replace("<chat-bar>", Messages.CHAT_BAR.toString())
                        .replace("<scoreboard-bar>", Messages.SCOREBOARD_BAR.toString())
                );
            }
        }

        return strings;
    }
}
