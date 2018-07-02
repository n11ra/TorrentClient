package com.rusinov.torrent;

import java.io.File;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;

import com.rusinov.main.DownloadStorage;

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
public class TorrentClient {

	@Autowired
	DownloadStorage downloadStorage;

	public synchronized void dowload(URL torrentURL, File targetStorage) {
		System.out.println("Adding torrent to downloading storage: " + torrentURL.toString());
		downloadStorage.addToDownloading(torrentURL.toString(), targetStorage);

		Storage storage = new FileSystemStorage(targetStorage.toPath());

		BtClient client = Bt.client().storage(storage).torrent(torrentURL).build();
		client.startAsync(state -> {
			// always stop after download
			if (state.getPiecesRemaining() == 0) {
				client.stop();
				System.out.println("Removing torrent from downloading storage: " + torrentURL.toString());
				downloadStorage.removeFromDownloading(torrentURL.toString());
				System.out.println("Moving torrent to downloaded storage: " + torrentURL.toString());
			}
		}, 1000).join();
	}
}
