package com.palagear.thecube.inventories;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import com.palagear.thecube.gadgets.Gadget;
import com.palagear.thecube.user.CubeUser;

import me.palagear.core.gui.GUIHolder;
import me.palagear.core.gui.GuiItem;
import me.palagear.core.gui.GuiMenu;

public class Gadgets extends GuiMenu {

	private final CubeUser user;

	public Gadgets(CubeUser user) {
		super(user.getPlayer(), new GUIHolder(), 27);

		this.user = user;

		reBuild();
	}

	@Override
	public void reBuild() {
		setItem(
			10,
			new GuiItem(
				Material.GOLD_BLOCK,
				ChatColor.BLUE + "Pacman (" + ChatColor.GREEN + user.getGadgets().get(Gadget.pacman) + ChatColor.BLUE + ")",
				"Some lore for the pacman").onClick((args) -> {
					user.setCurrentGadget(Gadget.pacman);
				}));
		
		setItem(
			11,
			new GuiItem(
				Material.FIREBALL,
				ChatColor.BLUE + "Bomb (" + ChatColor.GREEN + user.getGadgets().get(Gadget.bomb) + ChatColor.BLUE + ")",
				"Some lore for the bomb").onClick((args) -> {
					user.setCurrentGadget(Gadget.bomb);
				}));
		
		setItem(
			12,
			new GuiItem(
				Material.TNT,
				ChatColor.BLUE + "Nuke (" + ChatColor.GREEN + user.getGadgets().get(Gadget.nuke) + ChatColor.BLUE + ")",
				"Some lore for the nuke").onClick((args) -> {
					user.setCurrentGadget(Gadget.nuke);
				}));
	}

}
