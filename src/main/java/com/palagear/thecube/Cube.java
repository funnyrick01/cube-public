package com.palagear.thecube;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import com.palagear.thecube.database.tableContainers.CubeContainer;
import com.palagear.thecube.layer.Layer;
import com.palagear.thecube.tasks.RegenerateTask;
import com.palagear.thecube.user.CubeUser;
import com.palagear.thecube.user.UserModule;
import com.palagear.thecube.util.Side;

import lombok.Getter;
import lombok.Setter;
import me.palagear.core.user.User;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class Cube {

	private static final String playerDamageMeta = "playerDamage:";
	public static final Random random = new Random();

	@Getter private static Location center;
	@Getter private final static int maxSize = 124;
	@Getter private static org.bukkit.World world;

	@Getter private static int ID;
	@Getter private static List<Layer> layers;
	@Getter @Setter private static Layer currentLayer;
	@Getter private static Timestamp createdAt;
	@Getter private static Timestamp destroyedAt;
	
	public static void load() {
		load(-1, true);
	}

	public static void load(int cubeID, boolean bukkitAvailable) {

		layers = new ArrayList<>();
		
		if(bukkitAvailable) {
			world = Bukkit.getWorlds().get(0);
			center = new Location(world, 0, 128, 0);

			for(int i = 0; i <= maxSize; i++) {
				layers.add(new Layer(i));
			}
			currentLayer = getMostOuterLayer();
		} else {
			center = new Location(null, 0, 128, 0);
		}

		CubeContainer cube;
		if(cubeID < 0) {
			cube = com.palagear.thecube.database.queries.Cube.getCurrentCube();
		} else {
			cube = com.palagear.thecube.database.queries.Cube.getCube(cubeID);
		}

		ID = cube.getID();
		createdAt = cube.getCreatedAt();
		destroyedAt = cube.getDestroyedAt();
	}

	public static void reset() throws Exception {
		CubeContainer cube = com.palagear.thecube.database.queries.Cube.createNewCube();

		if(cube == null || cube.getID() == ID)
			throw new Exception("Could not create a new cube!");

		UserModule.getCubeUsers().forEach(user -> {
			user.save();
			user.getPlayer().setGameMode(GameMode.SPECTATOR);
		});

		Bukkit.broadcastMessage(ChatColor.GREEN + "Generating new cube...");

		new RegenerateTask(maxSize).runTaskTimer(Main.getInstance(), 20, 5);
	}

	public static void finishReset() {
		layers = new ArrayList<>();

		for(int i = 0; i <= maxSize; i++) {
			layers.add(new Layer(i));
		}
		currentLayer = getMostOuterLayer();

		Cube.load();

		UserModule.reload();
	}

	public static final boolean canBreak(Block block) {
		return block != null && currentLayer.isInLayer(block.getLocation()) && block.getType() != Material.AIR;
	}

	public static final void damageBlock(User user, Block block, double damage, double spreadDamage, BlockFace face) {
		if(canBreak(block)) {
			double health = getBlockHealth(block);

			if(block.hasMetadata(playerDamageMeta + user.getUniqueId()) && block.getMetadata(playerDamageMeta + user.getUniqueId()).size() > 0) {
				double playerDamage = block.getMetadata(playerDamageMeta + user.getUniqueId()).get(0).asDouble();
				block.setMetadata(playerDamageMeta + user.getUniqueId(), new FixedMetadataValue(Main.getInstance(), playerDamage + (health - damage <= 0 ? health : damage)));
			} else {
				block.setMetadata(playerDamageMeta + user.getUniqueId(), new FixedMetadataValue(Main.getInstance(), health - damage <= 0 ? health : damage));
			}

			double remainingHealth = health - damage;
			if(remainingHealth <= 0) {
				breakBlock(block);
				if(remainingHealth < 0 && face != null) {
					double remainingDamage = (damage - health) * (spreadDamage / 100);
					damageBlock(user, block.getRelative(face), remainingDamage, spreadDamage, face);
				}
			} else {
				block.setMetadata("health", new FixedMetadataValue(Main.getInstance(), remainingHealth));
			}
			remainingHealth = remainingHealth < 0 ? 0 : Math.round(remainingHealth * 100) / 100d;

			String message = ChatColor.translateAlternateColorCodes('&', "&6[&2" + remainingHealth + "&6/&2" + currentLayer.getBlockHealth() + "&6]");
			user.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
		}
	}
	
	public static final void breakBlock(Block block, CubeUser user) {
		damageBlock(user, block, currentLayer.getBlockHealth(), 0, null);
	}

	private static final void breakBlock(Block block) {

		if(!canBreak(block))
			return;

		HashMap<Player, Double> players = new HashMap<>();

		for(Player player : Bukkit.getOnlinePlayers()) {
			if(block.hasMetadata(playerDamageMeta + player.getUniqueId())) {
				players.put(player, block.getMetadata(playerDamageMeta + player.getUniqueId()).get(0).asDouble());
				block.removeMetadata(playerDamageMeta + player.getUniqueId(), Main.getInstance());
			}
		}

		for(Map.Entry<Player, Double> entry : players.entrySet()) {
			CubeUser user = UserModule.getUser(entry.getKey());
			double damage = entry.getValue();

			double reward = damage / (double) currentLayer.getBlockHealth() * ((double) currentLayer.getBlockHealth() / 20d);

			user.addBlocksBroken(1);
			user.addTickets(reward);
			user.addExp((int) (currentLayer.getBlockHealth() / damage * 10));
		}

		block.removeMetadata("health", Main.getInstance());
		block.breakNaturally(new ItemStack(Material.AIR));
		block.getLocation().getWorld().playSound(block.getLocation(), Sound.BLOCK_STONE_BREAK, SoundCategory.BLOCKS, 1, 1);
		block.getLocation().getWorld().spawnParticle(Particle.BLOCK_DUST, block.getLocation(), 1);
		currentLayer.reduceBlocksRemaining();
	}
	
	public static final double getBlockHealth(Block block) {
		double health;
		if(block.hasMetadata("health")) {
			health = block.getMetadata("health").get(0).asDouble();
		} else {
			health = currentLayer.getBlockHealth();
		}
		return health;
	}

	public static Layer getMostOuterLayer() {
		Layer result = null;
		for(Layer layer : layers) {
			if(layer.getBlocksRemaining() > 0) {
				result = layer;
			}
		}
		return result;
	}
	
	public static Side getSideOfLocation(Location location) {
		Location direction = new Location(null, 0, 0, 0).setDirection(location.clone().subtract(center).toVector());
		double pitch = direction.getPitch();
		double yaw = direction.getYaw();
		
		Side side;
		
		if(pitch < -45) {
			side = Side.Up;
		} else if(pitch > 45) {
			side = Side.Down;
		} else if(yaw >= 315 || yaw < 45) {
			side = Side.South;
		} else if(yaw >= 225 && yaw < 315) {
			side = Side.East;
		} else if(yaw >= 135 && yaw < 225) {
			side = Side.North;
		} else {
			side = Side.West;
		}
		
		return side;
	}
}
