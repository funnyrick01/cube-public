package com.palagear.thecube;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.palagear.thecube.database.DatabaseModule;

import static org.hamcrest.Matchers.*;

public class CubeTest extends TestBase {
	
	public static final int cubeID = 2;

	@Before
	public void setUp() {
		new me.palagear.core.database.DatabaseModule("localhost", "3306", "network_core", "root", "");
		new DatabaseModule("localhost", "3306", "network_thecube", "root", "");
		
		Cube.load(cubeID, false);
	}
	
	@Test
	public void testLoadCenterLocation() {
		double expected = 128;
		double result = Cube.getCenter().getY();
		
		assertThat(expected, is(result));
	}
	
	@Test 
	public void testLoadID() {
		assertThat(Cube.getID(), is(cubeID));
	}
	
	@Test
	public void testLoadMaxSize() {
		assertThat(Cube.getMaxSize(), lessThanOrEqualTo(256 / 2));
	}
}
