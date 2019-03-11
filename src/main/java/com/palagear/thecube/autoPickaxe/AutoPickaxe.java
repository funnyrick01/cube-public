package com.palagear.thecube.autoPickaxe;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.palagear.thecube.Cube;
import com.palagear.thecube.Main;
import com.palagear.thecube.user.CubeUser;

import lombok.Getter;
import lombok.Setter;
import me.palagear.core.util.MathUtils;
import me.palagear.core.util.SetItemsUtil;

public class AutoPickaxe {
	
	@Getter private final CubeUser user;
	@Getter private final Player player;
	@Getter @Setter private Material material;
	@Getter private int level;
	@Getter private int speedLevel;
	@Getter private int damageLevel;
	@Getter private int rangeLevel;
	private Integer taskID;
	
	public AutoPickaxe(CubeUser user, int level, int speedLevel, int damageLevel, int rangeLevel) {
		this.user = user;
		this.player = user.getPlayer();
		this.material = Material.WOOD_PICKAXE;
		this.level = level;
		this.speedLevel = speedLevel;
		this.damageLevel = damageLevel;
		this.rangeLevel = rangeLevel;
	}
	
	public void activate() {
		if(taskID == null) {
			taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable() {
	            public void run() {
	            	
	            	if(!player.getInventory().getItemInMainHand().getType().equals(material)) {
	            		deactivate();
	            	}
	            	
	            	Block block = player.getTargetBlock(null, (int)Math.ceil(getRange()));
	            	if(block != null && block.getType() != Material.AIR) {
	            		Location blockLocation = block.getLocation().add(0.5,  0.5,  0.5);
	            		if(blockLocation.distance(player.getEyeLocation()) <= getRange()) {
		            		Cube.damageBlock(user, block, getDamage(), 0, null);
		            	}
	            	}
	            }
	        }, 0L, getSpeed());
		}
	}
	
	public void deactivate() {
		if(this.taskID != null) {
			Bukkit.getScheduler().cancelTask(taskID);
			this.taskID = null;
		}
	}
	
	public static double getDamageUpgradeCost(int level) {
		return MathUtils.round(Math.pow(1.25, level - 1), 2);
	}
	
	public static double getSpeedUpgradeCost(int level) {
		return MathUtils.round(Math.pow(2, level - 1), 2);
	}
	
	public static double getRangeUpgradeCost(int level) {
		return MathUtils.round(Math.pow(1.8, level - 1), 2);
	}
	
	public static double getDamage(int level) {
		return MathUtils.round(Math.pow(1.2, level - 1) + 1, 2);
	}
	
	public static int getSpeed(int level) {
		return level < 20 ? 20 - level : 1;
	}
	
	public static double getRange(int level) {
		return level + 3;
	}
	
	public double getDamage() {
		return getDamage(damageLevel);
	}
	
	public int getSpeed() {
		return getSpeed(speedLevel);
	}
	
	public double getRange() {
		return getRange(rangeLevel);
	}

	/**
	 * @param level the level to set
	 */
	public void setLevel(int level) {
		this.level = level;
		user.updateAutoPickaxeItem();
	}

	/**
	 * @param speedLevel the speedLevel to set
	 */
	public void setSpeedLevel(int speedLevel) {
		this.speedLevel = speedLevel;
		user.updateAutoPickaxeItem();
	}

	/**
	 * @param levels the levels to add
	 */
	public void addSpeedLevel(int levels) {
		setSpeedLevel(speedLevel + levels);
	}

	/**
	 * @param damageLevel the damageLevel to set
	 */
	public void setDamageLevel(int damageLevel) {
		this.damageLevel = damageLevel;
		user.updateAutoPickaxeItem();
	}
	
	/**
	 * @param levels the levels to add
	 */
	public void addDamageLevel(int levels) {
		setDamageLevel(damageLevel + levels);
	}

	/**
	 * @param rangeLevel the rangeLevel to set
	 */
	public void setRangeLevel(int rangeLevel) {
		this.rangeLevel = rangeLevel;
		user.updateAutoPickaxeItem();
	}

	/**
	 * @param levels the levels to add
	 */
	public void addRangeLevel(int levels) {
		setRangeLevel(rangeLevel + levels);
	}
	
	public ItemStack getItem() {
		return SetItemsUtil.setItem(
				material, 
				ChatColor.BLUE + "Auto Pickaxe", 
				Arrays.asList(
						ChatColor.WHITE + "Damage=" + getDamage(), 
						ChatColor.WHITE + "Speed=" + getSpeed(), 
						ChatColor.WHITE + "Range=" + getRange()), 
				false);
	}
	
}
