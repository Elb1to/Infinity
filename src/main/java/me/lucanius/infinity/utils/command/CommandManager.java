package me.lucanius.infinity.utils.command;

import me.lucanius.infinity.Infinity;
import me.lucanius.infinity.utils.CC;
import me.lucanius.infinity.utils.Messages;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.SimplePluginManager;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Command Framework - BukkitCommand
 * An implementation of Bukkit's Command class allowing for registering of commands without plugin.yml
 *
 * @author minnymin3
 */
public class CommandManager implements CommandExecutor {

	private final Infinity plugin;
	private final AllCommands allCommands;

	private final Map<String, Entry<Method, Object>> commandMap = new HashMap<>();
	private CommandMap map;

	public CommandManager(Infinity plugin) {
		this.plugin = plugin;
		this.allCommands = new AllCommands();

		if (plugin.getServer().getPluginManager() instanceof SimplePluginManager) {
			SimplePluginManager manager = (SimplePluginManager) plugin.getServer().getPluginManager();
			try {
				Field field = SimplePluginManager.class.getDeclaredField("commandMap");
				field.setAccessible(true);
				this.map = (CommandMap) field.get(manager);
			} catch (IllegalArgumentException | SecurityException | NoSuchFieldException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
		return handleCommand(sender, cmd, label, args);
	}

	public boolean handleCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
		for (int i = args.length; i >= 0; i--) {
			StringBuilder buffer = new StringBuilder();
			buffer.append(label.toLowerCase());
			for (int x = 0; x < i; x++) {
				buffer.append(".").append(args[x].toLowerCase());
			}

			String cmdLabel = buffer.toString();
			if (this.commandMap.containsKey(cmdLabel)) {
				Method method = this.commandMap.get(cmdLabel).getKey();
				Object methodObject = this.commandMap.get(cmdLabel).getValue();
				Command command = method.getAnnotation(Command.class);
				if (!command.permission().equals("") && (!sender.hasPermission(command.permission()))) {
					sender.sendMessage(Messages.NO_PERMISSION.toString());
					return true;
				}
				if (command.inGameOnly() && !(sender instanceof Player)) {
					sender.sendMessage(CC.translate("&cThis command can only be executed in game."));
					return true;
				}

				try {
					method.invoke(methodObject, new CommandArgs(sender, cmd, label, args, cmdLabel.split("\\.").length - 1));
				} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
					e.printStackTrace();
				}
				return true;
			}
		}

		defaultCommand(new CommandArgs(sender, cmd, label, args, 0));
		return true;
	}

	public void registerCommands(Object obj, List<String> aliases) {
		for (Method method : obj.getClass().getMethods()) {
			if (method.getAnnotation(Command.class) != null) {
				Command command = method.getAnnotation(Command.class);
				if (method.getParameterTypes().length > 1 || method.getParameterTypes()[0] != CommandArgs.class) {
					System.out.println("Unable to register command " + method.getName() + ". Unexpected method arguments");
					continue;
				}

				registerCommand(command, command.name(), method, obj);
				for (String alias : command.aliases()) {
					registerCommand(command, alias, method, obj);
				}
				if (aliases != null) {
					for (String alias : aliases) {
						registerCommand(command, alias, method, obj);
					}
				}
			} else if (method.getAnnotation(Completer.class) != null) {
				Completer comp = method.getAnnotation(Completer.class);
				if (method.getParameterTypes().length != 1 || method.getParameterTypes()[0] != CommandArgs.class) {
					System.out.println("Unable to register tab completer " + method.getName() + ". Unexpected method arguments");
					continue;
				}
				if (method.getReturnType() != List.class) {
					System.out.println("Unable to register tab completer " + method.getName() + ". Unexpected return type");
					continue;
				}

				registerCompleter(comp.name(), method, obj);
				for (String alias : comp.aliases()) {
					registerCompleter(alias, method, obj);
				}
			}
		}
	}

	public void registerCommand(Command command, String label, Method m, Object obj) {
		this.commandMap.put(label.toLowerCase(), new AbstractMap.SimpleEntry<>(m, obj));
		this.commandMap.put(this.plugin.getName() + ':' + label.toLowerCase(), new AbstractMap.SimpleEntry<>(m, obj));

		String cmdLabel = label.replace(".", ",").split(",")[0].toLowerCase();
		if (this.map.getCommand(cmdLabel) == null) {
			org.bukkit.command.Command cmd = new BukkitCommand(cmdLabel, this, this.plugin);
			this.map.register(this.plugin.getName(), cmd);
		}
		if (!command.description().equalsIgnoreCase("") && cmdLabel.equals(label)) {
			this.map.getCommand(cmdLabel).setDescription(command.description());
		}
		if (!command.usage().equalsIgnoreCase("") && cmdLabel.equals(label)) {
			this.map.getCommand(cmdLabel).setUsage(command.usage());
		}
	}

	public void registerCompleter(String label, Method m, Object obj) {
		String cmdLabel = label.replace(".", ",").split(",")[0].toLowerCase();
		if (this.map.getCommand(cmdLabel) == null) {
			org.bukkit.command.Command command = new BukkitCommand(cmdLabel, this, this.plugin);
			this.map.register(this.plugin.getName(), command);
		}
		if (this.map.getCommand(cmdLabel) instanceof BukkitCommand) {
			BukkitCommand command = (BukkitCommand) this.map.getCommand(cmdLabel);
			if (command.completer == null) {
				command.completer = new BukkitCompleter();
			}
			command.completer.addCompleter(label, m, obj);
		} else if (this.map.getCommand(cmdLabel) instanceof PluginCommand) {
			try {
				Object command = this.map.getCommand(cmdLabel);
				Field field = command.getClass().getDeclaredField("completer");
				field.setAccessible(true);
				if (field.get(command) == null) {
					BukkitCompleter completer = new BukkitCompleter();
					completer.addCompleter(label, m, obj);
					field.set(command, completer);
				} else if (field.get(command) instanceof BukkitCompleter) {
					BukkitCompleter completer = (BukkitCompleter) field.get(command);
					completer.addCompleter(label, m, obj);
				} else {
					System.out.println("Unable to register tab completer " + m.getName() + ". A tab completer is already registered for that command!");
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	private void defaultCommand(CommandArgs args) {
		args.getSender().sendMessage(args.getLabel() + " is not handled! Oh noes!");
	}

	public void loadCommands() throws InvocationTargetException, IllegalAccessException {
		for (Field field : this.allCommands.getClass().getDeclaredFields()) {
			if (InfinityCommand.class.isAssignableFrom(field.getType()) && field.getType().getSuperclass() == InfinityCommand.class) {
				field.setAccessible(true);
				try {
					Constructor<?> constructor = field.getType().getDeclaredConstructor();
					constructor.newInstance();
				} catch (ReflectiveOperationException exception) {
					exception.printStackTrace();
				}
			}
		}
	}
}
