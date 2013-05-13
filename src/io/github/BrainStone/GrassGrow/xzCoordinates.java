package io.github.BrainStone.GrassGrow;

import org.bukkit.Location;

public class xzCoordinates {
	public int x, z;

	public xzCoordinates(int x, int z) {
		this.x = x;
		this.z = z;
	}

	public xzCoordinates(Location loc) {
		x = loc.getBlockX();
		z = loc.getBlockZ();
	}

	@Override
	public boolean equals(Object par) {
		if (par instanceof xzCoordinates) {
			final xzCoordinates tmp = (xzCoordinates) par;

			return (x == tmp.x) && (z == tmp.z);
		} else if (par instanceof Location)
			return this.equals(new xzCoordinates((Location) par));

		return false;
	}
}
