package com.locydragon.abf;

import com.locydragon.abf.nms.BigPluginMessageSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Vector;

/**
 * @author Administrator
 */
public class AudioInput implements PluginMessageListener {
	public static Vector<QueueJob> jobQueue = new Vector<>();
	@Override
	public void onPluginMessageReceived(String s, Player player, byte[] bytes) {
		String message = new String(bytes);
		String[] params = message.split("\\*");
		for (int i = 0;i < jobQueue.size();i++) {
			QueueJob job = jobQueue.get(i);
			if (job.isSame(params[0])) {
				jobQueue.remove(i);
				if (Boolean.valueOf(params[1])) {
					String messageOut = "[Local]"+job.fileName;
					player.sendPluginMessage(AudioBuffer.buffer, "AudioBuffer", messageOut.getBytes());
				} else {
					File audioFile = new File(".//plugins//AudioBuffer//Music//"+job.fileName);
					if (!audioFile.exists()) {
						return;
					}
					String messageOut = "[Download]"+job.fileName;
					player.sendPluginMessage(AudioBuffer.buffer, "AudioBuffer", messageOut.getBytes());
					try {
						FileInputStream stream = new FileInputStream(audioFile);
						try {
							byte[] out = new byte[stream.available()];
							stream.read(out);
							stream.close();
							BigPluginMessageSender.send(player, "AudioBuffer", out);
							String messageOutStart = "[Local]"+job.fileName;
							player.sendPluginMessage(AudioBuffer.buffer, "AudioBuffer", messageOutStart.getBytes());
						} catch (IOException e) {
							e.printStackTrace();
						}
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				}
				break;
			}
		}
	}
}
