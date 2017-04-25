package de.at.main;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import de.at.commands.AnimatedTab;
import de.at.manager.TablistProcesser;


public class Main extends JavaPlugin {

	public static Main INSTANCE;
	public static String PREFIX = "§6§l┃ §eAnimatedTab §7§o";
	public static TablistProcesser processor;
	
	@Override
	public void onEnable() {
		INSTANCE = this;
		
		processor = new TablistProcesser();
		Bukkit.getPluginCommand("animatedtab").setExecutor(new AnimatedTab());

		Bukkit.getConsoleSender().sendMessage(PREFIX + "§a§oPlugin loaded.");
	}
	
	
	@Override
	public void onDisable() {
		Bukkit.getConsoleSender().sendMessage(PREFIX + "§c§oPlugin unloaded.");
	}
	
	
}
