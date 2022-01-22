package me.lucanius.infinity.managers.knockback.util;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import lombok.Getter;

/**
 * Created by Lucanius
 * Project: Infinity
 * Created on 20/01/2022 at 23:02
 */
@Getter
public class AbstractPacket {

    protected final PacketContainer packetContainer;

    protected AbstractPacket(PacketContainer packetContainer, PacketType packetType) {
        if (packetContainer == null) {
            throw new IllegalArgumentException("packetContainer can't be null!");
        }
        if (packetContainer.getType() != packetType) {
            throw new IllegalArgumentException(packetContainer.getHandle() + " is not the same as " + packetType + "!");
        }

        this.packetContainer = packetContainer;
    }
}
