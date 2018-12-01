package com.locydragon.abf.listener.core;

import com.locydragon.abf.api.AudioBufferAPI;
import org.bukkit.entity.Player;

public class LoopThread extends Thread {
	private String musicUrl = null;
	private int period = -1;
	private Player who;
	private boolean stop = false;

	public LoopThread(String url, int second, Player who) {
		this.musicUrl = url;
		this.period = second;
		this.who = who;
	}

	@Override
	public void run() {
		while (!this.stop) {
			if (this.who == null) {
				break;
			}
			AudioBufferAPI.playFor(this.who, this.musicUrl);
			try {
				Thread.sleep(this.period * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void stopSafely() {
		this.stop = true;
	}
}
