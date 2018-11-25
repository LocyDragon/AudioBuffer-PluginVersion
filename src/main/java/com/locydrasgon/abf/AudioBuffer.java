package com.locydrasgon.abf;

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
	}

	public static void reloadConfiguration() {
		buffer.reloadConfig();
		config = buffer.getConfig();
	}
}
