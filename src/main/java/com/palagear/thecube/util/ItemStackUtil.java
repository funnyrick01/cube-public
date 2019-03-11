package com.palagear.thecube.util;

public class ItemStackUtil {

	public static int getAllowedItemstackAmount(int amount) {
		if(amount > 64) {
			return 64;
		} else if(amount < 0) {
			return 0;
		}
		return amount;
	}

}
