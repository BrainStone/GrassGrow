package io.github.BrainStone.GrassGrow;

import java.util.HashMap;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class GrassGrowListener implements Listener {
	public static HashMap<String, GrowInformation> map;

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

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		final Player p = event.getPlayer();
		p.sendMessage("1");

		if (map.containsKey(p.getName())) {
			p.sendMessage("2");

			this.growGrass(p.getLocation(), p);
		}
	}
}
