package com.palagear.thecube.database.queries;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.palagear.thecube.Cube;
import com.palagear.thecube.database.DatabaseModule;
import com.palagear.thecube.database.tableContainers.CubeUserContainer;
import com.palagear.thecube.database.tableContainers.UserContainer;
import com.palagear.thecube.user.CubeUser;

import test.users.DummyPlayer;

public class UsersTest {

	private final UUID nonExistingUuid = UUID.fromString("00000000-0000-0000-0000-000000000001");

	@SuppressWarnings("unused") private final String username = "UnitTest";
	private final UUID uuid = UUID.fromString("00000000-0000-0000-0000-000000000000");
	private final int exp = 8923;
	private final double tokens = 287;

	private final int cubeID = 2;
	private final double tickets = 1234;
	private final long timePlayed = 23456;
	private final int blocksBroken = 217536;
	private final int handDamageLevel = 7;
	private final int spreadDamageLevel = 25;
	private final int critChanceLevel = 6;
	private final int critDamageLevel = 9;
	private final int pickaxeLevel = 2;
	private final int pickaxeDamageLevel = 13;
	private final int pickaxeSpeedLevel = 15;
	private final int pickaxeRangeLevel = 14;

	private final int pacman = 4;
	private final int nuke = 6;
	private final int bomb = 0;
	private final int infection = 3;
	private final int storm = 1;
	private final int ticketBooster = 2;

	@Before
	public void setUp() {
		new me.palagear.core.database.DatabaseModule("localhost", "3306", "network_core", "root", "");
		new DatabaseModule("localhost", "3306", "network_thecube", "root", "");

		Cube.load(cubeID, false);

		Users.saveUser(new UserContainer(
			uuid.toString(),
			exp,
			tokens));

		Users.saveCubeUser(new CubeUserContainer(
			cubeID,
			uuid.toString(),
			tickets,
			timePlayed,
			blocksBroken,
			handDamageLevel,
			spreadDamageLevel,
			critChanceLevel,
			critDamageLevel,
			pickaxeLevel,
			pickaxeDamageLevel,
			pickaxeSpeedLevel,
			pickaxeRangeLevel,
			pacman,
			nuke,
			bomb,
			infection,
			storm,
			ticketBooster));
	}

	@After
	public void breakDown() {
		Users.removeUser(nonExistingUuid);
		Users.removeCubeUser(nonExistingUuid, 0);
	}

	@Test
	public void testSaveUserData() {
		DummyPlayer player = new DummyPlayer();
		player.setUuid(uuid);

		CubeUser user = new CubeUser(player);
		user.setTokens(tokens + 10);
		user.setBlocksBroken(blocksBroken + 20);

		user.save();
		user.loadCubeUser(false);

		assertThat(user.getTokens(), is(tokens + 10));
		assertThat(user.getBlocksBroken(), is(blocksBroken + 20));

		user.setTokens(tokens);
		user.setBlocksBroken(blocksBroken);

		user.save();
		user.loadCubeUser(false);

		assertThat(user.getTokens(), is(tokens));
		assertThat(user.getBlocksBroken(), is(blocksBroken));
	}

	@Test
	public void testGetUser() {
		UserContainer data = Users.getUser(uuid);

		assertThat(data.getUUID(), is(uuid.toString()));
		assertThat(data.getExp(), is(exp));
		assertThat(data.getTokens(), is(tokens));
	}

	@Test
	public void testGetUserNotExisting() {
		UserContainer data = Users.getUser(UUID.fromString("00000000-0000-0000-0000-000000000002"));

		assertThat(data, is(nullValue()));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetUserInvalidUuid() {
		Users.getUser(null);
	}

	@Test
	public void testGetCubeUser() {
		CubeUserContainer data = Users.getCubeUser(uuid, cubeID);

		assertThat(data.getUUID(), is(uuid.toString()));
		assertThat(data.getBlocksBroken(), is(blocksBroken));
		assertThat(data.getTickets(), is(tickets));
	}

	@Test
	public void testGetCubeUserNotExisting() {
		UserContainer data = Users.getUser(UUID.fromString("00000000-0000-0000-0000-000000000002"));

		assertThat(data, is(nullValue()));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetCubeUserInvalidUuid() {
		Users.getUser(null);
	}

	@Test
	public void testGetCubeUserToManyAttempts() {
		CubeUserContainer data = Users.getCubeUser(uuid, 10);

		assertThat(data, is(nullValue()));
	}

	@Test
	public void testAddUser() {
		int result = Users.addUser(nonExistingUuid);
		int result2 = Users.addCubeUser(nonExistingUuid, cubeID);

		assertThat(result, is(1));
		assertThat(result2, is(1));

		Users.removeUser(nonExistingUuid);
		Users.removeCubeUser(nonExistingUuid, cubeID);
	}
}
