package io.github.BrainStone.GrassGrow;

import java.util.ArrayList;

import org.bukkit.Location;

public class GrowInformation {
	public ArrayList<Location> locations;
	public int radius;

	public GrowInformation(int radius) {
		this.radius = radius;
	}
}