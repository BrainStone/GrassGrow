package io.github.BrainStone.GrassGrow;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class GrasGrow extends JavaPlugin {
	private class GrowInformation {
		public ArrayList<Location> locations;
		public int radius;
	}

	private static final String VERSION = "0.1.2";
	private HashMap<Player, GrowInformation> map;
	private static int defaultRadius;

	@Override
	public void onEnable() {
		this.info("GrasGrow v" + VERSION + " starts enabling!");

		map = new HashMap<Player, GrowInformation>();
		loadConfig();

		this.info("GrasGrow v" + VERSION + " has been enabled!",
				"© by The_BrainStone, 2013", "Enjoy the unlimited Grass-Power!");
	}

	public void onPlayerMove(PlayerMoveEvent event) {
		Player p = event.getPlayer();

		if (map.containsKey(p)) {
			;
		}
	}

	private void loadConfig() {
		this.saveDefaultConfig();

		FileConfiguration config = getConfig();

		defaultRadius = config.getInt("defaultRadius", 25);

		saveConfig();
	}

	@Override
	public void onDisable() {
		this.info("GrasGrow starts disabling!");

		// TODO: Disabling code

		this.info("GrasGrow has been disabled!");
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if (label.equalsIgnoreCase("grasgrow") || label.equalsIgnoreCase("gg")
				|| label.equalsIgnoreCase("g_g")) {
			if (sender instanceof Player) {
				Player p = (Player) sender;
			} else {
				ConsoleCommandSender c = (ConsoleCommandSender) sender;
			}
		} else {
			return false;
		}

		return true;
	}

	private void info(String... msg) {
		for (final String str : msg) {
			this.getLogger().info(str);
		}
	}
}