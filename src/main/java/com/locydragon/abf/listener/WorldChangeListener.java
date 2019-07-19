package com.locydragon.abf.listener;

import com.locydragon.abf.AudioBuffer;
import com.locydragon.abf.api.AudioBufferAPI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import java.util.concurrent.ConcurrentHashMap;

public class WorldChangeListener implements Listener {
	public static ConcurrentHashMap<String,String> playInWorld = new ConcurrentHashMap<>();

	@EventHandler
	public void onWorldChange(PlayerChangedWorldEvent e) {
		if (playInWorld.get(e.getPlayer().getWorld().getName()) != null) {
			AudioBufferAPI.stopPlaying(e.getPlayer());
			String musicType = playInWorld.get(e.getPlayer().getWorld().getName());
			AudioBufferAPI.playFor(e.getPlayer(), musicType);
		} else {
			if (AudioBuffer.buffer.getConfig().getBoolean("WorldChangeStopMusic", true)) {
				AudioBufferAPI.stopPlaying(e.getPlayer());
			}
		}
	}
}
