package com.palagear.thecube.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.palagear.thecube.Cube;
import com.palagear.thecube.Main;
import com.palagear.thecube.user.CubeUser;
import com.palagear.thecube.user.UserModule;

import me.palagear.core.util.MathUtils;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class playerListener implements Listener {

	private long lastSystemTime = System.currentTimeMillis();

	public playerListener() {
		Main.getInstance().getServer().getPluginManager().registerEvents(this, Main.getInstance());

		Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable() {
			public void run() {
				long timePlayed = System.currentTimeMillis() - lastSystemTime;
				lastSystemTime = System.currentTimeMillis();
				for(CubeUser user : UserModule.getCubeUsers()) {
					if(!user.isAFK()) {
						user.addTimePlayed(timePlayed);
					}
				}
			}
		}, 0L, 20L);
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onBlockDamage(BlockDamageEvent event) {
		if(event.getItemInHand().getType() == Material.AIR) {
			CubeUser user = UserModule.getUser(event.getPlayer());
			Player player = event.getPlayer();
			
			double pitch = player.getEyeLocation().getPitch();
			double yaw = player.getEyeLocation().getYaw();
			
			BlockFace face;
			
			if(pitch < -45) {
				face = BlockFace.UP;
			} else if(pitch > 45) {
				face = BlockFace.DOWN;
			} else if(yaw >= 315 || yaw < 45) {
				face = BlockFace.SOUTH;
			} else if(yaw >= 225 && yaw < 315) {
				face = BlockFace.EAST;
			} else if(yaw >= 135 && yaw < 225) {
				face = BlockFace.NORTH;
			} else {
				face = BlockFace.WEST;
			}
			
			double damage = user.getHandDamage();
			
			if(user.getCritChance() > Cube.random.nextInt(100)) {
				damage *= 1 + (user.getCritDamage() / 100);
				Cube.getWorld().spawnParticle(Particle.FLAME, event.getBlock().getLocation(), 5, 0.5, 0.5, 0.5, 0);
			}
			
			Cube.damageBlock(user, event.getBlock(), damage, user.getSpreadDamage(), face);
		}
		event.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onBlockInterect(PlayerInteractEvent event) {
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getItem() == null) {
			Block block = event.getClickedBlock();
			if(Cube.canBreak(block)) {
				double remainingHealth = MathUtils.round(Cube.getBlockHealth(event.getClickedBlock()), 2);
				String message = ChatColor.translateAlternateColorCodes('&', "&6[&2" + remainingHealth + "&6/&2" + Cube.getCurrentLayer().getBlockHealth() + "&6]");
				event.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onDamageTaken(EntityDamageEvent e) {
		e.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.HIGH) 
	public void onItemDrop(PlayerDropItemEvent event) {
		event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onJoin(PlayerJoinEvent event) {
		UserModule.getCubeUsers().forEach(user -> user.getScoreboard().setOnlinePlayers(Bukkit.getOnlinePlayers().size()));
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onQuit(PlayerQuitEvent event) {
		UserModule.getCubeUsers().forEach(user -> user.getScoreboard().setOnlinePlayers(Bukkit.getOnlinePlayers().size() - 1));
	}

}
