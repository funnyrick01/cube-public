package com.palagear.thecube.layer;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

import com.palagear.thecube.Cube;
import com.palagear.thecube.user.CubeUser;
import com.palagear.thecube.user.UserModule;

import lombok.Getter;

public class Layer {

	private final Location center;
	@Getter private final int position;
	@Getter private int blocksRemaining;
	@Getter private double maxBlocks;
	@Getter private final int blockHealth;

	/**
	 * The constructor for a layer
	 * 
	 * @param position The position of the layer
	 */
	public Layer(int position) {
		this(position, true);
	}

	/**
	 * The constructor for a layer
	 * 
	 * @param position The position of the layer
	 * @param bukkitAvailable Whether bukkit is available. false for unit tests
	 */
	public Layer(int position, boolean bukkitAvailable) {
		this.center = Cube.getCenter().clone();
		this.position = position;
		double lenght = position * 2 + 1;
		this.maxBlocks = (lenght * lenght * lenght) - ((lenght - 2) * (lenght - 2) * (lenght - 2));
		this.maxBlocks = this.maxBlocks <= 0 || position == 0 ? 1 : this.maxBlocks;
		this.blockHealth = (int) (Math.pow(1.25, Cube.getMaxSize() - position) * 10);

		if(bukkitAvailable) {
			this.blocksRemaining = getBlocks();
		}
	}

	private int getBlocks() {

		int blocks = 0;

		for(int x = -position; x <= position; x++) {
			for(int z = -position; z <= position; z++) {
				// TOP
				if(!center.clone().add(x, position, z).getBlock().getType().equals(Material.AIR)) {
					blocks++;
				}

				// BOTTOM
				if(!center.clone().add(x, -position, z).getBlock().getType().equals(Material.AIR)) {
					blocks++;
				}
			}
		}

		for(int x = -position; x <= position; x++) {
			for(int y = -position + 1; y <= position - 1; y++) {
				// NORTH
				if(!center.clone().add(x, y, -position).getBlock().getType().equals(Material.AIR)) {
					blocks++;
				}

				// SOUTH
				if(!center.clone().add(x, y, position).getBlock().getType().equals(Material.AIR)) {
					blocks++;
				}
			}
		}

		for(int z = -position + 1; z <= position - 1; z++) {
			for(int y = -position + 1; y <= position - 1; y++) {
				// EAST
				if(!center.clone().add(position, y, z).getBlock().getType().equals(Material.AIR)) {
					blocks++;
				}

				// WEST
				if(!center.clone().add(-position, y, z).getBlock().getType().equals(Material.AIR)) {
					blocks++;
				}
			}
		}

		if(position == 0) {
			blocks = 1;
		}

		return blocks;
	}

	public boolean isOutsideLayer(Location location) {
		if(location.getBlockX() > center.getBlockX() + position ||
			location.getBlockX() < center.getBlockX() - position ||
			location.getBlockY() > center.getBlockY() + position ||
			location.getBlockY() < center.getBlockY() - position ||
			location.getBlockZ() > center.getBlockZ() + position ||
			location.getBlockZ() < center.getBlockZ() - position) {
			return true;
		}
		return false;
	}

	public boolean isInsideLayer(Location location) {
		if(location.getBlockX() < center.getBlockX() + position &&
			location.getBlockX() > center.getBlockX() - position &&
			location.getBlockY() < center.getBlockY() + position &&
			location.getBlockY() > center.getBlockY() - position &&
			location.getBlockZ() < center.getBlockZ() + position &&
			location.getBlockZ() > center.getBlockZ() - position) {
			return true;
		}
		return false;
	}

	/**
	 * Check if the location is in the layer
	 * 
	 * @param location The location to check
	 * @return true if the location is inside the layer, false otherwise
	 */
	public boolean isInLayer(Location location) {
		return !isInsideLayer(location) && !isOutsideLayer(location);
	}
	
	public boolean isInOrInsideLayer(Location location) {
		return isInLayer(location) || isInsideLayer(location);
	}
	
	public boolean isInOrOutsideLayer(Location location) {
		return isInLayer(location) || isOutsideLayer(location);
	}

	private void onBreak() {
		Cube.setCurrentLayer(Cube.getMostOuterLayer());
		if(Cube.getCurrentLayer() == null) {
			Bukkit.broadcastMessage(ChatColor.YELLOW + "" + ChatColor.STRIKETHROUGH + "-----------------------------------------------------");
			Bukkit.broadcastMessage(ChatColor.GOLD + "The cube has been broken!"); // TODO give player VIP rank
			Bukkit.broadcastMessage(ChatColor.YELLOW + "" + ChatColor.STRIKETHROUGH + "-----------------------------------------------------");
		} else {
			Bukkit.broadcastMessage(ChatColor.YELLOW + "" + ChatColor.STRIKETHROUGH + "-----------------------------------------------------");
			Bukkit.broadcastMessage(ChatColor.GOLD + "A layer has been broken! The next layer has " + Cube.getCurrentLayer().getBlocksRemaining() + " blocks.");
			Bukkit.broadcastMessage(ChatColor.YELLOW + "" + ChatColor.STRIKETHROUGH + "-----------------------------------------------------");
		}

		for(CubeUser user : UserModule.getCubeUsers()) {
			firework(user.getPlayer());
			user.getScoreboard().setLayerProgress(getProgress());
		}
	}

	private void firework(Player player) {
		Firework firework = (Firework) Bukkit.getWorlds().get(0).spawnEntity(player.getLocation(), EntityType.FIREWORK);
		FireworkMeta fireworkMeta = firework.getFireworkMeta();
		FireworkEffect.Builder builder = FireworkEffect.builder();
		fireworkMeta.clearEffects();
		builder.with(FireworkEffect.Type.BALL);
		builder.withTrail();
		Random random = new Random();
		int rgb1 = random.nextInt(255);
		int rgb2 = random.nextInt(255);
		int rgb3 = random.nextInt(255);
		builder.withFade(Color.fromRGB(rgb1, rgb2, rgb3));
		builder.withColor(Color.fromRGB(rgb1, rgb2, rgb3));
		fireworkMeta.addEffects(new FireworkEffect[] { builder.build() });
		fireworkMeta.setPower(0);
		firework.setFireworkMeta(fireworkMeta);
	}

	/**
	 * @return the progress
	 */
	public double getProgress() {
		return blocksRemaining / maxBlocks * 100;
	}

	/**
	 * Reduce the remaining blocks
	 */
	public void reduceBlocksRemaining() {
		blocksRemaining--;
		UserModule.getCubeUsers().forEach(user -> user.getScoreboard().setLayerProgress(getProgress()));

		if(blocksRemaining <= 0) {
			onBreak();
		}
	}
}
