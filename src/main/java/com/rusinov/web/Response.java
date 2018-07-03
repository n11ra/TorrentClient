package com.rusinov.web;

import java.util.Map;

public class Response {

	String date;
	String taskName;
	String downloadingProgress;
	Map<String, String> files;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getDownloadingProgress() {
		return downloadingProgress;
	}

	public void setDownloadingProgress(String downloadingProgress) {
		this.downloadingProgress = downloadingProgress;
	}

	public Map<String, String> getFiles() {
		return files;
	}

	public void setFiles(Map<String, String> files) {
		this.files = files;
	}

}
