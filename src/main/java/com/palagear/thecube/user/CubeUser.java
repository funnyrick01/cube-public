package com.palagear.thecube.user;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.palagear.thecube.Cube;
import com.palagear.thecube.autoPickaxe.AutoPickaxe;
import com.palagear.thecube.database.queries.Users;
import com.palagear.thecube.database.tableContainers.CubeUserContainer;
import com.palagear.thecube.database.tableContainers.UserContainer;
import com.palagear.thecube.gadgets.Gadget;
import com.palagear.thecube.inventories.Shop;
import com.palagear.thecube.scoreboard.StatsScoreboard;
import com.palagear.thecube.util.ItemStackUtil;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import me.palagear.core.gui.GuiItem;
import me.palagear.core.user.User;
import me.palagear.core.util.MathUtils;
import test.users.DummyPlayer;

@ToString
public class CubeUser extends User {

	@Getter private String uuid;
	@Getter @Setter private int exp;
	@Getter @Setter private int blocksBroken;
	@Getter @Setter private long timePlayed;

	@Getter private double tickets;
	@Getter private double tokens;

	// Upgrades
	@Getter @Setter private int handDamageLevel;
	@Getter @Setter private int spreadDamageLevel;
	@Getter @Setter private int critChanceLevel;
	@Getter @Setter private int critDamageLevel;

	@Getter @Setter private AutoPickaxe autoPickaxe;

	// Gadgets
	@Getter private Gadget currentGadget;
	@Getter private Map<Gadget, Integer> gadgets;

	@Getter private StatsScoreboard scoreboard;

	public CubeUser(Player player) {
		super(player);

		loadCubeUser();

		this.player.setGameMode(GameMode.SURVIVAL);
		this.player.setLevel(0);
		this.player.setExp(0);
		this.player.giveExp(exp);
		this.player.setAllowFlight(true);
		this.player.setFlySpeed(0.1f);
		this.player.setFlying(true);
		this.player.teleport(Cube.getCenter().clone().add(0, Cube.getLayers().size() + 5, 0));
		this.player.getInventory().clear();
		this.player.getInventory().setItem(2, autoPickaxe.getItem());
		this.player.getInventory().setItem(4, new GuiItem(Material.COMPASS, ChatColor.BLUE + "Shop").onInteract((args) -> new Shop(args.getPlayer()).open()));
	}

	public CubeUser(DummyPlayer player) {
		super(player);
		loadCubeUser(false);
	}

	public void loadCubeUser() {
		loadCubeUser(true);
	}

	public void loadCubeUser(boolean BukkitAvailable) {
		UserContainer userData = Users.getUser(player.getUniqueId());

		if(userData == null) {
			Users.addUser(player.getUniqueId());
			userData = Users.getUser(player.getUniqueId());
		}

		if(userData != null) {
			uuid = userData.getUUID();
			exp = userData.getExp();
			tokens = userData.getTokens();
		} else {
			uuid = player.getUniqueId().toString();
			exp = 0;
			tokens = 0;
		}

		CubeUserContainer cubeUserData = Users.getCubeUser(player.getUniqueId(), Cube.getID());

		if(cubeUserData == null) {
			Users.addCubeUser(player.getUniqueId(), Cube.getID());
			cubeUserData = Users.getCubeUser(player.getUniqueId(), Cube.getID());
		}

		if(cubeUserData != null) {
			tickets = cubeUserData.getTickets();
			timePlayed = cubeUserData.getTimePlayed();
			blocksBroken = cubeUserData.getBlocksBroken();
			handDamageLevel = cubeUserData.getHandDamageLevel();
			spreadDamageLevel = cubeUserData.getSpreadDamageLevel();
			critChanceLevel = cubeUserData.getCritChanceLevel();
			critDamageLevel = cubeUserData.getCritDamageLevel();
			autoPickaxe = new AutoPickaxe(this,
				cubeUserData.getPickaxeLevel(),
				cubeUserData.getPickaxeSpeedLevel(),
				cubeUserData.getPickaxeDamageLevel(),
				cubeUserData.getPickaxeRangeLevel());

			gadgets = new HashMap<Gadget, Integer>();
			gadgets.put(Gadget.pacman, cubeUserData.getGadget_pacman());
			gadgets.put(Gadget.nuke, cubeUserData.getGadget_nuke());
			gadgets.put(Gadget.bomb, cubeUserData.getGadget_bomb());
			gadgets.put(Gadget.infection, cubeUserData.getGadget_infection());
			gadgets.put(Gadget.storm, cubeUserData.getGadget_storm());
			gadgets.put(Gadget.ticketBooster, cubeUserData.getGadget_ticketBooster());

		} else {
			tickets = 0;
			timePlayed = 0;
			blocksBroken = 0;
			handDamageLevel = 1;
			spreadDamageLevel = 0;
			critChanceLevel = 0;
			critDamageLevel = 0;
			autoPickaxe = new AutoPickaxe(this, 1, 1, 1, 1);

			gadgets = new HashMap<Gadget, Integer>();
			gadgets.put(Gadget.pacman, 0);
			gadgets.put(Gadget.nuke, 0);
			gadgets.put(Gadget.bomb, 0);
			gadgets.put(Gadget.infection, 0);
			gadgets.put(Gadget.storm, 0);
			gadgets.put(Gadget.ticketBooster, 0);
		}

		if(BukkitAvailable) {
			scoreboard = new StatsScoreboard(this);
		}
	}

	@Override
	public void save() {
		Users.saveUser(new UserContainer(uuid, exp, tokens));
		Users.saveCubeUser(new CubeUserContainer(
			Cube.getID(),
			uuid,
			tickets,
			timePlayed,
			blocksBroken,
			handDamageLevel,
			spreadDamageLevel,
			critChanceLevel,
			critDamageLevel,
			autoPickaxe.getLevel(),
			autoPickaxe.getDamageLevel(),
			autoPickaxe.getSpeedLevel(),
			autoPickaxe.getRangeLevel(),
			gadgets.get(Gadget.pacman),
			gadgets.get(Gadget.nuke),
			gadgets.get(Gadget.bomb),
			gadgets.get(Gadget.infection),
			gadgets.get(Gadget.storm),
			gadgets.get(Gadget.ticketBooster)));
	}

	public void updateAutoPickaxeItem() {
		this.player.getInventory().setItem(2, autoPickaxe.getItem());
	}

	public void setCurrentGadget(Gadget gadget) {
		this.currentGadget = gadget;
		
		GuiItem item = gadget.getAsItem();
		
		if(this.gadgets.get(currentGadget) > 0) {
			item.onInteract((args) -> {
				
				if(Cube.canBreak(args.getClickedBlock())) {
					if(gadgets.get(this.currentGadget) > 0) {
						this.currentGadget.use(this, args.getClickedBlock().getLocation());
						
						this.gadgets.put(currentGadget, this.gadgets.get(currentGadget) - 1);
						args.getItem().setAmount(ItemStackUtil.getAllowedItemstackAmount(this.gadgets.get(currentGadget)));
					}
				}
				
			});
		}
		
		item.setAmount(ItemStackUtil.getAllowedItemstackAmount(this.gadgets.get(currentGadget)));

		player.getInventory().setItem(5, item);
	}

	public static double getHandDamageUpgradeCost(int level) {
		return MathUtils.round(Math.pow(1.3, level - 1) * 10, 2);
	}

	public static double getHandDamage(int level) {
		return MathUtils.round(Math.pow(1.2, level - 1), 2);
	}

	public static double getSpreadDamage(int level) {
		return level;
	}

	public static double getCritChance(int level) {
		return level > 0 ? level + 4 : 0;
	}

	public static double getCritDamage(int level) {
		return level > 0 ? level * 1.5 + 23.5 : 0;
	}

	public double getHandDamage() {
		return getHandDamage(handDamageLevel);
	}

	public double getSpreadDamage() {
		return getSpreadDamage(spreadDamageLevel);
	}

	public double getCritChance() {
		return getCritChance(critChanceLevel);
	}

	public double getCritDamage() {
		return getCritChance(critDamageLevel);
	}

	public void addBlocksBroken(int amount) {
		blocksBroken += amount;
	}

	public void addHandDamageLevel(int level) {
		this.handDamageLevel += level;
	}

	public void addSpreadDamageLevel(int level) {
		this.spreadDamageLevel += level;
	}

	public void addCritChanceLevel(int level) {
		this.critChanceLevel += level;
	}

	public void addCritDamageLevel(int level) {
		this.critDamageLevel += level;
	}

	/**
	 * @param exp the exp to add
	 */
	public void addExp(int exp) {
		this.exp += exp;
		this.player.giveExp(exp);
	}

	/**
	 * @param tickets the tickets to set
	 */
	public void setTickets(double tickets) {
		this.tickets = tickets;
		if(scoreboard != null)
			scoreboard.setTickets(this.tickets);
	}

	/**
	 * @param tickets the tickets to add
	 */
	public void addTickets(double tickets) {
		this.tickets += tickets;
		if(scoreboard != null)
			scoreboard.setTickets(this.tickets);
	}

	/**
	 * @param tickets the tickets to reduce
	 * @return true if the user had enough tickets, false otherwise
	 */
	public boolean reduceTickets(double tickets) {
		if(this.tickets >= tickets) {
			this.tickets -= tickets;
			if(scoreboard != null)
				scoreboard.setTickets(this.tickets);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @param tokens the tokens to set
	 */
	public void setTokens(double tokens) {
		this.tokens = tokens;
		if(scoreboard != null)
			scoreboard.setTokens(this.tokens);
	}

	/**
	 * @param tokens the tokens to add
	 */
	public void addTokens(double tokens) {
		this.tokens += tokens;
		if(scoreboard != null)
			scoreboard.setTokens(this.tokens);
	}

	/**
	 * @param timePlayed the timePlayed to add
	 */
	public void addTimePlayed(long timePlayed) {
		this.timePlayed += timePlayed;
	}

}
