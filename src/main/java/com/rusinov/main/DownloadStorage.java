package com.rusinov.main;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DownloadStorage {

	Map<String, Object> downloading = new ConcurrentHashMap<>();
	Map<String, Object> downloaded = new ConcurrentHashMap<>();

	public void addToDownloaded(String key, Object object) {
		downloaded.put(key, object);
	}

	public void addToDownloading(String key, Object object) {
		downloading.put(key, object);
	}

	public void removeFromDownloading(String key) {
		downloading.remove(key);
	}

}
