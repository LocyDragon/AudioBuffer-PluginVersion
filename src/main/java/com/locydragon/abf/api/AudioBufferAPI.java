package com.locydragon.abf.api;

import com.locydragon.abf.AudioBuffer;
import com.locydragon.abf.AudioInput;
import com.locydragon.abf.QueueJob;
import org.bukkit.entity.Player;

import java.math.BigInteger;

public class AudioBufferAPI {
	public static boolean playFor(Player who, String musicName) {
		return playForByParam(who, AudioBuffer.config.getString("MusicList."+musicName+".param", null));
	}

	public static boolean playForByParam(Player who, String param) {
		if (param == null) {
			return false;
		}
		if (param.startsWith("[Net]")) {
			who.sendPluginMessage(AudioBuffer.buffer, "AudioBuffer", param.getBytes());
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
