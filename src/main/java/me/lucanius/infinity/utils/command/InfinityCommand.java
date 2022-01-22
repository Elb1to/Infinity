package me.lucanius.infinity.utils.command;

import me.lucanius.infinity.Infinity;

/**
 * Created by Lucanius
 * Project: Infinity
 * Created on 20/01/2022 at 18:56
 */
public abstract class InfinityCommand {

	public final Infinity plugin;

	public InfinityCommand() {
		this.plugin = Infinity.getInstance();
		this.plugin.getCommandManager().registerCommands(this, null);
	}

	public abstract void onCommand(CommandArgs command);
}
