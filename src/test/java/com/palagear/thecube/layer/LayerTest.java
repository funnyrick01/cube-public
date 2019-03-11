package com.palagear.thecube.layer;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.bukkit.Location;
import org.junit.Before;
import org.junit.Test;

import com.palagear.thecube.Cube;
import com.palagear.thecube.CubeTest;
import com.palagear.thecube.TestBase;

public class LayerTest extends TestBase {
	
	@Before
	public void setUp() {
		loadDatabase();
		Cube.load(CubeTest.cubeID, false);
	}

	@Test
	public void testIsInLayer_isTrue() {
		Layer layer = new Layer(20, false);
		Location location = Cube.getCenter();
		
		boolean result = layer.isInLayer(location.clone().add(0, 20, 0));

		assertThat(result, is(true));
	}
	
	@Test
	public void testIsInLayer_isFalse() {
		Layer layer = new Layer(20, false);
		Location location = Cube.getCenter();
		
		boolean result = layer.isInLayer(location.clone().add(0, 21, 0));

		assertThat(result, is(false));
	}
	
	@Test
	public void testIsInLayer_isFalse2() {
		Layer layer = new Layer(20, false);
		Location location = Cube.getCenter();
		
		boolean result = layer.isInLayer(location.clone().add(0, 19, 0));
		
		assertThat(result, is(false));
	}
	
	@Test
	public void testIsInsideLayer_isTrue() {
		Layer layer = new Layer(20, false);
		Location location = Cube.getCenter();
		
		boolean result = layer.isInsideLayer(location.clone().add(0, 19, 0));
		
		assertThat(result, is(true));
	}

	@Test
	public void testIsInsideLayer_isFalse() {
		Layer layer = new Layer(20, false);
		Location location = Cube.getCenter();
		
		boolean result = layer.isInsideLayer(location.clone().add(0, 20, 0));
		
		assertThat(result, is(false));
	}
	
	@Test
	public void testIsOutsideLayer_isTrue() {
		Layer layer = new Layer(20, false);
		Location location = Cube.getCenter();
		
		boolean result = layer.isOutsideLayer(location.clone().add(0, 21, 0));
		
		assertThat(result, is(true));
	}
	
	@Test
	public void testIsOutsideLayer_isfalse() {
		Layer layer = new Layer(20, false);
		Location location = Cube.getCenter();
		
		boolean result = layer.isOutsideLayer(location.clone().add(0, 20, 0));
		
		assertThat(result, is(false));
	}
	
	@Test
	public void testIsInOrOutsideLayer_isTrue() {
		Layer layer = new Layer(20, false);
		Location location = Cube.getCenter();
		
		boolean result = layer.isInOrOutsideLayer(location.clone().add(0, 20, 0)) && layer.isInOrOutsideLayer(location.clone().add(0, 21, 0));
		
		assertThat(result, is(true));
	}
	
	@Test
	public void testIsInOrOutsideLayer_isFalse() {
		Layer layer = new Layer(20, false);
		Location location = Cube.getCenter();
		
		boolean result = layer.isInOrOutsideLayer(location.clone().add(0, 19, 0)) && layer.isInOrOutsideLayer(location.clone().add(0, 21, 0));
		
		assertThat(result, is(false));
	}

	@Test
	public void testIsInOrInsideLayer_isTrue() {
		Layer layer = new Layer(20, false);
		Location location = Cube.getCenter();
		
		boolean result = layer.isInOrInsideLayer(location.clone().add(0, 20, 0)) && layer.isInOrInsideLayer(location.clone().add(0, 19, 0));
		
		assertThat(result, is(true));
	}
	
	@Test
	public void testIsInOrInsideLayer_isFalse() {
		Layer layer = new Layer(20, false);
		Location location = Cube.getCenter();
		
		boolean result = layer.isInOrInsideLayer(location.clone().add(0, 20, 0)) && layer.isInOrInsideLayer(location.clone().add(0, 21, 0));
		
		assertThat(result, is(false));
	}
}
