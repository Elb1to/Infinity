package me.lucanius.infinity.utils;

import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

/**
 * Created by Lucanius
 * Project: Infinity
 * Created on 21/01/2022 at 18:26
 */
public class NmsUtils {

    public static void setValue(Object object, String name, Object value) {
        try {
            Field field = object.getClass().getDeclaredField(name);
            field.setAccessible(true);
            field.set(object, value);
        } catch (Exception ignored) {

        }
    }

    public static void sendPacket(Packet<?> packet, Player player) {
        (((CraftPlayer) player).getHandle()).playerConnection.sendPacket(packet);
    }
}
