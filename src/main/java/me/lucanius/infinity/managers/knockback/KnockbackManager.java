package me.lucanius.infinity.managers.knockback;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import lombok.Getter;
import me.lucanius.infinity.Infinity;
import me.lucanius.infinity.managers.knockback.util.PlayEntityVelocity;
import me.lucanius.infinity.player.PlayerData;
import me.lucanius.infinity.utils.ManagerUtil;
import me.lucanius.infinity.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Lucanius
 * Project: Infinity
 * Created on 20/01/2022 at 23:10 (i'm tired xd)
 */
public class KnockbackManager extends ManagerUtil {

    @Getter
    private final Map<UUID, PlayerHit> playerHits = new ConcurrentHashMap<>();

    public KnockbackManager(Infinity plugin) {
        super(plugin);

        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(plugin, PacketType.Play.Server.ENTITY_VELOCITY) {
            @Override
            public void onPacketSending(PacketEvent event) {
                PlayEntityVelocity playEntityVelocity = new PlayEntityVelocity(event.getPacket());
                Player player = null;
                int id = playEntityVelocity.getEntityId();

                for (Player online : Utils.getOnlinePlayers()) {
                    if (online.getEntityId() == id) {
                        player = online;
                        break;
                    }
                }

                if (player == null) {
                    return;
                }

                if (!KnockbackManager.this.playerHits.containsKey(player.getUniqueId())) {
                    return;
                }

                PlayerHit playerHit = KnockbackManager.this.playerHits.get(player.getUniqueId());
                KnockbackManager.this.playerHits.remove(player.getUniqueId());

                if (playerHit == null) {
                    return;
                }

                Vector firstKb = playerHit.getDirection().setY(1);
                Vector attacker = playerHit.getAttacker().getLocation().getDirection().normalize().setY(1);

                PlayerData playerData = KnockbackManager.this.plugin.getPlayerManager().getPlayerData(player.getUniqueId());

                double horizontal = playerData.getDifficulty().getHorizontal();
                double vertical = playerData.getDifficulty().getVertical();

                Vector finalKb;
                if (playerHit.isSprinting()) {
                    finalKb = firstKb.clone().multiply(1.0D - 0.5D).add(attacker.clone().multiply(0.5));

                    finalKb.setX(finalKb.getX() * 2.0D);
                    finalKb.setY(finalKb.getY() * 2.0D);
                    finalKb.setZ(finalKb.getZ() * 2.0D);
                } else {
                    finalKb = firstKb;
                }

                finalKb = new Vector(finalKb.getX() * horizontal, finalKb.getY() * vertical, finalKb.getZ() * horizontal);
                if (playerHit.getKnockbackLevel() < 0) {
                    double enchantAdd = playerHit.getKnockbackLevel() * 0.45D;
                    double distance = Math.sqrt(Math.pow(finalKb.getX(), 2.0D)) + Math.pow(finalKb.getZ(), 2.0D);
                    double x = finalKb.getX() / distance;
                    double z = finalKb.getZ() / distance;

                    finalKb = new Vector((x * enchantAdd + finalKb.getX()), finalKb.getY(), (z * enchantAdd + finalKb.getZ()));
                }

                playEntityVelocity.setVelocityX(finalKb.getX());
                playEntityVelocity.setVelocityY(finalKb.getY());
                playEntityVelocity.setVelocityZ(finalKb.getZ());
            }
        });
    }
}
