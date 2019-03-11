/**
 * 
 */
package com.palagear.thecube.user;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.UUID;

import org.junit.Test;

import com.palagear.thecube.TestBase;

import test.users.DummyPlayer;

/**
 * @author Rick
 *
 */
public class CubeUsersTest extends TestBase {

	private final String uuid = "00000000-0000-0000-0000-000000000000";
	
	@Test
	public void testGetHandDamageUpgradeCost() {
		double result = CubeUser.getHandDamageUpgradeCost(5);

		assertThat(result, is(28.56));
	}
	
	@Test
	public void testGetHandDamage() {
		double result = CubeUser.getHandDamage(5);
		
		assertThat(result, is(2.07));
	}
	
	@Test
	public void testGetSpreadDamage() {
		double result = CubeUser.getSpreadDamage(5);

		assertThat(result, is(5d));
	}
	
	@Test
	public void testGetCritChance() {
		double result = CubeUser.getCritChance(5);

		assertThat(result, is(9d));
	}
	
	@Test
	public void testGetCritDamage() {
		double result = CubeUser.getCritDamage(5);

		assertThat(result, is(31d));
	}
	
	@Test
	public void testAddTickets() {
		DummyPlayer player = new DummyPlayer();
		player.setUuid(UUID.fromString(uuid));
		
		CubeUser user = new CubeUser(player);
		user.setTickets(400);
		user.addTickets(5);

		assertThat(user.getTickets(), is(405d));
	}
	
	@Test 
	public void testReduceTickets() {
		DummyPlayer player = new DummyPlayer();
		player.setUuid(UUID.fromString(uuid));
		
		CubeUser user = new CubeUser(player);
		user.setTickets(400);
		boolean result = user.reduceTickets(5000);

		assertThat(result, is(false));
		assertThat(user.getTickets(), is(400d));
	}
}
