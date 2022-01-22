package me.lucanius.infinity.managers.knockback.util;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;

/**
 * Created by Lucanius
 * Project: Infinity
 * Created on 20/01/2022 at 23:05
 */
public class PlayEntityVelocity extends AbstractPacket {

    public static final PacketType TYPE = PacketType.Play.Server.ENTITY_VELOCITY;

    public PlayEntityVelocity(PacketContainer packet) {
        super(packet, TYPE);
    }

    public int getEntityId() {
        return this.packetContainer.getIntegers().read(0);
    }

    public void setVelocityX(double value) {
        this.packetContainer.getIntegers().write(1, (int) (value * 8000.0D));
    }

    public void setVelocityY(double value) {
        this.packetContainer.getIntegers().write(2, (int) (value * 8000.0D));
    }

    public void setVelocityZ(double value) {
        this.packetContainer.getIntegers().write(3, (int) (value * 8000.0D));
    }
}
