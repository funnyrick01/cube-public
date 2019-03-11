package com.palagear.thecube.gadgets;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.scheduler.BukkitRunnable;

import com.palagear.thecube.Cube;
import com.palagear.thecube.Main;
import com.palagear.thecube.user.CubeUser;
import com.palagear.thecube.util.BlockUtil;

public class Pacman {

	private CubeUser user;
	private Location location;
	private int maxBlocksBroken;
	
	public Pacman(CubeUser user, Location location) {
		this.user = user;
		this.location = location;
		this.maxBlocksBroken = 75;
		
		new PacmanTask().runTaskTimer(Main.getInstance(), 0, 4);
	}
	
	
	private class PacmanTask extends BukkitRunnable {
		
		private int index = 0;
		
		private PacmanTask() {
			location.getBlock().setType(Material.GOLD_BLOCK);
		}
		
		@Override
		public void run() {
			
			Cube.damageBlock(user, location.getBlock(), Cube.getCurrentLayer().getBlockHealth(), 0, null);
			location.getWorld().playSound(location, Sound.BLOCK_LAVA_POP, 1, 2);
			
			Location oldLocation = location.clone();
			
			for(BlockFace blockFace : BlockUtil.getShuffeldBlockFaces()) {
				Block newBlock = location.getBlock().getRelative(blockFace);
				if(Cube.canBreak(newBlock)) {
					location = newBlock.getLocation();
					break;
				}
			}
			
			if(location.equals(oldLocation) || index >= maxBlocksBroken) {
				this.cancel();
				location.getWorld().playSound(location, Sound.BLOCK_LAVA_POP, 1, 0.5F);
				return;
			} else {
				Cube.damageBlock(user, oldLocation.getBlock(), Cube.getCurrentLayer().getBlockHealth(), 0, null);
				oldLocation.getWorld().playSound(oldLocation, Sound.BLOCK_LAVA_POP, 1, 2);
				
				location.getBlock().setType(Material.GOLD_BLOCK);
			}
			
			index++;
		}
	}
}

