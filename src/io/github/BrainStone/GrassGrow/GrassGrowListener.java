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
	private final GrassGrow PluginInstance;

	public GrassGrowListener(GrassGrow PluginInstance) {
		this.PluginInstance = PluginInstance;
	}

	private Location findGrass(Location location) {
		final Location tmp = location.clone();
		final World w = location.getWorld();
		boolean Up = true, Down = true;
		final int startHeight = location.getBlockY();

		if (w.getBlockAt(tmp).getTypeId() == 2)
			return tmp;

		for (int i = 1; Up || Down; i++) {
			if ((startHeight + i) > 255) {
				Up = false;
			} else {
				tmp.setY(startHeight + i);
				if (w.getBlockAt(tmp).getTypeId() == 2)
					return tmp;
			}

			if ((startHeight - i) < 0) {
				Down = false;
			} else {
				tmp.setY(startHeight - i);
				if (w.getBlockAt(tmp).getTypeId() == 2)
					return tmp;
			}
		}

		return null;
	}

	private void growGrass(Location location, Player player) {
		final GrowInformation info = map.get(player.getName());
		final int radius = info.radius;
		final int xPos = location.getBlockX();
		final int zPos = location.getBlockZ();
		final World w = location.getWorld();
		final Location tmp = new Location(w, 0, location.getBlockY(), 0);
		final Random rand = new Random();
		Location loc;

		for (int x = xPos - radius; x < (xPos + radius); x++) {
			tmp.setX(x);

			for (int z = zPos - radius; z < (zPos + radius); z++) {
				try {
					tmp.setZ(z);

					if (!info.Pos.contains(new xzCoordinates(tmp))
							&& ((loc = this.findGrass(tmp)) != null)) {
						info.addPos(loc);
						loc.setY(loc.getBlockY() + 1);

						final int id = w.getBlockAt(loc).getTypeId();

						if ((id == 0) || (id == 31) || (id == 37) || (id == 38)) {
							final double randDouble = rand.nextDouble();

							if (randDouble < .75) {
								w.getBlockAt(loc).setTypeIdAndData(31,
										(byte) 1, true);
							} else if (randDouble < .82) {
								w.getBlockAt(loc).setTypeId(37);
							} else if (randDouble < .85) {
								w.getBlockAt(loc).setTypeId(38);
							} else {
								w.getBlockAt(loc).setTypeId(0);
							}
						}
					}
				} catch (final NullPointerException e) {
					PluginInstance.warning("Block not available!", e);
				}
			}
		}

		map.put(player.getName(), info);
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		final Player p = event.getPlayer();

		if (map.containsKey(p.getName())
				&& !map.get(p.getName()).isOldPosition(p.getLocation())) {
			this.growGrass(p.getLocation(), p);
		}
	}
}