package com.palagear.thecube.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

import com.palagear.thecube.Main;
import com.palagear.thecube.user.CubeUser;
import com.palagear.thecube.user.UserModule;

public class AutoPickaxeListener implements Listener {
	
	public AutoPickaxeListener() {
		Main.getInstance().getServer().getPluginManager().registerEvents(this, Main.getInstance());
	}

	@EventHandler
	public void ItemSwitch(PlayerItemHeldEvent event) {
		
		CubeUser user = UserModule.getUser(event.getPlayer());
		
		if(user != null) {
			ItemStack item = event.getPlayer().getInventory().getItem(event.getNewSlot());
			
			if(item != null && item.getType().equals(user.getAutoPickaxe().getMaterial())) {
				user.getAutoPickaxe().activate();
			} else {
				user.getAutoPickaxe().deactivate();
			}
		}
		
	}
	
}
