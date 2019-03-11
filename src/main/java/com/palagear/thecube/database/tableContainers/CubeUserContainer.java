package com.palagear.thecube.database.tableContainers;

import lombok.Data;
import lombok.ToString;

@Data @ToString
public class CubeUserContainer {
	private final int cubeID;
	private final String UUID;
	private final double tickets;
	private final long timePlayed;
	private final int blocksBroken;
	private final int handDamageLevel;
	private final int spreadDamageLevel;
	private final int critChanceLevel;
	private final int critDamageLevel;
	private final int pickaxeLevel;
	private final int pickaxeDamageLevel;
	private final int pickaxeSpeedLevel;
	private final int pickaxeRangeLevel;
	private final int gadget_pacman;
	private final int gadget_nuke;
	private final int gadget_bomb;
	private final int gadget_infection;
	private final int gadget_storm;
	private final int gadget_ticketBooster;
}
