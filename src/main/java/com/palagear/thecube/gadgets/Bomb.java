package com.palagear.thecube.gadgets;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

import com.palagear.thecube.Cube;
import com.palagear.thecube.Main;
import com.palagear.thecube.user.CubeUser;
import com.palagear.thecube.util.Side;

public class Bomb {
	private CubeUser user;
	private Location location;

	public Bomb(CubeUser user, Location location) {
		this.user = user;
		this.location = location;

		new BombTask().runTaskTimer(Main.getInstance(), 0, 1);
	}

	private class BombTask extends BukkitRunnable {
		private int explosionSize = 7;

		private int index = 0;

		private List<Block> iteratedBlocks = new ArrayList<>();
		private List<Block> blocksIterator = new ArrayList<>();
		
		public BombTask() {
			blocksIterator.add(location.getBlock());
			location.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, location, 1);
		}

		@Override
		public void run() {

			for(Block block : blocksIterator) {
				Cube.breakBlock(block, user);
			}

			if(index >= explosionSize) {
				cancel();
			} else {
				blocksIterator = getAdjacentBlocks(blocksIterator);
			}

			if(index >= 1000) {
				cancel();
			}

			index++;
		}

		/**
		 * Get the adjacent blocks from all the blocks in the given list. It
		 * will only return blocks that may be broken in the layer and have not
		 * been iterated on before.
		 * 
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
