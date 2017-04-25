package de.at.manager;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_8_R3.PlayerConnection;

public class Tablist implements Listener {

	
	@SuppressWarnings("rawtypes")
	public static void sendTablist(Player p, String msg1, String msg2) {
	
		msg1 = convertStrings(p, msg1);
		msg2 = convertStrings(p, msg2);
		
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
		
		
		s = ChatColor.translateAlternateColorCodes('&', s);
		
		return s;
	}
	
}
