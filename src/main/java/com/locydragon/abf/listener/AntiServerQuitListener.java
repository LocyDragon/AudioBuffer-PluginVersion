package com.locydragon.abf.listener;

import com.locydragon.abf.api.AudioBufferAPI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class AntiServerQuitListener implements Listener {
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		AudioBufferAPI.stopPlaying(e.getPlayer());
	}

	@EventHandler
	public void onBeingKicked(PlayerKickEvent e) {
		AudioBufferAPI.stopPlaying(e.getPlayer());
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		AudioBufferAPI.stopPlaying(e.getPlayer());
	}
}
