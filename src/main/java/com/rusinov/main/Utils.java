package com.rusinov.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.springframework.util.FileCopyUtils;

import com.github.junrar.Junrar;
import com.github.junrar.exception.RarException;

public class Utils {
	
	public static final DecimalFormat DF = new DecimalFormat("#.##");

	public static String convertToHumanReadableScale(long bytes) {
		double kilobytes = (bytes / 1024);
		if (kilobytes < 1) {
			return DF.format(bytes) + " bytes";
		}
		double megabytes = (kilobytes / 1024);
		if (megabytes < 1) {
			return DF.format(kilobytes) + " KB";
		}
		double gigabytes = (megabytes / 1024);
		if (gigabytes < 1) {
			return DF.format(megabytes) + " MB";
		}
		double terabytes = (gigabytes / 1024);
		if (terabytes < 1) {
			return DF.format(gigabytes) + " GB";
		}
		return "";
	}

	public static File downloadFile(URL url, String taskName, String cookie) throws IOException {
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		if (cookie != null) {
			conn.setRequestProperty("Cookie", cookie);
		}

		conn.setRequestProperty("Referer", url.toString());
		conn.setRequestProperty("Connection", "close");

		String contentDisposition = conn.getHeaderField("Content-Disposition");
		String fileName = parseContentDisposition(contentDisposition);

		File file = new File(Application.ROOT_DIR + "/" + taskName + "/" + fileName);
		file.createNewFile();
		FileCopyUtils.copy(conn.getInputStream(), new FileOutputStream(file));
		return file;
	}

	private static String parseContentDisposition(String contentDisposition) {
		String fileNameStr = "filename=";
		String fileName = contentDisposition.substring(contentDisposition.indexOf(fileNameStr) + fileNameStr.length(),
				contentDisposition.length());
		if ((fileName.startsWith("\"") || fileName.startsWith("\'"))
				&& (fileName.endsWith("\"") || fileName.endsWith("\'"))) {
			return fileName.substring(1, fileName.length() - 1);
		}
		return fileName;
	}

	public static void unzip(File zip, File targetDir) throws IOException {
		byte[] buffer = new byte[1024];
		ZipInputStream zis = new ZipInputStream(new FileInputStream(zip));
		ZipEntry zipEntry = zis.getNextEntry();
		while (zipEntry != null) {
			String fileName = zipEntry.getName();
			File newFile = new File(targetDir + "/" + fileName);
			FileOutputStream fos = new FileOutputStream(newFile);
			int len;
			while ((len = zis.read(buffer)) > 0) {
				fos.write(buffer, 0, len);
			}
			fos.close();
			zipEntry = zis.getNextEntry();
		}
		zis.closeEntry();
		zis.close();
	}
	
	public static void unrar(File zip, File targetDir) throws RarException, IOException {
		Junrar.extract(zip, targetDir);
	}

}
