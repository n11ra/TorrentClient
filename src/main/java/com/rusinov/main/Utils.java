package com.rusinov.main;

public class Utils {

	public static String convertToHumanReadableScale(long bytes) {
		double kilobytes = (bytes / 1024);
		if (kilobytes < 1) {
			return bytes + " bytes";
		}
		double megabytes = (kilobytes / 1024);
		if (megabytes < 1) {
			return kilobytes + " KB";
		}
		double gigabytes = (megabytes / 1024);
		if (gigabytes < 1) {
			return megabytes + " MB";
		}
		double terabytes = (gigabytes / 1024);
		if (terabytes < 1) {
			return gigabytes + " GB";
		}
		return "";
	}

}
