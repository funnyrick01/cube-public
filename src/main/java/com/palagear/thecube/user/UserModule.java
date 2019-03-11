package com.palagear.thecube.user;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.entity.Player;

import me.palagear.core.user.User;
import me.palagear.core.user.UserFactory;

public class UserModule implements UserFactory {
	
	public UserModule() {
		me.palagear.core.user.UserModule.setUserFactory(this);
	}
	
	@Override
	public CubeUser create(Player p) {
		return new CubeUser(p);
	}
	
	public static CubeUser getUser(Player player) {
		if(!player.isOnline()) {
			return null;
		}
		User u = me.palagear.core.user.UserModule.getUser(player);
		
		if(u instanceof CubeUser) {
			return (CubeUser) u;
		} else {
			return null;
		}
 	}
	
	/**
 	 * gets all registered users
 	 * 
 	 * @return all registered users
 	 */
	public static Set<CubeUser> getCubeUsers() {
		Set<CubeUser> users = new HashSet<CubeUser>();
		for(User user : me.palagear.core.user.UserModule.getUsers()) {
			if(user instanceof CubeUser) {
				if(!user.isOnline()) {
					user.save();
					me.palagear.core.user.UserModule.removeUser(user.getUniqueId());
				} else {
					users.add((CubeUser)user);
				}
			}
		}
		return users;
	}

	public static void reload() {
		me.palagear.core.user.UserModule.reload();
	}
	
}
