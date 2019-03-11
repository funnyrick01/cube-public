package com.palagear.thecube.util;

import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

public enum Side {
	Up, 
	Down, 
	North, 
	East, 
	South, 
	West;
	
	public Vector toVector() {
		
		Vector result = null;
		
		switch(this) {
			case Up:
				result = new Vector(0, 1, 0);
				break;
			case Down:
				result = new Vector(0, -1, 0);
				break;
			case North:
				result = new Vector(0, 0, -1);
				break;
			case East:
				result = new Vector(1, 0, 0);
				break;
			case South:
				result = new Vector(0, 0, 1);
				break;
			case West:
				result = new Vector(-1, 0, 0);
				break;
		}
		
		return result;
	}
	
	public BlockFace toBlockFace() {
		
		BlockFace result = null;
		
		switch(this) {
			case Up:
				result = BlockFace.UP;
				break;
			case Down:
				result = BlockFace.DOWN;
				break;
			case North:
				result = BlockFace.NORTH;
				break;
			case East:
				result = BlockFace.EAST;
				break;
			case South:
				result = BlockFace.SOUTH;
				break;
			case West:
				result = BlockFace.WEST;
				break;
		}
		
		return result;
	}
}
