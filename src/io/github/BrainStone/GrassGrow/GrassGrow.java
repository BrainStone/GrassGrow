package io.github.BrainStone.GrassGrow;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class GrassGrow extends JavaPlugin {
	private static final String VERSION = "1.8.0";
	private static int defaultRadius;

	public void info(Object... msg) {
		for (final Object tmp : msg) {
			this.info(tmp.toString());
		}
	}

	public void info(String... msg) {
		for (final String str : msg) {
			this.getLogger().info(str);
		}
	}

	private void loadConfig() {
		this.saveDefaultConfig();

		final FileConfiguration config = this.getConfig();

		defaultRadius = config.getInt("defaultRadius", 25);

		this.saveConfig();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if (label.equalsIgnoreCase("grasgrow") || label.equalsIgnoreCase("gg")
				|| label.equalsIgnoreCase("g_g")) {
			if (sender instanceof Player) {
				if ((args.length != 1) && (args.length != 2)) {
					sender.sendMessage("Wrong number of arguments!");

					return false;
				}

				final Player p = (Player) sender;
				final String action = args[0];

				if (action.equalsIgnoreCase("enable")
						|| action.equalsIgnoreCase("en")) {
					int radius;

					if (args.length == 1) {
						radius = defaultRadius;
					} else {
						radius = Integer.valueOf(args[1]);

						if (radius > 50) {
							p.sendMessage("$5Radius is too big (max. 50)!");
						}
					}

					GrassGrowListener.map.put(p.getName(), new GrowInformation(
							radius));

					p.sendMessage("§5Feel the flower power! (Radius: "
							+ String.valueOf(radius) + ")");
				} else if (action.equalsIgnoreCase("disable")
						|| action.equalsIgnoreCase("dis")) {
					if (args.length == 2) {
						sender.sendMessage("Wrong number of arguments!");

						return false;
					}

					GrassGrowListener.map.remove(p.getName());

					p.sendMessage("§5No more flowers! :(");
				} else {
					p.sendMessage("§7Please use the mode §5\"enable\"§7 or §5\"disable\"");
				}
			} else {
				if ((args.length != 2) && (args.length != 3)) {
					sender.sendMessage("Wrong number of arguments!");

					return false;
				}

				final Player target = Bukkit.getServer().getPlayer(args[1]);

				if (target == null) {
					sender.sendMessage(args[0] + " is not online!");

					return false;
				}

				final String action = args[0];

				if (action.equalsIgnoreCase("enable")
						|| action.equalsIgnoreCase("en")) {
					int radius;

					if (args.length == 2) {
						radius = defaultRadius;
					} else {
						radius = Integer.valueOf(args[2]);

						if (radius > 50) {
							sender.sendMessage("$5Radius is too big (max. 50)!");
						}
					}

					GrassGrowListener.map.put(target.getName(),
							new GrowInformation(radius));

					sender.sendMessage("§5Feel the flower power! (Radius: "
							+ String.valueOf(radius) + ")");
				} else if (action.equalsIgnoreCase("disable")
						|| action.equalsIgnoreCase("dis")) {
					if (args.length == 3) {
						sender.sendMessage("Wrong number of arguments!");

						return false;
					}

					GrassGrowListener.map.remove(target.getName());

					sender.sendMessage("§5No more flowers! :(");
				} else {
					sender.sendMessage("§7Please use the mode \"§5enable§7\" or \"§5disable§7\"");
				}
			}
		} else
			return false;

		return true;
	}

	@Override
	public void onDisable() {
		this.info("GrasGrow starts disabling!");

		GrassGrowListener.map = null;

		this.info("GrasGrow has been disabled!");
	}

	@Override
	public void onEnable() {
		this.info("GrasGrow v" + VERSION + " starts enabling!");

		GrassGrowListener.map = new HashMap<String, GrowInformation>();
		this.loadConfig();
		this.getServer().getPluginManager()
				.registerEvents(new GrassGrowListener(this), this);

		this.info("GrassGrow v" + VERSION + " has been enabled!",
				"© by The_BrainStone, 2013", "Enjoy the unlimited Flower Power");
	}

	public void warning(Object... msg) {
		for (final Object tmp : msg) {
			this.warning(tmp.toString());
		}
	}

	public void warning(String... msg) {
		for (final String str : msg) {
			this.getLogger().warning(str);
		}
	}
}