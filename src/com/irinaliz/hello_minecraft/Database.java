package com.irinaliz.hello_minecraft;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class Database {
	private static Connection conn;
	
	public static Connection getDatabase() {
		try {
			if (conn == null || (conn != null && conn.isClosed())) {
				Class.forName("com.mysql.jdbc.Driver");
				String url = "jdbc:mysql://localhost/" + Main.location;
				conn = DriverManager.getConnection(url, Main.id, Main.pass);
			}
		} catch (ClassNotFoundException e) {
			Bukkit.getLogger().severe("드라이버 로딩에 실패하였습니다.");
		} catch (SQLException e) {
			Bukkit.getLogger().severe("SQL 오류: " + e);
		}
		return conn;
	}
	
	public static void close() {
		try {
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException e) {
			Bukkit.getLogger().severe("SQL 오류: " + e);
		}
	}
	
	public static void insertStatistic(Player player) {
		try {
			int value = 0;
			Connection conn = Database.getDatabase();
			PreparedStatement pstmt = conn.prepareStatement
			("INSERT INTO player_statistics (uuid, statistic, substatistic, value) VALUES (?, ?, ?, ?)"
					+ " ON DUPLICATE KEY UPDATE value = ?;");
			pstmt.setString(1, player.getUniqueId().toString());
			
			for (Statistic statistic : Statistic.values()) {
				pstmt.setString(2, statistic.name());
				switch (statistic.getType()) {
					case UNTYPED:
						value = player.getStatistic(statistic);
						if (value <= 0) continue;
						pstmt.setString(3, "");
						pstmt.setInt(4, value);
						pstmt.setInt(5, value);
						pstmt.executeUpdate();
						break;
					case ITEM: 
					case BLOCK: 
						for (Material item : Material.values()) {
							value = player.getStatistic(statistic, item);
							if (value <= 0) continue;
							pstmt.setString(3, item.name());
							pstmt.setInt(4, value);
							pstmt.setInt(5, value);
							pstmt.executeUpdate();
						}
						break;
					case ENTITY:
						for (EntityType type : EntityType.values()) {
							if (type == EntityType.UNKNOWN) continue;
							value = player.getStatistic(statistic, type);
							if (value <= 0) continue;
							pstmt.setString(3, type.name());
							pstmt.setInt(4, value);
							pstmt.setInt(5, value);
							pstmt.executeUpdate();
						}
						break;
				}
			}
			
		} catch (SQLException ex) {
			Bukkit.getLogger().severe("SQL 오류: " + ex);
		}
	}

}
