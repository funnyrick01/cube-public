package com.palagear.thecube.util;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class MultiEntityStructure {
	
	private Location location;
	private HashMap<ArmorStand, Vector> armorStands = new HashMap<>();
	
	public MultiEntityStructure(Location location) {
		this.location = location;
	}
	
	public void addArmorStandRelative(ArmorStand armorStand, Vector offset) {
		armorStands.put(armorStand, offset);
	}
	
	public void addBlock(Material material, Vector offset) {
		addBlock(material, offset, false);
	}
	
	public void addBlock(Material material, Vector offset, boolean isBaby) {
		ArmorStand armorStand = spawnArmorStand(location.clone().add(VectorUtils.rotateVector(offset, this.location)));
		armorStand.setHelmet(new ItemStack(material));
		armorStand.setSmall(isBaby);
		
		// TODO maken zodat de hoofd van de armorstand in de offset locatie is i.p.v. de voeten
		
		armorStands.put(armorStand, offset);
	}
	
	private ArmorStand spawnArmorStand(Location location) {
		ArmorStand armorStand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
		
		armorStand.setVisible(false);
		armorStand.setAI(false);
		armorStand.setGravity(false);
		
		return armorStand;
	}
	
	public void remove() {
		for (ArmorStand armorStand : armorStands.keySet()) {
		    armorStand.remove();
		}
	}
	
	public void teleport(Location location) {
		this.location = location;
		
		for (Entry<ArmorStand, Vector> entry : armorStands.entrySet()) {
			ArmorStand armorStand = entry.getKey();
			Vector offset = entry.getValue();

			armorStand.teleport(location.clone().add(VectorUtils.rotateVector(offset, this.location)));
		}
	}
}
