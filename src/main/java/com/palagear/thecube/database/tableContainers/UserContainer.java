package com.palagear.thecube.database.tableContainers;

import lombok.Data;
import lombok.ToString;

@Data @ToString
public class UserContainer {
	private final String UUID;
	private final int exp;
	private final double tokens;
}