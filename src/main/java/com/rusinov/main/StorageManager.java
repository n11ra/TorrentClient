package com.rusinov.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
import org.springframework.util.FileSystemUtils;

@Component
public class StorageManager {

	Map<String, DownloadingInfo> downloading = new ConcurrentHashMap<>();
	Map<String, DownloadedInfo> downloaded = new ConcurrentHashMap<>();

	public StorageManager() throws IOException {
		// initial scan
		loadStorage();
	}

	public void loadStorage() throws IOException {
		downloaded.clear();
		File rootStorage = new File(Application.getRootDir());
		if (!rootStorage.exists()) {
			rootStorage.mkdirs();
		}

		for (File file : new File(Application.getRootDir()).listFiles()) {
			if (file.isDirectory()) {
				if (file.listFiles().length == 0) {
					file.delete();
					continue;
				}
				String taskName = file.getName();
				List<File> files = new ArrayList<>();
				Utils.getFiles(file, files);

				addToDownloaded(taskName, new DownloadedInfo(files, new Date()));
			}
		}
	}

	public void addToDownloaded(String key, DownloadedInfo object) {
		downloaded.put(key, object);
	}

	public void addToDownloading(String key, DownloadingInfo object) {
		downloading.put(key, object);
	}

	public void removeFromDownloading(String key) {
		downloading.remove(key);
	}

	public void removeFromDownloaded(String key) {
		downloaded.remove(key);
	}

	public Map<String, DownloadingInfo> getDownloading() {
		return downloading;
	}

	public Map<String, DownloadedInfo> getDownloaded() {
		return downloaded;
	}

	public void deleteFile(String taskName, String filePath) {
		if (downloading.containsKey(taskName)) {
			FileSystemUtils.deleteRecursively(new File(Application.getRootDir() + "/" + taskName));
			removeFromDownloading(taskName);
		}
		if (downloaded.containsKey(taskName)) {
			if (filePath == null) {
				FileSystemUtils.deleteRecursively(new File(Application.getRootDir() + "/" + taskName));
				removeFromDownloaded(taskName);
			} else {
				File fileToDel = new File(filePath);

				// don't delete anything outside
				if (!fileToDel.getAbsolutePath().startsWith(Application.getRootDir())) {
					return;
				}

				if (!fileToDel.exists()) {
					if(fileToDel.getParentFile() != null && fileToDel.getParentFile().list().length <= 0) {
						fileToDel.getParentFile().delete();
						removeFromDownloaded(taskName);
					}
					return;
				}

				for (File file : downloaded.get(taskName).getFiles()) {
					if (file.getAbsolutePath().equals(fileToDel.getAbsolutePath())) {
						if (file.delete()) {
							downloaded.get(taskName).getFiles().remove(file);
						}
						if (file.getParentFile().list().length <= 0) {
							file.getParentFile().delete();
							removeFromDownloaded(taskName);
						}
						break;
					}
				}
			}
		}
	}

}
