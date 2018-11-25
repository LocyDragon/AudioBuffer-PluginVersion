package com.locydrasgon.abf.commands;
import com.locydrasgon.abf.AudioBuffer;
import com.locydrasgon.abf.api.AudioBufferAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AudioCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
		if (!(sender.hasPermission("AudioBuffer.use"))) {
			sender.sendMessage(ChatColor.RED+"你没有权限.");
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
}
