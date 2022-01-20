package me.lucanius.infinity.utils.command;

import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Command Framework - BukkitCommand
 * An implementation of Bukkit's Command class allowing for registering of commands without plugin.yml
 *
 * @author minnymin3
 */
@Getter
public class CommandArgs {

	private final CommandSender sender;
	private final org.bukkit.command.Command command;
	private final String label;
	private final String[] args;

	protected CommandArgs(CommandSender sender, org.bukkit.command.Command command, String label, String[] args, int subCommand) {
		String[] modArgs = new String[args.length - subCommand];
		if (args.length - subCommand >= 0) {
			System.arraycopy(args, subCommand, modArgs, 0, args.length - subCommand);
		}

		StringBuilder buffer = new StringBuilder();
		buffer.append(label);
		for (int x = 0; x < subCommand; x++) {
			buffer.append(".").append(args[x]);
		}
		String cmdLabel = buffer.toString();

		this.sender = sender;
		this.command = command;
		this.label = cmdLabel;
		this.args = modArgs;
	}

	public String getArgs(int index) {
		return this.args[index];
	}

	public int length() {
		return this.args.length;
	}

	public boolean isPlayer() {
		return this.sender instanceof Player;
	}

	public Player getPlayer() {
		if (!(this.sender instanceof Player)) {
			return null;
		}

		return (Player) this.sender;
	}
}
