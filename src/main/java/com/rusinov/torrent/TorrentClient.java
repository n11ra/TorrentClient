package com.rusinov.torrent;

import java.io.File;
import java.net.URL;

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

	public static void dowload(URL torrentURL, File targetStorage) {

		Storage storage = new FileSystemStorage(targetStorage.toPath());

		BtClient client = Bt.client().storage(storage).torrent(torrentURL).build();

		client.startAsync(state -> {
			// always stop after download
			if (state.getPiecesRemaining() == 0) {
				client.stop();
			}
		}, 1000).join();
	}
}
