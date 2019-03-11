package com.palagear.thecube.database.queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.palagear.thecube.database.DatabaseModule;
import com.palagear.thecube.database.tableContainers.CubeContainer;

import me.palagear.core.database.DatabaseConnection;

public class Cube {

	public static CubeContainer getCurrentCube() {

		DatabaseConnection database = DatabaseModule.getInstance().getDatabase();

		CubeContainer result = null;

		if(database.canConnect()) {
			try {
				Connection conn = database.openConnection();

				PreparedStatement stmt = conn.prepareStatement(
					"SELECT * "
						+ "FROM cube "
						+ "WHERE ID = (SELECT MAX(ID) FROM cube)");

				ResultSet queryResult = stmt.executeQuery();

				while(queryResult.next()) {
					result = new CubeContainer(
						queryResult.getInt("ID"),
						queryResult.getTimestamp("createdAt"),
						queryResult.getTimestamp("destroyedAt"));
				}

				database.closeConnection();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public static CubeContainer getCube(int ID) {

		DatabaseConnection database = DatabaseModule.getInstance().getDatabase();

		CubeContainer result = null;

		if(database.canConnect()) {
			try {
				Connection conn = database.openConnection();

				PreparedStatement stmt = conn.prepareStatement(
					"SELECT * "
						+ "FROM cube "
						+ "WHERE ID = ?");

				stmt.setInt(1, ID);
				
				ResultSet queryResult = stmt.executeQuery();

				while(queryResult.next()) {
					result = new CubeContainer(
						queryResult.getInt("ID"),
						queryResult.getTimestamp("createdAt"),
						queryResult.getTimestamp("destroyedAt"));
				}

				database.closeConnection();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public static CubeContainer createNewCube() {
		DatabaseConnection database = DatabaseModule.getInstance().getDatabase();

		if(database.canConnect()) {
			try {
				Connection conn = database.openConnection();

				PreparedStatement stmt = conn.prepareStatement("INSERT INTO cube () VALUES()");

				stmt.executeUpdate();

				database.closeConnection();

				return getCurrentCube();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}

		return null;
	}

}
