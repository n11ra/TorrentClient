package com.rusinov.main;

import java.util.Date;

public class DownloadingInfo {

	private Date date;
	private String downloadingProgress;

	public DownloadingInfo(Date date, String downloadingProgress) {
		super();
		this.date = date;
		this.downloadingProgress = downloadingProgress;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getDownloadingProgress() {
		return downloadingProgress;
	}

	public void setDownloadingProgress(String downloadingProgress) {
		this.downloadingProgress = downloadingProgress;
	}


}
