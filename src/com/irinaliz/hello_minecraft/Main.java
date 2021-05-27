package com.irinaliz.hello_minecraft;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Main extends JavaPlugin implements Listener{
	FileConfiguration config = getConfig();
	public static FileConfiguration configs;
	public static String id;
	public static String pass;
	public static String location;
	@Override
	public void onEnable() {
		config.addDefault("location", "12341234");
		config.addDefault("id", "12341234");
		config.addDefault("pass", "12341234");
		config.options().copyDefaults(true);
		saveConfig();
		id = this.getConfig().getString("id");
		location = this.getConfig().getString("location");
		pass = this.getConfig().getString("pass");
	    
		try {
			Database.getDatabase();
		} catch (Exception e) {
			Bukkit.getLogger().severe("������ �߻��Ͽ� �÷������� �����մϴ�: " + e);
			Bukkit.getPluginManager().disablePlugin(this);
		}
		getServer().getPluginManager().registerEvents(this, this);
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		
		new BukkitRunnable() {
			@Override
			public void run() {
				Database.insertStatistic(e.getPlayer());
			}
		}.runTaskAsynchronously(this);
		
		
		
//		Player p = e.getPlayer();
//		for(Statistic stat : Statistic.values()) {
//			switch (stat.getType()) {
//			
//			case UNTYPED :
//				if(p.getStatistic(stat) > 0 ) {
//					Bukkit.getLogger().info( "�ൿ |  "+ stat + " : " +p.getStatistic(stat));
//				}
//					break;
//			
//			case ITEM : 
//			case BLOCK : 
//				for(Material item : Material.values()) {
//					if(p.getStatistic(stat, item) > 0) {
//						String hangdong = "";
//						
//						switch(stat.toString()) {
//						case "USE_ITEM" : 
//							hangdong = "���"; 
//							break;
//						case "CRAFT_ITEM" :
//							hangdong = "����"; 
//							break;
//						case "MINE_BLOCK" : 
//							hangdong = "�ı�"; 
//							break;
//						case "PICKUP" : 
//							hangdong = "�ݱ�"; 
//							break;
//						case "DROP" : 
//							hangdong = "������"; 
//							break;
//						}
//					Bukkit.getLogger().info(hangdong + " | " +item.name() + " : " + p.getStatistic(stat, item));
//					}
//				}
//				break;
//			
//			case ENTITY :
//				for ( EntityType type : EntityType.values()) {
//					if(!type.toString().equals("UNKNOWN")) {
//						if(p.getStatistic(stat, type) > 0) {
//						Bukkit.getLogger().info(  (stat.toString() == "KILL_ENTITY" ? "���̴�" : "�״�") + " | "+  type.name() + " : "+ p.getStatistic(stat,type));
//						}
//					}
//				}
//			}
//		}
		
	}

}
