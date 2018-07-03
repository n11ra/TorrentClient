package com.rusinov.main;

import java.io.File;
import java.util.Date;
import java.util.List;

public class DownloadedInfo {

	private List<File> files;
	private Date date;

	public DownloadedInfo(List<File> files, Date date) {
		super();
		this.files = files;
		this.date = date;
	}

	public List<File> getFiles() {
		return files;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

//	public double getTotalSize() {
//		long totalSize = 0;
//		for (File f : files) {
//			totalSize += f.length();
//		}
//		return Utils.convertToMegabites(totalSize);
//	}

}
