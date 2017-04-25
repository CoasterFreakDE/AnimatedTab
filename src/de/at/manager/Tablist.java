package de.at.manager;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import me.clip.placeholderapi.PlaceholderAPI;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_8_R3.PlayerConnection;

public class Tablist implements Listener {

	public static Class<?> getNmsClass(String nmsClassName) throws ClassNotFoundException
	{
		return Class.forName("net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + "." + nmsClassName);
	}
			  
	public static String getServerVersion()
	{
		return Bukkit.getServer().getClass().getPackage().getName().substring(23);
	}
	
	@SuppressWarnings("rawtypes")
	public static void sendTablist(Player p, String msg1, String msg2) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException, NoSuchFieldException {
	
		msg1 = convertStrings(p, msg1);
		msg2 = convertStrings(p, msg2);
		
		if ((getServerVersion().equalsIgnoreCase("v1_9_R1")) || 
		        (getServerVersion().equalsIgnoreCase("v1_9_R2")) || 
		        (getServerVersion().equalsIgnoreCase("v1_10_R1")) || 
		        (getServerVersion().equalsIgnoreCase("v1_11_R1")))
	   {
	        Object header = getNmsClass("ChatComponentText").getConstructor(new Class[] { String.class }).newInstance(new Object[] { ChatColor.translateAlternateColorCodes('&', msg1) });
	        Object footer = getNmsClass("ChatComponentText").getConstructor(new Class[] { String.class }).newInstance(new Object[] { ChatColor.translateAlternateColorCodes('&', msg2) });
	        
	        Object ppoplhf = getNmsClass("PacketPlayOutPlayerListHeaderFooter").getConstructor(new Class[] { getNmsClass("IChatBaseComponent") }).newInstance(new Object[] { header });
	        
	        Field f = ppoplhf.getClass().getDeclaredField("b");
	        f.setAccessible(true);
	        f.set(ppoplhf, footer);
	        
	        Object nmsp = p.getClass().getMethod("getHandle", new Class[0]).invoke(p, new Object[0]);
	        Object pcon = nmsp.getClass().getField("playerConnection").get(nmsp);
	        
	        pcon.getClass().getMethod("sendPacket", new Class[] { getNmsClass("Packet") }).invoke(pcon, new Object[] { ppoplhf });
		}
		 else if ((getServerVersion().equalsIgnoreCase("v1_8_R2")) || 
			        (getServerVersion().equalsIgnoreCase("v1_8_R3")))
		{
	        Object header = getNmsClass("IChatBaseComponent$ChatSerializer").getMethod("a", new Class[] { String.class }).invoke(null, new Object[] { "{'text': '" + msg1 + "'}" });
	        Object footer = getNmsClass("IChatBaseComponent$ChatSerializer").getMethod("a", new Class[] { String.class }).invoke(null, new Object[] { "{'text': '" + msg2 + "'}" });
	        
	        Object ppoplhf = getNmsClass("PacketPlayOutPlayerListHeaderFooter").getConstructor(new Class[] { getNmsClass("IChatBaseComponent") }).newInstance(new Object[] { header });
	        
	        Field f = ppoplhf.getClass().getDeclaredField("b");
	        f.setAccessible(true);
	        f.set(ppoplhf, footer);
	        
	        Object nmsp = p.getClass().getMethod("getHandle", new Class[0]).invoke(p, new Object[0]);
	        Object pcon = nmsp.getClass().getField("playerConnection").get(nmsp);
	        
	        pcon.getClass().getMethod("sendPacket", new Class[] { getNmsClass("Packet") }).invoke(pcon, new Object[] { ppoplhf });
		}
		 else {
		
	        Object header = ChatSerializer.a("{'text': '" + msg1 + "'}");
	        Object footer = ChatSerializer.a("{'text': '" + msg2 + "'}");
	        
	        Object ppoplhf = new PacketPlayOutPlayerListHeaderFooter((IChatBaseComponent) header);
	        
	        Field f = null;
			try {
				f = ppoplhf.getClass().getDeclaredField("b");
			} catch (NoSuchFieldException | SecurityException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        f.setAccessible(true);
	        try {
				f.set(ppoplhf, footer);
			} catch (IllegalArgumentException | IllegalAccessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        
	        Object nmsp = null;
			try {
				nmsp = p.getClass().getMethod("getHandle", new Class[0]).invoke(p, new Object[0]);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
					| SecurityException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        PlayerConnection pcon = null;
			try {
				pcon = (PlayerConnection) nmsp.getClass().getField("playerConnection").get(nmsp);
			} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        
	        pcon.sendPacket((Packet) ppoplhf);
		 }
	}
	
	
	public static String convertStrings(Player p, String s) {
		
		s = s.replace("ae", "ä");
		s = s.replace("oe", "ö");
		s = s.replace("ue", "ü");
		s = s.replace("AE", "Ä");
		s = s.replace("OE", "Ö");
		s = s.replace("UE", "Ü");
		
		s = s.replace("{player}", p.getName());
		s = s.replace("{disname}", p.getDisplayName());
		s = s.replace("{level}", "" + p.getLevel());
		s = s.replace("{xp}", "" + p.getExp());
		s = s.replace("{online}", "" + Bukkit.getOnlinePlayers().size());
		
		if(Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
			s = PlaceholderAPI.setPlaceholders(p, s);
		}
		
		s = ChatColor.translateAlternateColorCodes('&', s);
		
		return s;
	}
	
}
