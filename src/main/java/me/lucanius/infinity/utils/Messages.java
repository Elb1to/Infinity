package me.lucanius.infinity.utils;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Lucanius
 * Project: Infinity
 * Created on 20/01/2022 at 18:15
 */
@Getter
public enum Messages {

    NO_PERMISSION("NO-PERMS", "&cNo permission.", null),
    SCOREBOARD_BAR("SCOREBOARD-BAR", "&7&m-&3&m*&7&m------------------&3&m*&7&m-", null),
    CHAT_BAR("CHAT-BAR", "&7&m-&3&m*&7&m----------------------------------------&3&m*&7&m-", null);

    @Setter private static ConfigFile configFile;
    private final String path;
    private final String string;
    private final List<String> stringList;

    Messages(String path, String string, List<String> stringList) {
        this.path = path;
        this.string = string;
        if (stringList != null) {
            this.stringList = stringList;
        } else {
            this.stringList = new ArrayList<>(Collections.singletonList(string));
        }
    }

    public String toString() {
        return configFile.getString(this.path);
    }

    public List<String> toList() {
        return configFile.getStringList(this.path);
    }
}
