package com.palagear.thecube.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.block.BlockFace;

import com.palagear.thecube.Cube;

import lombok.Getter;

public class BlockUtil {

	@Getter private final static List<BlockFace> blockFaces = new ArrayList<BlockFace>() {
		private static final long serialVersionUID = 6191388308956963750L;
		{
			add(BlockFace.UP);
			add(BlockFace.DOWN);
			add(BlockFace.NORTH);
			add(BlockFace.EAST);
			add(BlockFace.SOUTH);
			add(BlockFace.WEST);
		}
	};

	public static BlockFace getRandomBlockFace() {
		return blockFaces.get(Cube.random.nextInt(blockFaces.size()));
	}
	
	public static List<BlockFace> getShuffeldBlockFaces() {
		List<BlockFace> list = new ArrayList<BlockFace>(blockFaces);
		Collections.shuffle(list);
		return list;
	}

}
