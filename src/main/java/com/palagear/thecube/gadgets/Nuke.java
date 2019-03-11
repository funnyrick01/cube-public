package com.palagear.thecube.gadgets;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.palagear.thecube.Cube;
import com.palagear.thecube.Main;
import com.palagear.thecube.user.CubeUser;
import com.palagear.thecube.util.MultiEntityStructure;
import com.palagear.thecube.util.Side;

public class Nuke {

	private CubeUser user;
	private Location missileLocation;
	private Location location;

	private MultiEntityStructure multiEntityStructure;

	public Nuke(CubeUser user, Location location) {
		this.user = user;
		this.location = location;
		Vector direction = Cube.getSideOfLocation(location).toVector().normalize().multiply(-1);
		this.missileLocation = location.clone().setDirection(direction).add(direction.multiply(-50));

		new NukeTask().runTaskTimer(Main.getInstance(), 0, 1);
	}

	private class NukeTask extends BukkitRunnable {
		private int explosionSize = 40;
		
		private int index = 0;
		private int explosionIndex = 0;
		private boolean exploding = false;
		
		private List<Block> iteratedBlocks = new ArrayList<>();
		private List<Block> blocksIterator = new ArrayList<>();

		private NukeTask() {
			multiEntityStructure = new MultiEntityStructure(missileLocation);
			multiEntityStructure.addBlock(Material.TNT, new Vector(0, 0, 0));
			multiEntityStructure.addBlock(Material.TNT, new Vector(0.6, 0, 0));
			multiEntityStructure.addBlock(Material.TNT, new Vector(1.1, 0.75, 0), true);
		}

		@Override
		public void run() {

			multiEntityStructure.teleport(missileLocation.add(missileLocation.getDirection().multiply(0.5)));

			if(index == 100) {
				multiEntityStructure.remove();
				location.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, location, 5);
				blocksIterator.add(location.getBlock());
				exploding = true;
			}
			
			if(exploding) {
				
				for(Block block : blocksIterator) {
					Cube.breakBlock(block, user);
					block.getLocation().getWorld().spawnParticle(Particle.EXPLOSION_LARGE, block.getLocation().add(0.5, 0.5, 0.5), 1);
				}
				
				if(explosionIndex >= explosionSize) {
					cancel();
				} else {
					blocksIterator = getAdjacentBlocks(blocksIterator);
				}
				
				explosionIndex++;
			}
			
			if(index >= 1000 || (explosionIndex >= 1000 && exploding)) {
				cancel();
			}
			
			index++;
		}
		
		/**
		 * Get the adjacent blocks from all the blocks in the given list.
		 * It will only return blocks that may be broken in the layer and
		 * have not been iterated on before.
		 * @param blocks The blocks to get the adjacent blocks from.
		 * @return A list containing all valid adjacent blocks.
		 */
		private List<Block> getAdjacentBlocks(List<Block> blocks) {
			
			List<Block> result = new ArrayList<>();
			
			for(Block block : blocks) {
				for(Side side : Side.values()) {
					Block relitiveBlock = block.getRelative(side.toBlockFace());
					if(Cube.getCurrentLayer().isInLayer(block.getLocation()) && !result.contains(relitiveBlock)) {
						if(!iteratedBlocks.contains(relitiveBlock)) {
							result.add(relitiveBlock);
						}
					}
				}
			}
			
			iteratedBlocks.addAll(result);
			return result;
		}
	}
}
