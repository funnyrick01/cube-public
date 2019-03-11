package com.palagear.thecube.inventories;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.palagear.thecube.autoPickaxe.AutoPickaxe;
import com.palagear.thecube.inventories.item.UpgradeableItem;
import com.palagear.thecube.user.CubeUser;
import com.palagear.thecube.user.UserModule;

import me.palagear.core.gui.GUIHolder;
import me.palagear.core.gui.GuiMenu;

public class AutoPickaxeShop extends GuiMenu {

	private final CubeUser user;
	private AutoPickaxe pick;

	public AutoPickaxeShop(Player p) {
		super(p, new GUIHolder(), 27, "Autopickaxe shop");
		user = UserModule.getUser(p);

		//setInventory(Bukkit.createInventory(new GUIHolder(), 27, "Autopickaxe shop"));

		reBuild();
	}

	@Override
	public void reBuild() {

		pick = user.getAutoPickaxe();

		setItem(10, new UpgradeableItem(
			Material.BOOK,
			this,
			ChatColor.GREEN + "Upgrade damage",
			"makes your auto pickaxe do more damage per hit!",
			AutoPickaxe::getDamage,
			pick.getDamageLevel(),
			-1,
			1.25,
			10).onBuy(() -> {
				pick.addDamageLevel(1);
			}));

		setItem(11, new UpgradeableItem(
			Material.BOOK,
			this,
			ChatColor.GREEN + "Upgrade speed",
			"makes your auto pickaxe hit even faster!",
			AutoPickaxe::getSpeed,
			pick.getSpeedLevel(),
			20,
			2,
			2).onBuy(() -> {
				pick.addSpeedLevel(1);
			}));

		setItem(12, new UpgradeableItem(
			Material.BOOK,
			this,
			ChatColor.GREEN + "Upgrade range",
			"makes your auto pickaxe hit from a larger distance!",
			AutoPickaxe::getRange,
			pick.getRangeLevel(),
			-1,
			1.8,
			50).onBuy(() -> {
				pick.addRangeLevel(1);
			}));
	}

}
