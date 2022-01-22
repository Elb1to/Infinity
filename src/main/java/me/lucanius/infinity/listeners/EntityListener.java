package me.lucanius.infinity.listeners;

import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import me.lucanius.infinity.Infinity;
import me.lucanius.infinity.player.PlayerData;
import me.lucanius.infinity.managers.knockback.PlayerHit;
import me.lucanius.infinity.utils.NmsUtils;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.ai.Navigator;
import net.citizensnpcs.api.ai.goals.TargetNearbyEntityGoal;
import net.citizensnpcs.api.event.NPCDamageByEntityEvent;
import net.citizensnpcs.api.npc.NPC;
import net.minecraft.server.v1_8_R3.PacketPlayOutAnimation;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.util.Vector;

/**
 * Created by Lucanius
 * Project: Infinity
 * Created on 21/01/2022 at 18:20
 */
@AllArgsConstructor
public class EntityListener implements Listener {

    private final Infinity plugin;

    @EventHandler
    public void onNPCDamageByEntity(NPCDamageByEntityEvent event) {
        NPC npc = event.getNPC();
        Entity entity = npc.getEntity();

        event.setCancelled(true);
        LivingEntity livingEntity = (LivingEntity) entity;
        PacketPlayOutAnimation animation = new PacketPlayOutAnimation();

        NmsUtils.setValue(animation, "a", livingEntity.getEntityId());
        NmsUtils.setValue(animation, "b", 1);

        Player player = (Player) event.getDamager();
        NmsUtils.sendPacket(animation, player);

        PlayerData playerData = this.plugin.getPlayerManager().getPlayerData(player.getUniqueId());
        if (!playerData.isAttacking()) {
            playerData.setAttacking(true);

            Navigator navigator = npc.getNavigator();
            navigator.getLocalParameters().baseSpeed(0.0F).attackDelayTicks(10).range(8.0F).attackRange(8.0D).stuckAction((npc1, navigator1) -> false);

            npc.getDefaultGoalController().clear();
            npc.getDefaultGoalController().setPaused(false);

            TargetNearbyEntityGoal nearbyGoal = TargetNearbyEntityGoal.builder(npc).aggressive(true).radius(10.0D).targets(Sets.newHashSet(EntityType.PLAYER)).build();
            npc.getDefaultGoalController().addGoal(nearbyGoal, 1);
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player)) {
            return;
        }
        if (CitizensAPI.getNPCRegistry().isNPC(event.getEntity())) {
            return;
        }
        event.setDamage(0.0D);

        Player attacker = (Player) event.getDamager();
        Player victim = (Player) event.getEntity();

        Vector vector = new Vector(victim.getLocation().getX() - attacker.getLocation().getX(), 0.0D, victim.getLocation().getZ() - attacker.getLocation().getZ());
        int kbLevel = attacker.getItemInHand().getEnchantmentLevel(Enchantment.KNOCKBACK);

        PlayerData playerData = this.plugin.getPlayerManager().getPlayerData(victim.getUniqueId());
        playerData.setCurrentHits(playerData.getCurrentHits() + 1);

        PlayerHit playerHit = new PlayerHit(vector.normalize(), attacker.isSprinting(), kbLevel, attacker);
        this.plugin.getKnockbackManager().getPlayerHits().put(victim.getUniqueId(), playerHit);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            event.setCancelled(true);
        }
    }
}
