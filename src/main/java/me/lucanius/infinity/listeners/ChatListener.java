package me.lucanius.infinity.listeners;

import lombok.AllArgsConstructor;
import me.lucanius.infinity.Infinity;
import me.lucanius.infinity.player.PlayerData;
import me.lucanius.infinity.utils.CC;
import me.lucanius.infinity.utils.Messages;
import me.lucanius.infinity.utils.Utils;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.SkinTrait;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * Created by Lucanius
 * Project: Infinity
 * Created on 21/01/2022 at 19:45
 */
@AllArgsConstructor
public class ChatListener implements Listener {

    private final Infinity plugin;

    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = this.plugin.getPlayerManager().getPlayerData(player.getUniqueId());

        NPC npc = CitizensAPI.getNPCRegistry().getById(playerData.getNpcId());
        String message = event.getMessage();
        if (playerData.isChangingName()) {
            event.setCancelled(true);
            if (message.equalsIgnoreCase("cancel")) {
                player.sendMessage(Messages.CANCELLED.toString());
                playerData.setChangingName(false);
                playerData.setChangingSkin(false);
                return;
            }

            Utils.run(this.plugin, () -> npc.setName(CC.translate(message)));
            playerData.setNpcName(message);
            playerData.setChangingName(false);
            player.sendMessage(CC.translate(Messages.CHANGE_NAME_SUCCESSFULL.getString().replace("<name>", message)));
        }

        if (playerData.isChangingSkin()) {
            event.setCancelled(true);
            if (message.equalsIgnoreCase("cancel")) {
                player.sendMessage(Messages.CANCELLED.toString());
                playerData.setChangingName(false);
                playerData.setChangingSkin(false);
                return;
            }

            npc.getOrAddTrait(SkinTrait.class).setSkinName(message);
            playerData.setNpcSkin(message);
            playerData.setChangingSkin(false);
            player.sendMessage(CC.translate(Messages.CHANGE_SKIN_SUCCESSFULL.getString().replace("<skin>", message)));
        }
    }
}
