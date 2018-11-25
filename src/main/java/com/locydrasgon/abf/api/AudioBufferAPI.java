package com.locydrasgon.abf.api;

import com.locydrasgon.abf.AudioBuffer;
import com.locydrasgon.abf.AudioInput;
import com.locydrasgon.abf.QueueJob;
import org.bukkit.entity.Player;

import java.math.BigInteger;

public class AudioBufferAPI {
	public static boolean playFor(Player who, String musicName) {
		String param = AudioBuffer.config.getString("MusicList."+musicName+".param", null);
		if (param == null) {
			return false;
		}
		if (param.startsWith("[Net]")) {
			who.sendPluginMessage(AudioBuffer.buffer, "AudioBuffer", musicName.getBytes());
			return true;
		} else if (param.startsWith("[Local]")) {
			QueueJob job = new QueueJob();
			QueueJob.nowID.add(new BigInteger("1"));
			job.queueId = QueueJob.nowID.toString();
			job.fileName = param.replace("[Local]", "");
			AudioInput.jobQueue.add(job);
			String outputMsg = "[Has]"+job.queueId+"*"+job.fileName;
			who.sendPluginMessage(AudioBuffer.buffer, "AudioBuffer", outputMsg.getBytes());
			return true;
		}
		return false;
	}

	public static void stopPlaying(Player who) {
		who.sendPluginMessage(AudioBuffer.buffer, "AudioBuffer", "[Stop]".getBytes());
	}

	public static void setVolume(Player who, float volume) {
		String output = "[Volume]"+volume;
		who.sendPluginMessage(AudioBuffer.buffer, "AudioBuffer", output.getBytes());
	}
}
