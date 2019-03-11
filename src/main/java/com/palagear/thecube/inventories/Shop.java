package com.palagear.thecube.inventories;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.palagear.core.gui.GUIHolder;
import me.palagear.core.gui.GuiItem;
import me.palagear.core.gui.GuiMenu;

public class Shop extends GuiMenu {

	public Shop(Player p) {
		super(p, new GUIHolder(), 27, ChatColor.BLUE + "Shop");

		//setInventory(Bukkit.createInventory(new GUIHolder(), 27, ChatColor.BLUE + "Shop"));
		
		reBuild();
	}

	@Override
	public void reBuild() {
		setItem(10, new GuiItem(Material.TOTEM, ChatColor.BLUE + "Upgrade hand").onClick((args) -> new HandShop(args.getPlayer()).open()));
		setItem(11, new GuiItem(Material.WOOD_PICKAXE, ChatColor.BLUE + "Upgrade auto-pickaxe").onClick((args) -> new AutoPickaxeShop(args.getPlayer()).open()));
	}
}
