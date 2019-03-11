package com.palagear.thecube.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.palagear.thecube.inventories.Gadgets;
import com.palagear.thecube.user.UserModule;

import me.palagear.core.commands.CommandBase;

public class Test extends CommandBase {
	public Test() {
		super("test");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender.hasPermission("palagear.thecube.test")) {
			try {
				
				Player player = (Player)sender;

				new Gadgets(UserModule.getUser(player)).open();
				
				//new Bomb(UserModule.getUser(player), player.getTargetBlock(null, 50).getLocation());
				
			} catch(Exception e) {
				e.printStackTrace();
			}
			sender.sendMessage("executing test command");
		}
		return true;
	}
}
