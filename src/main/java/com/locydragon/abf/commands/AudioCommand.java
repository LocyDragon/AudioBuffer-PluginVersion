package com.locydragon.abf.commands;
import com.locydragon.abf.AudioBuffer;
import com.locydragon.abf.api.AudioBufferAPI;
import com.locydragon.abf.listener.WorldChangeListener;
import com.locydragon.abf.listener.core.PlayerLoopThreadAche;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.io.IOException;

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
				if (PlayerLoopThreadAche.loopThreadAche.containsKey(playerName)) {
					sender.sendMessage(ChatColor.RED+"循环播放时无法改变音量.");
					return false;
				}
				AudioBufferAPI.stopPlaying(target);
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
			for (Player online : Bukkit.getOnlinePlayers()) {
				AudioBufferAPI.stopPlaying(online);
			}
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
				AudioBuffer.worldMusic.set(targetWorld.getName(), args[2]);
				try {
					AudioBuffer.worldMusic.save(".//plugins//AudioBuffer//Cache//WorldMusic.abf");
				} catch (IOException e) {
					e.printStackTrace();
				}
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
				AudioBuffer.worldMusic.set(targetWorld.getName(), null);
				try {
					AudioBuffer.worldMusic.save(".//plugins//AudioBuffer//Cache//WorldMusic.abf");
				} catch (IOException e) {
					e.printStackTrace();
				}
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
		} else if (args[0].equalsIgnoreCase("cleanCache") && sender.hasPermission("AudioBuffer.admin")) {
			if (args.length == 2) {
				String musicName = args[1];
				String param = AudioBuffer.config.getString("MusicList."+musicName+".param", null);
				AudioBuffer.save.set(param, -1);
				sender.sendMessage(ChatColor.GREEN + "清除缓存成功了.");
			} else {
				sender.sendMessage(ChatColor.RED+"请使用/abf cleanCache [音乐名称] ——清理一个音乐的缓存");
			}
		} else if (args[0].equalsIgnoreCase("loopFor")) {
			if (args.length == 3) {
				String playerName = args[1];
				String musicName = args[2];
				Player target = Bukkit.getPlayer(playerName);
				if (target == null) {
					sender.sendMessage(ChatColor.RED+"你指定的玩家不在线或不存在!");
					return false;
				}
				AudioBufferAPI.loopPlayer(target, musicName);
			} else {
				sender.sendMessage(ChatColor.RED + "请使用/abf loopFor [玩家名称] [音乐名称] ——为一个玩家循环播放音乐.");
			}
		} else if (args[0].equalsIgnoreCase("stopAndPlay")) {
			if (args.length == 3) {
				String playerName = args[1];
				String musicName = args[2];
				Player target = Bukkit.getPlayer(playerName);
				if (target == null) {
					sender.sendMessage(ChatColor.RED+"你指定的玩家不在线或不存在!");
					return false;
				}
				AudioBufferAPI.stopPlaying(target);
				if (!AudioBufferAPI.playFor(target, musicName)){
					sender.sendMessage(ChatColor.RED+"你指定音乐名称不存在或参数不符合规范!");
					return false;
				}
			} else {
				sender.sendMessage(ChatColor.RED+"请使用/abf stopAndPlay [玩家名字] [音乐名称] ——来为某个玩家播放(先暂停)一个音乐!");
			}
		} else if (args[0].equalsIgnoreCase("stopAndPlaySelf")) {
			if (args.length == 2) {
				Player target = (Player)sender;
				AudioBufferAPI.stopPlaying(target);
				AudioBufferAPI.playFor(target, args[1]);
			} else {
				sender.sendMessage(ChatColor.RED+"请使用/abf stopAndPlaySelf [音乐名称] ——给自己播放(停下后)一个音乐");
			}
		} else if (args[0].equalsIgnoreCase("stopNearBy")) {
			if (args.length == 5) {
				String who = args[1];
				if (isInt(args[2]) && isInt(args[3]) && isInt(args[4])) {
					int x = Integer.valueOf(args[2]);
					int y = Integer.valueOf(args[3]);
					int z = Integer.valueOf(args[4]);
					Player target = Bukkit.getPlayer(who);
					if (target == null) {
						sender.sendMessage(ChatColor.RED+"你指定的玩家不在线或不存在!");
						return false;
					}
					for (Entity entity : target.getNearbyEntities(x, y, z)) {
						if (entity instanceof Player) {
							Player instance = (Player)entity;
							AudioBufferAPI.stopPlaying(instance);
						}
					}
					AudioBufferAPI.stopPlaying(target);
				} else {
					sender.sendMessage(ChatColor.RED+"请输入正确的数字.");
				}
			} else {
				sender.sendMessage(ChatColor.RED+"请使用/abf stopNearBy [玩家名] [x] [y] [z] ——一一个玩家为立体图形的中心,xyz为长宽高,在这个范围内的玩家都会播放音效(包括该玩家)");
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
