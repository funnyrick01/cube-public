package com.palagear.thecube.database.queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

import com.palagear.thecube.database.DatabaseModule;
import com.palagear.thecube.database.tableContainers.CubeUserContainer;
import com.palagear.thecube.database.tableContainers.UserContainer;

import me.palagear.core.database.DatabaseConnection;

public class Users {

	public static UserContainer getUser(UUID uuid) {
		if(uuid == null) {
			throw new IllegalArgumentException("UUID may not be NULL");
		}

		DatabaseConnection database = DatabaseModule.getInstance().getDatabase();

		UserContainer result = null;

		if(database.canConnect()) {
			try {
				Connection conn = database.openConnection();
				PreparedStatement stmt = conn.prepareStatement("SELECT * "
					+ "FROM users "
					+ "WHERE UUID = ?");

				stmt.setString(1, uuid.toString());

				ResultSet queryResult = stmt.executeQuery();

				while(queryResult.next()) {
					result = new UserContainer(
						queryResult.getString("UUID"),
						queryResult.getInt("exp"),
						queryResult.getDouble("tokens"));
				}
				database.closeConnection();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public static CubeUserContainer getCubeUser(UUID uuid, int cubeID) {
		if(uuid == null) {
			throw new IllegalArgumentException("UUID may not be NULL");
		}

		DatabaseConnection database = DatabaseModule.getInstance().getDatabase();

		CubeUserContainer result = null;

		if(database.canConnect()) {
			try {
				Connection conn = database.openConnection();
				PreparedStatement stmt = conn.prepareStatement("SELECT * "
					+ "FROM cube_users "
					+ "WHERE UUID = ? "
					+ "AND cubeID = ?");

				stmt.setString(1, uuid.toString());
				stmt.setInt(2, cubeID);

				ResultSet queryResult = stmt.executeQuery();

				while(queryResult.next()) {
					result = new CubeUserContainer(
						queryResult.getInt("cubeID"),
						queryResult.getString("UUID"),
						queryResult.getDouble("tickets"),
						queryResult.getLong("timePlayed"),
						queryResult.getInt("blocksBroken"),
						queryResult.getInt("handDamageLevel"),
						queryResult.getInt("spreadDamageLevel"),
						queryResult.getInt("critChanceLevel"),
						queryResult.getInt("critDamageLevel"),
						queryResult.getInt("pickaxeLevel"),
						queryResult.getInt("pickaxeDamageLevel"),
						queryResult.getInt("pickaxeSpeedLevel"),
						queryResult.getInt("pickaxeRangeLevel"),
						queryResult.getInt("gadget_pacman"),
						queryResult.getInt("gadget_nuke"),
						queryResult.getInt("gadget_bomb"),
						queryResult.getInt("gadget_infection"),
						queryResult.getInt("gadget_storm"),
						queryResult.getInt("gadget_ticketBooster"));
				}
				database.closeConnection();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * Add a new user to the database
	 * 
	 * @param uuid
	 */
	public static int addUser(UUID uuid) {

		int result = 0;

		DatabaseConnection database = DatabaseModule.getInstance().getDatabase();

		if(database.canConnect()) {
			try {
				Connection conn = database.openConnection();

				PreparedStatement stmt = conn.prepareStatement("INSERT INTO users (UUID) VALUES(?)");

				stmt.setString(1, uuid.toString());

				result = stmt.executeUpdate();

				database.closeConnection();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}

		return result;
	}

	public static int addCubeUser(UUID uuid, int cubeID) {

		int result = 0;

		DatabaseConnection database = DatabaseModule.getInstance().getDatabase();

		if(database.canConnect()) {
			try {
				Connection conn = database.openConnection();

				PreparedStatement stmt = conn.prepareStatement("INSERT INTO cube_users (cubeID, UUID) VALUES(?, ?)");

				stmt.setInt(1, cubeID);
				stmt.setString(2, uuid.toString());

				result = stmt.executeUpdate();

				database.closeConnection();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * Save a user to the database
	 * 
	 * @param uuid
	 */
	public static void saveUser(UserContainer data) {

		DatabaseConnection database = DatabaseModule.getInstance().getDatabase();

		if(database.canConnect()) {
			try {
				Connection conn = database.openConnection();

				PreparedStatement stmt = conn.prepareStatement(
					"UPDATE users SET "
						+ "exp=?, "
						+ "tokens=? "
						+ "WHERE UUID = ?");

				stmt.setInt(1, data.getExp());
				stmt.setDouble(2, data.getTokens());

				stmt.setString(3, data.getUUID());

				stmt.executeUpdate();

				database.closeConnection();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void saveCubeUser(CubeUserContainer data) {

		DatabaseConnection database = DatabaseModule.getInstance().getDatabase();

		if(database.canConnect()) {
			try {
				Connection conn = database.openConnection();

				PreparedStatement stmt = conn.prepareStatement(
					"UPDATE cube_users SET "
						+ "tickets=?, "
						+ "timePlayed=?, "
						+ "blocksBroken=?, "
						+ "handDamageLevel=?, "
						+ "spreadDamageLevel=?, "
						+ "critChanceLevel=?, "
						+ "critDamageLevel=?, "
						+ "pickaxeLevel=?, "
						+ "pickaxeSpeedLevel=?, "
						+ "pickaxeDamageLevel=?, "
						+ "pickaxeRangeLevel=?, "
						+ "gadget_pacman=?, "
						+ "gadget_nuke=?, "
						+ "gadget_bomb=?, "
						+ "gadget_infection=?, "
						+ "gadget_storm=?, "
						+ "gadget_ticketBooster=? "
						+ "WHERE UUID = ? "
						+ "AND cubeID = ?");

				int i = 1;

				stmt.setDouble(i++, data.getTickets());
				stmt.setLong(i++, data.getTimePlayed());
				stmt.setInt(i++, data.getBlocksBroken());
				stmt.setInt(i++, data.getHandDamageLevel());
				stmt.setInt(i++, data.getSpreadDamageLevel());
				stmt.setInt(i++, data.getCritChanceLevel());
				stmt.setInt(i++, data.getCritDamageLevel());
				stmt.setInt(i++, data.getPickaxeLevel());
				stmt.setInt(i++, data.getPickaxeDamageLevel());
				stmt.setInt(i++, data.getPickaxeSpeedLevel());
				stmt.setInt(i++, data.getPickaxeRangeLevel());
				stmt.setInt(i++, data.getGadget_pacman());
				stmt.setInt(i++, data.getGadget_nuke());
				stmt.setInt(i++, data.getGadget_bomb());
				stmt.setInt(i++, data.getGadget_infection());
				stmt.setInt(i++, data.getGadget_storm());
				stmt.setInt(i++, data.getGadget_ticketBooster());

				stmt.setString(i++, data.getUUID());
				stmt.setInt(i++, data.getCubeID());

				stmt.executeUpdate();

				database.closeConnection();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void removeUser(UUID uuid) {
		if(uuid == null) {
			throw new IllegalArgumentException("UUID may not be NULL");
		}

		DatabaseConnection database = DatabaseModule.getInstance().getDatabase();

		if(database.canConnect()) {
			try {
				Connection conn = database.openConnection();

				PreparedStatement stmt = conn.prepareStatement(
					"DELETE FROM users "
						+ "WHERE UUID = ?");

				stmt.setString(1, uuid.toString());

				stmt.executeUpdate();

				database.closeConnection();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void removeCubeUser(UUID uuid, int cubeID) {
		if(uuid == null) {
			throw new IllegalArgumentException("UUID may not be NULL");
		}

		DatabaseConnection database = DatabaseModule.getInstance().getDatabase();

		if(database.canConnect()) {
			try {
				Connection conn = database.openConnection();

				PreparedStatement stmt = conn.prepareStatement(
					"DELETE FROM cube_users "
						+ "WHERE cubeID = ? "
						+ "AND UUID = ?");

				stmt.setInt(1, cubeID);
				stmt.setString(2, uuid.toString());

				stmt.executeUpdate();

				database.closeConnection();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

}
