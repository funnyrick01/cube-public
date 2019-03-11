package com.palagear.thecube.scoreboard;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.palagear.thecube.Cube;
import com.palagear.thecube.user.CubeUser;

import me.palagear.core.util.ScoreBoardAPI;
import me.palagear.core.util.ScoreBoardAPI.Entry;

public class StatsScoreboard {
	
	private Player player;
	
	public StatsScoreboard(CubeUser user) {
		
		this.player = user.getPlayer();
		
		if(!ScoreBoardAPI.hasScoreboard(player)) {
			Map<Entry, String> values = new HashMap<ScoreBoardAPI.Entry, String>();
			
			values.put(Entry.SCORE1,  ChatColor.GOLD + "Current cube ");
			values.put(Entry.SCORE2,  ChatColor.YELLOW + "Layer progress: " + ChatColor.WHITE + "0%");
			values.put(Entry.SCORE3,  ChatColor.YELLOW + "Online players: " + ChatColor.WHITE + "0");
			values.put(Entry.SCORE4,  " ");
			values.put(Entry.SCORE5,  ChatColor.BOLD + "" + ChatColor.DARK_RED + "You " + ChatColor.RESET + ChatColor.GRAY + "(" + player.getName() + ")");
			values.put(Entry.SCORE6,  ChatColor.LIGHT_PURPLE + "Tickets: " + ChatColor.WHITE + "0");
			values.put(Entry.SCORE7,  ChatColor.LIGHT_PURPLE + "Tokens: " + ChatColor.WHITE + "0");
			values.put(Entry.SCORE8,  "  ");
			values.put(Entry.SCORE9,  ChatColor.BOLD + "" + ChatColor.DARK_AQUA + "Global");
			values.put(Entry.SCORE10,  ChatColor.AQUA + "No boosters active");
			values.put(Entry.SCORE11, "   ");
			values.put(Entry.SCORE12, ChatColor.BOLD + "" + ChatColor.GREEN + "play.palagear.net");
			
			ScoreBoardAPI.scoreboard(player, ChatColor.RED + "The Cube", values);
		}
		setLayerProgress(Cube.getCurrentLayer().getProgress());
		setTickets(user.getTickets());
		setTokens(user.getTokens());
	}
	
	public void setLayerProgress(double progress) {
		progress = Math.round(progress * 100) / 100d;
		ScoreBoardAPI.updateScore(player, Entry.SCORE2,  ChatColor.YELLOW + "Layer progress: " + ChatColor.WHITE + progress + "%");
	}
	
	public void setOnlinePlayers(int players) {
		ScoreBoardAPI.updateScore(player, Entry.SCORE3,  ChatColor.YELLOW + "Online players: " + ChatColor.WHITE + players);
	}
	
	public void setTickets(double tickets) {
		tickets = Math.round(tickets * 100) / 100d;
		ScoreBoardAPI.updateScore(player, Entry.SCORE6,  ChatColor.LIGHT_PURPLE + "Tickets: " + ChatColor.WHITE + tickets);
	}
	
	public void setTokens(double tokens) {
		tokens = Math.round(tokens * 100) / 100d;
		ScoreBoardAPI.updateScore(player, Entry.SCORE7,  ChatColor.LIGHT_PURPLE + "Tokens: " + ChatColor.WHITE + tokens);
	}
	
	public void setBooster(String booster) {
		if(booster.equals(null)) {
			ScoreBoardAPI.updateScore(player, Entry.SCORE10,  ChatColor.AQUA + "No boosters active");
		} else {
			ScoreBoardAPI.updateScore(player, Entry.SCORE10,  ChatColor.AQUA + booster);
		}
	}
	
}
