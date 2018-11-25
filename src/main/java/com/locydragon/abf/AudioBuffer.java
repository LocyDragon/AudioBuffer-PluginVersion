package com.locydragon.abf;

import com.locydragon.abf.commands.AudioCommand;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

/**
 * @author LocyDragon
 */
public class AudioBuffer extends JavaPlugin {
	public static FileConfiguration config;
	public static AudioBuffer buffer;
	@Override
	public void onEnable() {
		Bukkit.getMessenger().registerOutgoingPluginChannel(this, "AudioBuffer");
		Bukkit.getMessenger().registerIncomingPluginChannel(this, "AudioBufferOut", new AudioInput());
		saveDefaultConfig();
		config = getConfig();
		buffer = this;
		File noticeFile = new File(".//plugins//AudioBuffer//Music//PutMusicHere.exe");
		if (!noticeFile.exists()) {
			noticeFile.getParentFile().mkdirs();
			try {
				noticeFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Bukkit.getPluginCommand("abf").setExecutor(new AudioCommand());
		Bukkit.getPluginCommand("ab").setExecutor(new AudioCommand());
		Bukkit.getLogger().info("=====> 你正在使用AudioBuffer");
		Bukkit.getLogger().info("=====> 新一代音乐|RPG音效播放器!");
		Bukkit.getLogger().info("=====> 作者: 绿毛 -> QQ2424441676");
		Bukkit.getLogger().info("=====> 本插件永久免费，如果你是通过购买渠道获取的，请小心谨慎!");
		new Metrics(this);
	}

	public static void reloadConfiguration() {
		buffer.reloadConfig();
		config = buffer.getConfig();
	}
}
