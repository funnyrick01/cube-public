package com.palagear.thecube.gadgets;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;

import com.palagear.thecube.user.CubeUser;

import lombok.Getter;
import me.palagear.core.gui.GuiItem;

public enum Gadget {
	pacman(GadgetRarity.uncommun),
	nuke(GadgetRarity.legendary),
	bomb(GadgetRarity.common),
	infection(GadgetRarity.uncommun),
	storm(GadgetRarity.rare),
	ticketBooster(GadgetRarity.legendary);

	@Getter private GadgetRarity rarity;

	private Gadget(GadgetRarity rarity) {
		this.rarity = rarity;
	}

	public GuiItem getAsItem() {

		GuiItem result = null;

		switch(this) {
			case pacman:
				result = new GuiItem(Material.GOLD_BLOCK, ChatColor.BLUE + "Pacman", "lore");
				break;
			case nuke:
				result = new GuiItem(Material.TNT, ChatColor.BLUE + "Nuke", "lore");
				break;
			case bomb:
				result = new GuiItem(Material.FIREBALL, ChatColor.BLUE + "Bomb", "lore");
				break;
			default:
				throw new IllegalArgumentException("Unable to get item of " + this);
		}

		return result;
	}

	public void use(CubeUser user, Location location) {
		switch(this) {
			case pacman:
				new Pacman(user, location);
				break;
			case nuke:
				new Nuke(user, location);
				break;
			case bomb:
				new Bomb(user, location);
				break;
			default:
				throw new IllegalArgumentException("Unable to use " + this);
		}
	}
}
