package com.rusinov.torrent;

import java.io.File;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rusinov.main.DownloadedInfo;
import com.rusinov.main.DownloadingInfo;
import com.rusinov.main.StorageManager;

import bt.Bt;
import bt.data.Storage;
import bt.data.file.FileSystemStorage;
import bt.runtime.BtClient;

/**
 * http://atomashpolskiy.github.io/bt/intro/
 * 
 * @author Damyan_Rusinov
 *
 */
@Component
public class TorrentClient {

	private DecimalFormat df = new DecimalFormat("#.##");

	@Autowired
	StorageManager storageManager;

	public synchronized void dowload(String taskName, URL torrentURL, File targetStorage) {
		System.out.println("Adding torrent to downloading storage: " + torrentURL.toString());
		
		DownloadingInfo di = new DownloadingInfo(new Date(), "0%");
		storageManager.addToDownloading(taskName, di);

		Storage storage = new FileSystemStorage(targetStorage.toPath());

		BtClient client = Bt.client().storage(storage).torrent(torrentURL).build();
		client.startAsync(state -> {
			// calc progress every second
			di.setDownloadingProgress(df.format(state.getPiecesRemaining() / state.getPiecesTotal() * 100) + "%");
			// always stop after download
			if (state.getPiecesRemaining() == 0) {
				client.stop();
				System.out.println("Removing torrent from downloading storage: " + torrentURL.toString());
				storageManager.removeFromDownloading(taskName);
				System.out.println("Moving torrent to downloaded storage: " + torrentURL.toString());
				storageManager.addToDownloaded(taskName,
						new DownloadedInfo(Arrays.asList(targetStorage.listFiles()), new Date()));
			}
		}, 1000).join();
	}

}
