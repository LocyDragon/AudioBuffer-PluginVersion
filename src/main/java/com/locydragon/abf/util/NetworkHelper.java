package com.locydragon.abf.util;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.URL;

public class NetworkHelper {
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
