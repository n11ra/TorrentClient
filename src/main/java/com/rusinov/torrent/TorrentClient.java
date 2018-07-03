package com.rusinov.torrent;

import java.io.File;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Date;

import com.rusinov.main.DownloadedInfo;
import com.rusinov.main.DownloadingInfo;
import com.rusinov.main.StorageManager;

import bt.Bt;
import bt.data.Storage;
import bt.data.file.FileSystemStorage;
import bt.runtime.BtClient;

public class TorrentClient {

	private DecimalFormat df = new DecimalFormat("#.##");

	private String taskName;
	private URL torrentURL;
	private File targetStorage;
	private BtClient client;
	private StorageManager storageManager;

	public TorrentClient(String taskName, URL torrentURL, File targetStorage, StorageManager storageManager) {
		this.taskName = taskName;
		this.torrentURL = torrentURL;
		this.targetStorage = targetStorage;
		this.storageManager = storageManager;
	}

	public void startDownload() {
		System.out.println("Adding torrent to downloading storage: " + taskName);

		DownloadingInfo downloadInfo = new DownloadingInfo(new Date(), "0%", this);
		storageManager.addToDownloading(taskName, downloadInfo);

		Storage storage = new FileSystemStorage(targetStorage.toPath());

		client = Bt.client().storage(storage).torrent(torrentURL).build();
		client.startAsync(state -> {
			// calc progress every second
			downloadInfo
					.setDownloadingProgress(df.format(state.getPiecesRemaining() / state.getPiecesTotal() * 100) + "%");
			// always stop after download
			if (state.getPiecesRemaining() == 0) {
				stopDownload();
			}
		}, 1000).join();
	}

	public void stopDownload() {
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

	public static void main(String[] args) {
		// TODO: test it
	}

}
