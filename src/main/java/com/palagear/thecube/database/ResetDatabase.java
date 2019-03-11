package com.palagear.thecube.database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import me.palagear.core.database.DatabaseConnection;

public class ResetDatabase {

	/**
	 * This resets the entire database to it's original state.
	 * @param args
	 */
	public static void main(String[] args) {
		
		new DatabaseModule();
		
		DatabaseConnection database = DatabaseModule.getInstance().getDatabase();
		
		try {
			Connection conn = database.openConnection();
			
			PreparedStatement stmt = conn.prepareStatement(getQuery());
			
			stmt.execute();
			
			System.out.println("Database reset!");
			
			database.closeConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Get the reset query.
	 * @return The reset query.
	 */
	private static String getQuery() {
		StringBuilder query = new StringBuilder();
	    try{
	        
	        BufferedReader bufferedReader = new BufferedReader(
	                                        new FileReader(new File("").getAbsolutePath() + "\\src\\me\\group3\\StorageRobot\\database\\queries\\Reset")
	                                                            );
	        String line = bufferedReader.readLine();
	        while(line != null) {
	        	
	             query.append(line).append("\n");
	             line = bufferedReader.readLine();
	        }
	        bufferedReader.close();
	    }
	    catch (IOException e){
	        e.printStackTrace();
	    }
	    
	    return query.toString();
	}
	
}
