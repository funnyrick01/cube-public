package com.palagear.thecube.inventories.item;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import com.palagear.thecube.autoPickaxe.AutoPickaxe;
import com.palagear.thecube.user.CubeUser;
import com.palagear.thecube.user.UserModule;

import lombok.Getter;
import me.palagear.core.gui.ClickEventArgs;
import me.palagear.core.gui.GuiItem;
import me.palagear.core.gui.GuiMenu;
import me.palagear.core.util.MathUtils;

public class UpgradeableItem extends GuiItem {

	@Getter private final int level;
	@Getter private final int maxLevel;
	@Getter private final double upgradeCostPower;
	@Getter private final double minUpgradeCost;
	
	@Getter private final double upgradeCost;
	
	private BuyHandler buyHandler;

	/**
	 * Create an upgradeable GUI item.
	 * @param material the {@link Material}
	 * @param guiMenu the {@link GuiMenu}
	 * @param displayName the display name of the item
	 * @param description a quick description of the upgrade. use \n for a newline
	 * @param calculation the method to calculate the value of the level. ex: {@link AutoPickaxe#getDamage(int)}
	 * @param level the current level
	 * @param maxLevel the max level
	 * @param upgradeCostPower the power of which the upgrade cost is being calculated with
	 * @param minUpgradeCost the min upgrade cost
	 */
	public UpgradeableItem(Material material, GuiMenu guiMenu, String displayName, String description, UpgradeValueCalculation calculation, int level, int maxLevel, double upgradeCostPower, double minUpgradeCost) {
		super(material, guiMenu);

		this.level = level;
		this.maxLevel = maxLevel >= 0 ? maxLevel : Integer.MAX_VALUE;
		this.upgradeCostPower = upgradeCostPower;
		this.minUpgradeCost = minUpgradeCost;
		
		this.upgradeCost = MathUtils.round(Math.pow(this.upgradeCostPower, this.level - 1) * this.minUpgradeCost, 2);

		setDisplayName(displayName);
		int amount = level > 1 ? level : 1;
		amount = level > 64 ? 64 : amount;
		setAmount(amount);
		
		List<String> lore = new ArrayList<String>();
		if(this.level >= this.maxLevel) {
			lore.add(ChatColor.RED + "" + calculation.calculate(level));
		} else {
			lore.add(ChatColor.RED + "" + calculation.calculate(level) + ChatColor.DARK_RED + " -> " + ChatColor.RED + calculation.calculate(level + 1));
		}
		
		for(String line : description.split("[\\r\\n]+")) {
			lore.add(line);
		}
		lore.add("");
		if(this.level >= this.maxLevel) {
			lore.add(ChatColor.DARK_AQUA + "Current level: " + this.level + ChatColor.DARK_RED + " MAX");
		} else {
			lore.add(ChatColor.DARK_AQUA + "Current level: " + this.level);
			lore.add(ChatColor.DARK_AQUA + "Cost: " + upgradeCost);
		}
		
		setLore(lore);
	}
	
	public final UpgradeableItem onBuy(BuyHandler handler) {
		this.buyHandler = handler;
		return this;
	}

	@Override
	protected final void onClickEvent(ClickEventArgs args) {
		Player player = args.getPlayer();
		CubeUser user = UserModule.getUser(player);
		
		if(this.buyHandler != null && this.level < this.maxLevel && user.reduceTickets(this.upgradeCost)) {
			this.buyHandler.handleBuy();
			player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1.5f);
			guiMenu.reBuild();
		} else {
			player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 0.5f);
		}
	}
}