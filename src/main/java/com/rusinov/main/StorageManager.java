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

	Map<String, DownloadedInfo> downloaded = new ConcurrentHashMap<>();

	public StorageManager() throws IOException {
		// initial scan
		loadStorage();
	}

	public void loadStorage() throws IOException {
		downloaded.clear();
		File rootStorage = new File(Application.ROOT_OSMC);
		if (!rootStorage.exists()) {
			rootStorage.mkdirs();
		}

		for (File file : rootStorage.listFiles()) {
			if (file.isDirectory()) {
				if (file.listFiles().length <= 0) {
					file.delete();
					continue;
				}
				String taskName = file.getName();
				List<File> files = new ArrayList<>();
				Utils.getFiles(file, files);

				downloaded.put(taskName, new DownloadedInfo(files, new Date()));
			}
		}
	}

	public Map<String, DownloadedInfo> getDownloaded() {
		return downloaded;
	}

	public void deleteFile(String taskName, String filePath) {
		if (downloaded.containsKey(taskName)) {
			if (filePath == null) {
				FileSystemUtils.deleteRecursively(new File(Application.ROOT_OSMC + "/" + taskName));
				downloaded.remove(taskName);
			} else {
				File fileToDel = new File(filePath);

				// don't delete anything outside
				if (!fileToDel.getAbsolutePath().startsWith(Application.ROOT_OSMC)) {
					return;
				}

				if (!fileToDel.exists()) {
					if(fileToDel.getParentFile() != null && fileToDel.getParentFile().list().length <= 0) {
						fileToDel.getParentFile().delete();
						downloaded.remove(taskName);
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
							downloaded.remove(taskName);
						}
						break;
					}
				}
			}
		}
	}

}
