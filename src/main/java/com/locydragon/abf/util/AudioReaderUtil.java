package com.locydragon.abf.util;

import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.decoder.Header;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class AudioReaderUtil {
	public static Integer getAudioPlayTime(String filePath) {
		FileInputStream fis = null;
		int time = 0;
		try {
			fis = new FileInputStream(new File(filePath));
			int b = fis.available();
			Bitstream bt = new Bitstream(fis);
			Header h = bt.readFrame();
			time = (int)h.total_ms(b);
		} catch (BitstreamException | IOException e) {
			e.printStackTrace();
		}
		int i = time / 1000;
		try {
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return i;
	}
}
