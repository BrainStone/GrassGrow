package io.github.BrainStone.GrassGrow;

import java.util.ArrayList;

import org.bukkit.Location;

public class GrowInformation {
	public ArrayList<xzCoordinates> Pos;
	public int radius;
	public Location OldPos;

	public GrowInformation(int radius) {
		this.radius = radius;
		Pos = new ArrayList<xzCoordinates>();
		OldPos = null;
	}

	public void addPos(Location loc) {
		Pos.add(new xzCoordinates(loc));
	}

	public boolean isOldPosition(Location loc) {
		final boolean output = (OldPos == null)
				|| ((loc.getBlockX() == OldPos.getBlockX()) && (loc.getBlockZ() == OldPos
						.getBlockZ()));

		OldPos = loc;

		return output;
	}
}