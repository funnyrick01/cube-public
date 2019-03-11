package com.palagear.thecube.database.tableContainers;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class CubeContainer {
	private final int ID;
	private final Timestamp createdAt;
	private final Timestamp destroyedAt;
}
