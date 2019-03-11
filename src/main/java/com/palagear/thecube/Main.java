package com.palagear.thecube;

import com.palagear.thecube.Listeners.AutoPickaxeListener;
import com.palagear.thecube.Listeners.playerListener;
import com.palagear.thecube.commands.CommandModule;
import com.palagear.thecube.database.DatabaseModule;
import com.palagear.thecube.user.UserModule;

import lombok.Getter;
import me.palagear.core.Core;
import me.palagear.core.config.FallbackConfigModule;

public class Main extends Core {

	@Getter private static Main instance;

	@Override
	public void onEnable() {
		instance = this;

		new FallbackConfigModule();
		new DatabaseModule();
		Cube.load();
		new UserModule();
		
		super.onEnable();

		new playerListener();
		new AutoPickaxeListener();
		new CommandModule();
	}

	@Override
	public void onDisable() {
		super.onDisable();
		
		UserModule.getCubeUsers().forEach(user -> user.save());
	}
}
