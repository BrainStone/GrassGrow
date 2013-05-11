package io.github.BrainStone.GrassGrow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class GrassGrow extends JavaPlugin {
	private class GrowInformation {
		public ArrayList<Location> locations;
		public int radius;

		public GrowInformation(int radius) {
			this.radius = radius;
		}
	}

	private static final String VERSION = "1.1.2";
	private HashMap<String, GrowInformation> map;
	private static int defaultRadius;

	private Location findGrass(Location location) {
		final Location tmp = location.clone();
		final World w = location.getWorld();
		int heightUp = -1;
		int heightDown = -1;
		final int startHeight = location.getBlockY();

		if (w.getBlockAt(tmp).getTypeId() == 2)
			return tmp;

		for (int y = startHeight; y < 255; y++) {
			tmp.setY(y);

			if (w.getBlockAt(tmp).getTypeId() == 2) {
				heightUp = y;

				break;
			}
		}

		for (int y = startHeight; y >= 0; y--) {
			tmp.setY(y);

			if (w.getBlockAt(tmp).getTypeId() == 2) {
				heightDown = y;

				break;
			}
		}

		if ((heightUp == -1) && (heightDown == -1))
			return null;

		if (heightUp == -1) {
			tmp.setY(heightDown);
		} else if (heightDown == -1) {
			tmp.setY(heightUp);
		} else {
			if ((heightUp - startHeight) < -(heightDown - startHeight)) {
				tmp.setY(heightUp);
			} else {
				tmp.setY(heightDown);
			}
		}

		return tmp;
	}

	private void growGrass(Location location, Player player) {
		final GrowInformation info = map.get(player);
		final int radius = info.radius;
		final int xPos = location.getBlockX();
		final int zPos = location.getBlockZ();
		final World w = location.getWorld();
		final Location tmp = new Location(w, 0, location.getBlockY(), 0);
		final Random rand = new Random();

		for (int x = xPos - radius; x < (xPos + radius); x++) {
			tmp.setX(x);

			for (int z = zPos - radius; z < (zPos + radius); z++) {
				tmp.setZ(z);

				final Location loc = this.findGrass(tmp);

				if ((loc != null) && !info.locations.contains(loc)) {
					info.locations.add(loc);

					loc.setY(loc.getBlockY() + 1);

					if (w.getBlockAt(loc).getTypeId() == 0) {
						final double randDouble = rand.nextDouble();

						if (randDouble < .75) {
							w.getBlockAt(loc).setTypeIdAndData(31, (byte) 1,
									true);
						} else if (randDouble < .82) {
							w.getBlockAt(loc).setTypeId(37);
						} else if (randDouble < .85) {
							w.getBlockAt(loc).setTypeId(38);
						}
					}
				}
			}
		}

		map.put(player.getName(), info);
	}

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

					map.put(p.getName(), new GrowInformation(radius));
				} else if (action.equalsIgnoreCase("disable")
						|| action.equalsIgnoreCase("dis")) {
					if (args.length == 2) {
						sender.sendMessage("Wrong number of arguments!");

						return false;
					}

					map.remove(p);
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

					map.put(target.getName(), new GrowInformation(radius));
				} else if (action.equalsIgnoreCase("disable")
						|| action.equalsIgnoreCase("dis")) {
					if (args.length == 3) {
						sender.sendMessage("Wrong number of arguments!");

						return false;
					}

					map.remove(target);
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

		map = new HashMap<String, GrowInformation>();
		this.loadConfig();

		this.info("GrasGrow v" + VERSION + " has been enabled!",
				"© by The_BrainStone, 2013", "Enjoy the unlimited Grass-Power!");
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		final Player p = event.getPlayer();

		if (map.containsKey(p)) {
			this.growGrass(p.getLocation(), p);

		}
	}
}