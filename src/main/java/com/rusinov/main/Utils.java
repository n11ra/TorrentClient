package com.rusinov.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.springframework.util.FileCopyUtils;

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

	public static void findRenameAndMoveSubtitles(File taskDir) {
		if (taskDir == null || !taskDir.exists() || !taskDir.isDirectory()) {
			return;
		}

		// .sub .str
		List<File> subtitleFiles = new ArrayList<>();
		getSubtitleFiles(taskDir, subtitleFiles);

		if (subtitleFiles.size() == 0) {
			return;
		}

		// .mkv .avi .flv .wmv .mp4
		List<File> videoFiles = new ArrayList<>();
		getVideoFiles(taskDir, videoFiles);

		if (videoFiles.size() == 0) {
			return;
		}

		File movie = null;
		for (File video : videoFiles) {
			if (movie == null || movie.length() < video.length()) {
				movie = video;
			}
		}

		File subs = subtitleFiles.get(0);

		String movieName = movie.getName().substring(0, movie.getName().lastIndexOf('.'));
		String subExtention = subs.getName().substring(subs.getName().lastIndexOf('.'), subs.getName().length());

		subs.renameTo(new File(movie.getParentFile().getAbsolutePath() + "/" + movieName + subExtention));
	}

	public static void getFiles(File dir, List<File> files) {
		File[] fList = dir.listFiles();
		for (File file : fList) {
			if (file.isFile()) {
				files.add(file);
			} else if (file.isDirectory()) {
				getFiles(file, files);
			}
		}
	}

	private static void getVideoFiles(File dir, List<File> files) {
		File[] fList = dir.listFiles();
		for (File file : fList) {
			if (file.isFile() && (file.getAbsolutePath().endsWith(".mkv") || file.getAbsolutePath().endsWith(".avi")
					|| file.getAbsolutePath().endsWith(".flv") || file.getAbsolutePath().endsWith(".wmv")
					|| file.getAbsolutePath().endsWith(".mp4"))) {
				files.add(file);
			} else if (file.isDirectory()) {
				getVideoFiles(file, files);
			}
		}
	}

	private static void getSubtitleFiles(File dir, List<File> files) {
		File[] fList = dir.listFiles();
		for (File file : fList) {
			if (file.isFile() && (file.getAbsolutePath().endsWith(".sub") || file.getAbsolutePath().endsWith(".str")
					|| file.getAbsolutePath().endsWith(".srt"))) {
				files.add(file);
			} else if (file.isDirectory()) {
				getSubtitleFiles(file, files);
			}
		}
	}

	public static File downloadFile(URL url, String taskName, String cookie) throws IOException {
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		String fileName = "unknown";

		conn.setRequestProperty("Referer", url.toString());
		conn.setRequestProperty("Connection", "close");

		if (cookie != null) {
			conn.setRequestProperty("Cookie", cookie);
			fileName = url.toString().substring(url.toString().lastIndexOf('/'), url.toString().length());
		} else {
			String contentDisposition = conn.getHeaderField("Content-Disposition");
			if (contentDisposition != null) {
				fileName = parseContentDisposition(contentDisposition);
			}
		}

		File file = new File(Application.getRootDir() + "/" + taskName + "/" + fileName);
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

	public static void unarchive(File archive) throws IOException {
		if (archive.getName().endsWith(".zip")) {
//			Runtime.getRuntime().exec("unzip " + archive.getAbsolutePath() + " -d " + archive.getParent());
			new java.lang.ProcessBuilder("unzip", archive.getAbsolutePath(), "-d", archive.getParent()).start();
		} else if (archive.getName().endsWith(".rar")) {
//			Runtime.getRuntime().exec("unrar e " + archive.getAbsolutePath() + " " + archive.getParent());
			new java.lang.ProcessBuilder("unrar", "e", archive.getAbsolutePath(), archive.getParent()).start();
		}
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

}
