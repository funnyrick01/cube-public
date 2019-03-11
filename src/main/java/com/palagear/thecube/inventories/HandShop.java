package com.palagear.thecube.inventories;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.palagear.thecube.inventories.item.UpgradeableItem;
import com.palagear.thecube.user.CubeUser;
import com.palagear.thecube.user.UserModule;

import me.palagear.core.gui.GUIHolder;
import me.palagear.core.gui.GuiMenu;

public class HandShop extends GuiMenu {

	private final CubeUser user;

	public HandShop(Player p) {
		super(p, new GUIHolder(), 27, "Hand shop");
		user = UserModule.getUser(p);

		//setInventory(Bukkit.createInventory(new GUIHolder(), 27, "Hand shop"));

		reBuild();
	}

	@Override
	public void reBuild() {
		setItem(10, new UpgradeableItem(
			Material.BOOK,
			this,
			ChatColor.GREEN + "Upgrade damage",
			"makes you do more damage per hit!",
			CubeUser::getHandDamage,
			user.getHandDamageLevel(),
			-1,
			1.3,
			10).onBuy(() -> {
				user.addHandDamageLevel(1);
			}));
		
		setItem(11, new UpgradeableItem(
			Material.BOOK,
			this,
			ChatColor.GREEN + "Upgrade spread damage",
			"Don't let your extra damage go to waste!\n"
			+ "When you do more damage then a block has\n"
			+ "left as health, your remaining damage will\n"
			+ "go to the block behind it.",
			CubeUser::getSpreadDamage,
			user.getSpreadDamageLevel(),
			75,
			1.2,
			200).onBuy(() -> {
				user.addSpreadDamageLevel(1);
			}));
		

		setItem(12, new UpgradeableItem(
			Material.BOOK,
			this,
			ChatColor.GREEN + "Upgrade crit change",
			"Gain a higher chance to do a critical hit!",
			CubeUser::getCritChance,
			user.getCritChanceLevel(),
			75,
			1.19,
			50).onBuy(() -> {
				user.addCritChanceLevel(1);
			}));
		
		setItem(13, new UpgradeableItem(
			Material.BOOK,
			this,
			ChatColor.GREEN + "Upgrade crit damage",
			"Make your critical hits do more damage!",
			CubeUser::getCritDamage,
			user.getCritDamageLevel(),
			-1,
			1.18,
			50).onBuy(() -> {
				user.addCritDamageLevel(1);
			}));
	}

}
