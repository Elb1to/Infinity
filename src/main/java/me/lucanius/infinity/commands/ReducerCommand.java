package me.lucanius.infinity.commands;

import me.lucanius.infinity.utils.CC;
import me.lucanius.infinity.utils.Messages;
import me.lucanius.infinity.utils.command.Command;
import me.lucanius.infinity.utils.command.CommandArgs;
import me.lucanius.infinity.utils.command.InfinityCommand;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.CommandSender;

/**
 * Created by Lucanius
 * Project: Infinity
 * Created on 20/01/2022 at 19:03
 */
public class ReducerCommand extends InfinityCommand {

    @Override @Command(name = "infinity", aliases = {"lucanius", "elb1to"}, inGameOnly = false)
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();

        sender.sendMessage(Messages.CHAT_BAR.toString());
        sender.sendMessage(CC.translate("&b&lInfinity&r &7- &a" + this.plugin.getDescription().getVersion()));
        sender.sendMessage(CC.translate("&7* &bAuthors: &f" + StringUtils.join(this.plugin.getDescription().getAuthors(), "&7, &f")));
        sender.sendMessage(CC.translate("&7* &fGitHub: &bhttps://github.com/FrozedClubDevelopment/Infinity"));
        sender.sendMessage(Messages.CHAT_BAR.toString());
    }
}
