package com.locydragon.abf.util;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.URL;

public class NetworkHelper {
	public static byte[] getBytes(String filePath) {
		byte[] buffer = null;
		try {
			File file = new File(filePath);
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
			byte[] b = new byte[bos.size()];
			int n = 0;
			while ((n = fis.read(b)) != -1)
			{
				bos.write(b, 0, n);
			}
			fis.close();
			bos.close();
			buffer = bos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer;
	}

	public static void downloadHttpUrl(String url, String dir, String fileName) {
		try {
			URL httpurl = new URL(url);
			File dirfile = new File(dir);
			if (!dirfile.exists()) {
				dirfile.mkdirs();
			}
			FileUtils.copyURLToFile(httpurl, new File(dir + fileName));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
