package de.at.manager;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import de.at.main.Main;
import de.at.utils.FileConfig;

public class TablistProcesser {

	public static List<String> header;
	public static List<String> footer;
	public static int interval = 20;
	public static BukkitTask task;
	
	public static int size1 = 0;
	public static int size2 = 0;
	
	public TablistProcesser() {
		header = new ArrayList<String>();
		footer = new ArrayList<String>();
		
		FileConfig anim = new FileConfig("AnimatedTab", "tablist.yml");
		
		if(anim.contains("Header")) {
			if(anim.getStringList("Header") != null) {
				header = anim.getStringList("Header");
			}
			else {
				header.add("&7&oWelcome &a&o{player}");
				header.add("&7&oWelcome &2&o{player}");
				anim.set("Header", header);
				anim.SaveConfig();
			}
		}
		else {
			header.add("&7&oWelcome &a&o{player}");
			header.add("&7&oWelcome &2&o{player}");
			anim.set("Header", header);
			anim.SaveConfig();
		}
		
		if(anim.contains("Footer")) {
			if(anim.getStringList("Footer") != null) {
				footer = anim.getStringList("Footer");
			}
			else {
				footer.add("&7&oYour Level: &e{level} \n&7&oYour XP: &6{xp}");
				footer.add("&7&oYour Level: &6{level} \n&7&oYour XP: &e{xp}");
				footer.add("&7&oYour Level: &d{level} \n&7&oYour XP: &b{xp}");
				footer.add("&7&oYour Level: &b{level} \n&7&oYour XP: &d{xp}");
				anim.set("Footer", footer);
				anim.SaveConfig();
			}
		}
		else {
			footer.add("&7&oYour Level: &e{level} \n&7&oYour XP: &6{xp}");
			footer.add("&7&oYour Level: &6{level} \n&7&oYour XP: &e{xp}");
			footer.add("&7&oYour Level: &d{level} \n&7&oYour XP: &b{xp}");
			footer.add("&7&oYour Level: &b{level} \n&7&oYour XP: &d{xp}");
			anim.set("Footer", footer);
			anim.SaveConfig();
		}
		
		if(anim.contains("Interval")) {
			interval = anim.getInt("Interval");
		}
		else {
			anim.set("Interval", 20);
			anim.SaveConfig();
		}
		
		size1 = header.size() - 1;
		size2 = footer.size() - 1;
		
		// Start Task
		start();
	}
	
	
	
	
	public void start() {
		task = Bukkit.getScheduler().runTaskTimerAsynchronously(Main.INSTANCE, new Runnable() {

			int i1 = 0;
			int i2 = 0;
			
			@Override
			public void run() {
				
				for(Player p : Bukkit.getOnlinePlayers()) {
					try {
						Tablist.sendTablist(p, header.get(i1), footer.get(i2));
					} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
							| InvocationTargetException | NoSuchMethodException | SecurityException
							| ClassNotFoundException | NoSuchFieldException e) {
					}
				}
				
				if(i1 < size1) {
					i1++;
				}
				else {
					i1 = 0;
				}
				
				if(i2 < size2) {
					i2++;
				}
				else {
					i2 = 0;
				}
			}
		}, interval, interval);
	}
	
	public void reload() {
		task.cancel();
		Main.processor = new TablistProcesser();
	}
	
}
