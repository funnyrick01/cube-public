package com.palagear.thecube.database;

import me.palagear.core.config.ConfigModule;
import me.palagear.core.database.DatabaseConnection;

public class DatabaseModule {
	private static DatabaseModule instance;
	private DatabaseConnection database;
	
	public DatabaseModule() {
		instance = this;
		this.database = new DatabaseConnection(
				ConfigModule.getInstance().getDatabaseManager().host,
				ConfigModule.getInstance().getDatabaseManager().port, 
				"network_thecube", 
				ConfigModule.getInstance().getDatabaseManager().username, 
				ConfigModule.getInstance().getDatabaseManager().password);
	}
	
	public DatabaseModule(String host, String port, String database, String username, String password) {
		instance = this;
		this.database = new DatabaseConnection(host, port, database, username, password);
	}
	
	public DatabaseConnection getDatabase() {
		return this.database;
	}
	
	public static DatabaseModule getInstance() {
		return instance;
	}
}
