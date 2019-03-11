package com.palagear.thecube.tasks;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

import com.palagear.thecube.Cube;

public class RegenerateTask extends BukkitRunnable {
	
	private final Random random = new Random();
	
	private final Location center;
	private final int maxSize;

	private int currentLayer = 1;
	private int oldColor;
	private Long startTime = System.currentTimeMillis();

	public RegenerateTask(int maxSize) {
		this.center = Cube.getCenter();
		this.maxSize = maxSize;
		
		center.getBlock().setType(Material.SEA_LANTERN);
	}
	
	@Override
	public void run() {
		int newColor = this.getColor(this.oldColor);

		for (int i = -currentLayer; i <= currentLayer; i++) {
			for (int j = -currentLayer; j <= currentLayer; j++) {

				//TOP
				this.setBlock(this.center.clone().add(i, currentLayer, j), Material.CONCRETE, newColor);

				//BOTTOM
				this.setBlock(this.center.clone().add(i, -currentLayer, j), Material.CONCRETE, newColor);

				//NORTH
				this.setBlock(this.center.clone().add(i, j, -currentLayer), Material.CONCRETE, newColor);

				//SOUTH
				this.setBlock(this.center.clone().add(i, j, currentLayer), Material.CONCRETE, newColor);

				//EAST
				this.setBlock(this.center.clone().add(currentLayer, i, j), Material.CONCRETE, newColor);

				//WEST
				this.setBlock(this.center.clone().add(-currentLayer, i, j), Material.CONCRETE, newColor);
			}
		}

		this.currentLayer++;
		this.oldColor = newColor;

		if (this.currentLayer > this.maxSize) {

			long length = System.currentTimeMillis() - this.startTime;

			Bukkit.broadcastMessage("Generating took " + (length / 1000) + " seconds");
			
			Cube.finishReset();

			this.cancel();
		}
	}

	private int getColor(int old) {
		int color = this.random.nextInt(15);

		while (old != -1 && old == color) {
			color = this.random.nextInt(15);
		}

		return color;
	}

	@SuppressWarnings("deprecation")
	private void setBlock(Location location, Material material, int color) {
		Block block = location.getBlock();
		
		block.setType(material, false);
		block.setData((byte) color);
	}
	
}
