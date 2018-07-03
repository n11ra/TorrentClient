package com.rusinov.main;

import java.util.Date;

import com.rusinov.torrent.TorrentClient;

public class DownloadingInfo {

	private Date date;
	private String downloadingProgress;
	private TorrentClient torrentClient;

	public DownloadingInfo(Date date, String downloadingProgress, TorrentClient torrentClient) {
		super();
		this.date = date;
		this.downloadingProgress = downloadingProgress;
		this.torrentClient = torrentClient;
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

	public TorrentClient getTorrentClient() {
		return torrentClient;
	}

	public void setTorrentClient(TorrentClient torrentClient) {
		this.torrentClient = torrentClient;
	}

}
