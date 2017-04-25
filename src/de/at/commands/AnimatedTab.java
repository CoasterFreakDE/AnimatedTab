package de.at.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.at.main.Main;

public class AnimatedTab implements CommandExecutor {

	
	@Override
	public boolean onCommand(CommandSender s, Command cmd, String string, String[] args) {
		
		if(s instanceof Player) {
			if(!((Player)s).isOp()) {
				s.sendMessage(Main.PREFIX + "§7§oPlugin by F1b3r");
				return true;
			}
		}
		
		if(args.length == 1) {
			String sub = args[0];
			
			if(sub.equalsIgnoreCase("rl") || sub.equalsIgnoreCase("reload")) {
				Main.processor.reload();
				s.sendMessage(Main.PREFIX + "§a§oReload complete!");
				s.sendMessage(Main.PREFIX + "§7§oPlugin by F1b3r");
			}
			else {
				s.sendMessage(Main.PREFIX + "§d§oUse /at reload");
				s.sendMessage(Main.PREFIX + "§7§oPlugin by F1b3r");
			}
		}
		else {
			s.sendMessage(Main.PREFIX + "§d§oUse /at reload");
			s.sendMessage(Main.PREFIX + "§7§oPlugin by F1b3r");
		}
		
		return true;
	}
	
}
