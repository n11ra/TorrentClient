package com.rusinov.torrent;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;

import com.rusinov.main.DownloadedInfo;
import com.rusinov.main.DownloadingInfo;
import com.rusinov.main.StorageManager;
import com.rusinov.main.Utils;
import com.turn.ttorrent.client.Client;
import com.turn.ttorrent.client.SharedTorrent;

public class TorrentClient {
	
	private String taskName;
	private File torrent;
	private File targetStorage;
	private StorageManager storageManager;
	private Client client;
	private boolean downloaded = false;

	public TorrentClient(String taskName, File torrent, File targetStorage, StorageManager storageManager) {
		this.taskName = taskName;
		this.torrent = torrent;
		this.targetStorage = targetStorage;
		this.storageManager = storageManager;
	}

	public void startDownload() throws UnknownHostException, NoSuchAlgorithmException, IOException {
		if (downloaded) {
			return;
		}

		System.out.println("Adding torrent to downloading storage: " + taskName);
		DownloadingInfo downloadInfo = new DownloadingInfo(new Date(), "0%", this);
		storageManager.addToDownloading(taskName, downloadInfo);

		client = new Client(InetAddress.getLocalHost(), SharedTorrent.fromFile(torrent, targetStorage));

//		client.setMaxDownloadRate(50.0);
//		client.setMaxUploadRate(50.0);

		client.download();

		client.addObserver(new Observer() {
			@Override
			public void update(Observable observable, Object data) {
				Client client = (Client) observable;
				float progress = client.getTorrent().getCompletion();
				downloadInfo.setDownloadingProgress(Utils.DF.format(progress) + "%");
			}
		});

		// To wait for this process to finish, call:
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				client.waitForCompletion();
				stopDownload();
				Utils.findRenameAndMoveSubtitles(targetStorage);
			}
		}).start();
	}

	public void stopDownload() {
		downloaded = true;
		if (client != null) {
			client.stop();
		}
		System.out.println("Removing torrent from downloading storage: " + taskName);
		storageManager.removeFromDownloading(taskName);

		if (targetStorage.listFiles() != null) {
			System.out.println("Moving torrent to downloaded storage: " + taskName);
			storageManager.addToDownloaded(taskName,
					new DownloadedInfo(Arrays.asList(targetStorage.listFiles()), new Date()));
		}
	}

}
