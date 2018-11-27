package com.locydragon.abf.commands;
import com.locydragon.abf.AudioBuffer;
import com.locydragon.abf.api.AudioBufferAPI;
import com.locydragon.abf.listener.WorldChangeListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class AudioCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
		if (!(sender.hasPermission("AudioBuffer.use"))) {
			sender.sendMessage(ChatColor.RED+"你没有权限.");
			return false;
		}
		if (args.length <= 0) {
			sender.sendMessage(ChatColor.RED+"参数不足.");
			return false;
		}
		if (args[0].equalsIgnoreCase("play")) {
			if (args.length == 3) {
				String playerName = args[1];
				String musicName = args[2];
				Player target = Bukkit.getPlayer(playerName);
				if (target == null) {
					sender.sendMessage(ChatColor.RED+"你指定的玩家不在线或不存在!");
					return false;
				}
				if (!AudioBufferAPI.playFor(target, musicName)){
					sender.sendMessage(ChatColor.RED+"你指定音乐名称不存在或参数不符合规范!");
					return false;
				}
			} else {
				sender.sendMessage(ChatColor.RED+"请使用/abf play [玩家名字] [音乐名称] ——来为某个玩家播放一个音乐!");
			}
		} else if (args[0].equalsIgnoreCase("volume")) {
			if (args.length == 3) {
				String playerName = args[1];
				String volume = args[2];
				Player target = Bukkit.getPlayer(playerName);
				if (!isNumber(volume)) {
					sender.sendMessage(ChatColor.RED+"你输入的内容不是一个数字.");
					return false;
				}
				AudioBufferAPI.setVolume(target, Float.valueOf(volume));
			} else {
				sender.sendMessage(ChatColor.RED+"请使用/abf volume [玩家名字] [音量] ——为某个玩家调节音量.");
			}
		} else if (args[0].equalsIgnoreCase("stop")) {
			if (args.length == 2) {
				Player target = Bukkit.getPlayer(args[1]);
				if (target == null) {
					sender.sendMessage(ChatColor.RED+"你指定的玩家不在线或不存在!");
					return false;
				}
				AudioBufferAPI.stopPlaying(target);
			} else {
				sender.sendMessage(ChatColor.RED+"请使用/abf stop [玩家名字] ——来为某个玩家停止播放音乐!");
			}
		} else if (args[0].equalsIgnoreCase("reload") && sender.hasPermission("AudioBuffer.admin")) {
			AudioBuffer.reloadConfiguration();
			sender.sendMessage(ChatColor.GREEN+"重载插件成功!");
		} else if (args[0].equalsIgnoreCase("stopAll") && sender.hasPermission("AudioBuffer.admin")) {
			for (Player online : Bukkit.getOnlinePlayers()) {
				AudioBufferAPI.stopPlaying(online);
			}
		} else if (args[0].equalsIgnoreCase("playAll")) {
			if (args.length == 2) {
				String musicName = args[1];
				for (Player online : Bukkit.getOnlinePlayers()) {
					AudioBufferAPI.playFor(online, musicName);
				}
			} else {
				sender.sendMessage(ChatColor.RED+"请使用/abf playAll [音乐名称] ——来为所有玩家播放一个音乐!");
			}
		} else if (args[0].equalsIgnoreCase("playInWorld")) {
			if (args.length == 3) {
				World targetWorld = Bukkit.getWorld(args[1]);
				if (targetWorld == null) {
					sender.sendMessage(ChatColor.RED+"世界不存在!");
					return false;
				}
				for (Player inWorld : targetWorld.getPlayers()) {
					AudioBufferAPI.stopPlaying(inWorld);
					AudioBufferAPI.playFor(inWorld, args[2]);
				}
				WorldChangeListener.playInWorld.put(targetWorld.getName(), args[2]);
			} else {
				sender.sendMessage(ChatColor.RED+"请使用/abf playInWorld [世界名称] [音乐名称] ——在一个世界里播放音乐!");
			}
		} else if (args[0].equalsIgnoreCase("stopInWorld")) {
			if (args.length == 2) {
				String worldName = args[1];
				World targetWorld = Bukkit.getWorld(worldName);
				if (targetWorld == null) {
					sender.sendMessage(ChatColor.RED+"世界不存在!");
					return false;
				}
				for (Player inWorld : targetWorld.getPlayers()) {
					AudioBufferAPI.stopPlaying(inWorld);
				}
				WorldChangeListener.playInWorld.remove(worldName);
			} else {
				sender.sendMessage(ChatColor.RED+"请使用/abf stopInWorld [世界名称] ——在一个世界里停止播放音乐!");
			}
		} else if (args[0].equalsIgnoreCase("nearBy")) {
			if (args.length == 6) {
				String who = args[1];
				String musicType = args[2];
				if (isInt(args[3]) && isInt(args[4]) && isInt(args[5])) {
					int x = Integer.valueOf(args[3]);
					int y = Integer.valueOf(args[4]);
					int z = Integer.valueOf(args[5]);
					Player target = Bukkit.getPlayer(who);
					if (target == null) {
						sender.sendMessage(ChatColor.RED+"你指定的玩家不在线或不存在!");
						return false;
					}
					for (Entity entity : target.getNearbyEntities(x, y, z)) {
						if (entity instanceof Player) {
							Player instance = (Player)entity;
							AudioBufferAPI.playFor(instance, musicType);
						}
					}
					AudioBufferAPI.playFor(target, musicType);
				} else {
					sender.sendMessage(ChatColor.RED+"请输入正确的数字.");
				}
			} else {
				sender.sendMessage(ChatColor.RED+"请使用/abf nearBy [玩家名] [音乐名] [x] [y] [z] ——一一个玩家为立体图形的中心,xyz为长宽高,在这个范围内的玩家都会播放音效(包括该玩家)");
			}
		} else if (args[0].equalsIgnoreCase("playSelf")) {
			if (args.length == 2) {
				Player target = (Player)sender;
				AudioBufferAPI.playFor(target, args[1]);
			} else {
				sender.sendMessage(ChatColor.RED+"请使用/abf playSelf [音乐名称] ——给自己播放一个音乐");
			}
		}
		return false;
	}

	public static boolean isNumber(String number) {
		try {
			Float.valueOf(number);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean isInt(String number) {
		try {
			Integer.valueOf(number);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
