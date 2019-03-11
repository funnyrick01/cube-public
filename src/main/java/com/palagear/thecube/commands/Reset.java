package com.palagear.thecube.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.palagear.thecube.Cube;

import me.palagear.core.commands.CommandBase;

public class Reset extends CommandBase {

	public Reset() {
		super("reset");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender.hasPermission("palagear.thecube.reset")) {
			try {
				Cube.reset();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return true;
	}

}
