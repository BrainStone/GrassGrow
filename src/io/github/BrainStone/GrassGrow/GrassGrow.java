package io.github.BrainStone.GrassGrow;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class GrassGrow extends JavaPlugin {
	private static final String VERSION = "1.2.1";
	private static int defaultRadius;

	private void info(String... msg) {
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
					}

					GrassGrowListener.map.put(p.getName(), new GrowInformation(
							radius));
				} else if (action.equalsIgnoreCase("disable")
						|| action.equalsIgnoreCase("dis")) {
					if (args.length == 2) {
						sender.sendMessage("Wrong number of arguments!");

						return false;
					}

					GrassGrowListener.map.remove(p);
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
					}

					GrassGrowListener.map.put(target.getName(),
							new GrowInformation(radius));
				} else if (action.equalsIgnoreCase("disable")
						|| action.equalsIgnoreCase("dis")) {
					if (args.length == 3) {
						sender.sendMessage("Wrong number of arguments!");

						return false;
					}

					GrassGrowListener.map.remove(target);
				}
			}
		} else
			return false;

		return true;
	}

	@Override
	public void onDisable() {
		this.info("GrasGrow starts disabling!");

		// TODO: Disabling code

		this.info("GrasGrow has been disabled!");
	}

	@Override
	public void onEnable() {
		this.info("GrasGrow v" + VERSION + " starts enabling!");

		GrassGrowListener.map = new HashMap<String, GrowInformation>();
		this.loadConfig();
		getServer().getPluginManager().registerEvents(new GrassGrowListener(), this);

		this.info("GrasGrow v" + VERSION + " has been enabled!",
				"© by The_BrainStone, 2013", "Enjoy the unlimited Grass-Power!");
	}
}