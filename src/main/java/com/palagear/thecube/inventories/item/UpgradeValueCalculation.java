package com.palagear.thecube.inventories.item;

@FunctionalInterface
public interface UpgradeValueCalculation {
	double calculate(int level);
}
