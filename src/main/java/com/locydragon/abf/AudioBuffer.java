package com.locydragon.abf;

import com.locydragon.abf.commands.AudioCommand;
import com.locydragon.abf.listener.AntiServerQuitListener;
import com.locydragon.abf.listener.WorldChangeListener;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

/**
 * @author LocyDragon
 */
public class AudioBuffer extends JavaPlugin {
	public static FileConfiguration config;
	public static AudioBuffer buffer;
	public static FileConfiguration save;
	public static FileConfiguration worldMusic;
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
		Bukkit.getPluginManager().registerEvents(new WorldChangeListener(), this);
		Bukkit.getPluginManager().registerEvents(new AntiServerQuitListener(), this);
		File saveFile = new File(".//plugins//AudioBuffer//Cache//Cache.abf");
		if (!saveFile.exists()) {
			saveFile.getParentFile().mkdirs();
			try {
				saveFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		save = YamlConfiguration.loadConfiguration(saveFile);
		File musicSaving = new File(".//plugins//AudioBuffer//Cache//WorldMusic.abf");
		if (!musicSaving.exists()) {
			musicSaving.getParentFile().mkdirs();
			try {
				musicSaving.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		worldMusic = YamlConfiguration.loadConfiguration(musicSaving);
		for (String worldName : worldMusic.getKeys(false)) {
			String musicName = worldMusic.getString(worldName);
			WorldChangeListener.playInWorld.put(worldName, musicName);
		}
	}

	public static void reloadConfiguration() {
		buffer.reloadConfig();
		config = buffer.getConfig();
	}
}
