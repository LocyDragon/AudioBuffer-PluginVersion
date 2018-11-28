package com.locydragon.abf.api;

import com.locydragon.abf.AudioBuffer;
import com.locydragon.abf.AudioInput;
import com.locydragon.abf.QueueJob;
import com.locydragon.abf.listener.core.LoopThread;
import com.locydragon.abf.listener.core.PlayerLoopThreadAche;
import com.locydragon.abf.util.AudioReaderUtil;
import com.locydragon.abf.util.NetworkHelper;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Random;

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
		if (PlayerLoopThreadAche.loopThreadAche.get(who.getName()) != null) {
			LoopThread thread = PlayerLoopThreadAche.loopThreadAche.get(who.getName());
			thread.stopSafely();
			PlayerLoopThreadAche.loopThreadAche.remove(who.getName());
		}
		who.sendPluginMessage(AudioBuffer.buffer, "AudioBuffer", "[Stop]".getBytes());
	}

	public static void setVolume(Player who, float volume) {
		String output = "[Volume]"+volume;
		who.sendPluginMessage(AudioBuffer.buffer, "AudioBuffer", output.getBytes());
	}

	private static int getAudioLengthByParam(String param) {
		if (param.startsWith("[Local]")) {
			String message = param.replace("[Local]", "");
			File audioFile = new File(".//plugins//AudioBuffer//Music//"+message);
			return AudioReaderUtil.getAudioPlayTime(audioFile.getAbsolutePath());
		} else if (param.startsWith("[Net]")) {
			String message = param.replace("[Net]", "");
			String downloadCacheName = new Random().nextInt(Integer.MAX_VALUE) + ".mp3";
			NetworkHelper.downloadHttpUrl(message, ".//plugins//AudioBuffer//Music//Download//", downloadCacheName);
			File downloadedFile = new File(".//plugins//AudioBuffer//Music//Download//" + downloadCacheName);
			int audioTime = AudioReaderUtil.getAudioPlayTime(downloadedFile.getAbsolutePath());
			if (!forceDelete(downloadedFile)) {
				downloadedFile.delete();
			}
			return audioTime;
		}
		return -1;
	}

	private static boolean forceDelete(File file) {
		boolean result = file.delete();
		int tryCount = 0;
		while (!result && tryCount++ < 10) {
			System.gc();
			result = file.delete();
		}
		return result;
	}

	public static boolean loopPlayer(Player who, int secondDelay, String musicURL) {
		LoopThread targetThread = new LoopThread(musicURL, secondDelay, who);
		targetThread.start();
		PlayerLoopThreadAche.loopThreadAche.put(who.getName(), targetThread);
		return true;
	}

	public static int getAudioLengthByParamQuickly(String param) {
		int cacheParam = AudioBuffer.save.getInt(param, -1);
		if (cacheParam != -1) {
			return cacheParam;
		}
		int newAudioLength = getAudioLengthByParam(param);
		AudioBuffer.save.set(param, newAudioLength);
		try {
			AudioBuffer.save.save(".//plugins//AudioBuffer//Cache//Cache.abf");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return newAudioLength;
	}
}
