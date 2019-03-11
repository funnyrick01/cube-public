package com.palagear.thecube.database;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

import me.palagear.core.database.DatabaseConnection;

public class DatabaseModuleTest {
	
	private DatabaseConnection connection;
	
	@Before
	public void setUp() {
		connection = new DatabaseModule("localhost", "3306", "network_thecube", "root", "").getDatabase();
	}
	
	@Test
	public void testDatabaseCanConnect() {
		assertThat(connection.canConnect(), is(true));
	}
	
	@Test
	public void testDatabaseConnection() throws ClassNotFoundException, SQLException {
		Connection conn = connection.openConnection();
		assertThat(conn.isClosed(), is(false));
		
		conn.close();
		assertThat(conn.isClosed(), is(true));
	}
}
