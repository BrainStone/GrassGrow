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

public class GrasGrow extends JavaPlugin {
	private class GrowInformation {
		public ArrayList<Location> locations;
		public int radius;

		public GrowInformation(int radius) {
			this.radius = radius;
		}
	}

	private static final String VERSION = "1.0.1";
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

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Player p = event.getPlayer();

		if (map.containsKey(p)) {
			growGrass(p.getLocation(), p);
			
		}
	}

	private void growGrass(Location location, Player player) {
		GrowInformation info = map.get(player);
		int radius = info.radius;
		int xPos = location.getBlockX();
		int zPos = location.getBlockZ();
		World w = location.getWorld();
		Location tmp = new Location(w, 0, location.getBlockY(), 0);
		Random rand = new Random();
		
		for(int x = xPos - radius; x < xPos + radius; x++) {
			tmp.setX(x);
			
			for(int z = zPos - radius; z < zPos + radius; z++) {
				tmp.setZ(z);
				
				Location loc = findGrass(tmp);
				
				if(loc != null && !info.locations.contains(loc)) {
					info.locations.add(loc);
					
					loc.setY(loc.getBlockY() + 1);
					
					if(w.getBlockAt(loc).getTypeId() == 0) {
						double randDouble = rand.nextDouble();
						
						if(randDouble < .75) {
							w.getBlockAt(loc).setTypeIdAndData(31, (byte) 1, true);
						} else if(randDouble < .82) {
							w.getBlockAt(loc).setTypeId(37);
						} else if(randDouble < .85) {
							w.getBlockAt(loc).setTypeId(38);
						}
					}
				}
			}
		}
		
		map.put(player, info);
	}
	
	private Location findGrass(Location location) {
		Location tmp = location.clone();
		World w = location.getWorld();
		int heightUp = -1;
		int heightDown = -1;
		int startHeight = location.getBlockY();
		
		if(w.getBlockAt(tmp).getTypeId() == 2) {
			return tmp;
		}
		
		for(int y = startHeight; y < 255; y++) {
			tmp.setY(y);
			
			if(w.getBlockAt(tmp).getTypeId() == 2) {
				heightUp = y;
				
				break;
			}
		}
		
		for(int y = startHeight; y >= 0; y--) {
			tmp.setY(y);
			
			if(w.getBlockAt(tmp).getTypeId() == 2) {
				heightDown = y;
				
				break;
			}
		}
		
		if(heightUp == -1 && heightDown == -1)
			return null;
		
		if(heightUp == -1) {
			tmp.setY(heightDown);
		} else if(heightDown == -1) {
			tmp.setY(heightUp);
		} else {
			if((heightUp - startHeight) < -(heightDown - startHeight)) {
				tmp.setY(heightUp);
			} else {
				tmp.setY(heightDown);
			}
		}
		
		return tmp;
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
				if (args.length != 1 && args.length != 2) {
					sender.sendMessage("Wrong number of arguments!");

					return false;
				}

				Player p = (Player) sender;
				String action = args[0];

				if (action.equalsIgnoreCase("enable")) {
					int radius;

					if (args.length == 1)
						radius = defaultRadius;
					else
						radius = Integer.valueOf(args[1]);

					map.put(p, new GrowInformation(radius));
				} else if (action.equalsIgnoreCase("disable")) {
					if (args.length == 2) {
						sender.sendMessage("Wrong number of arguments!");

						return false;
					}

					map.remove(p);
				}
			} else {
				if (args.length != 2 && args.length != 3) {
					sender.sendMessage("Wrong number of arguments!");

					return false;
				}

				Player target = Bukkit.getServer().getPlayer(args[0]);

				if (target == null) {
					sender.sendMessage(args[0] + " is not online!");

					return false;
				}
				
				String action = args[1];

				if (action.equalsIgnoreCase("enable")) {
					int radius;

					if (args.length == 2)
						radius = defaultRadius;
					else
						radius = Integer.valueOf(args[2]);

					map.put(target, new GrowInformation(radius));
				} else if (action.equalsIgnoreCase("disable")) {
					if (args.length == 3) {
						sender.sendMessage("Wrong number of arguments!");

						return false;
					}

					map.remove(target);
				}
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