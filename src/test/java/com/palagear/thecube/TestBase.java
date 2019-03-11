package com.palagear.thecube;

import com.palagear.thecube.database.DatabaseModule;

public class TestBase {
	public static final double fuzz = 0.000001;
	
	public void loadDatabase() {
		new me.palagear.core.database.DatabaseModule("localhost", "3306", "network_core", "root", "");
		new DatabaseModule("localhost", "3306", "network_thecube", "root", "");
	}
}
