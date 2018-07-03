package com.rusinov.web;

import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rusinov.main.Application;
import com.rusinov.main.DownloadedInfo;
import com.rusinov.main.DownloadingInfo;
import com.rusinov.main.StorageManager;
import com.rusinov.main.Utils;
import com.rusinov.torrent.TorrentClient;

@RestController
public class RESTController {

	private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

	@Autowired
	private TorrentClient torrentClient;

	@Autowired
	private StorageManager storageManager;

	@RequestMapping(value = { "deleteFile" }, method = RequestMethod.DELETE)
	public void deleteFile(@RequestParam(value = "taskName", required = true) String taskName,
			@RequestParam(value = "filePath", required = false) String filePath) {
		try {
			storageManager.deleteFile(taskName, filePath);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@RequestMapping(value = { "downloadingList" }, method = RequestMethod.GET)
	public ResponseEntity<List<Response>> downloadingList() {
		List<Response> downloadingNowList = new LinkedList<>();
		try {
			for (Entry<String, DownloadingInfo> e : storageManager.getDownloading().entrySet()) {
				Response response = new Response();
				response.setTaskName(e.getKey());
				response.setDate(DATE_FORMATTER.format(e.getValue().getDate()));
				response.setDownloadingProgress(e.getValue().getDownloadingProgress());
				downloadingNowList.add(response);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new ResponseEntity<List<Response>>(downloadingNowList, HttpStatus.OK);
	}

	@RequestMapping(value = { "downloadedList" }, method = RequestMethod.GET)
	public ResponseEntity<List<Response>> downloadedList() {
		List<Response> downloadedList = new LinkedList<>();
		try {
			for (Entry<String, DownloadedInfo> e : storageManager.getDownloaded().entrySet()) {
				Response response = new Response();
				response.setTaskName(e.getKey());
				response.setDate(DATE_FORMATTER.format(e.getValue().getDate()));
				Map<String, String> files = new HashMap<>();
				for (File file : e.getValue().getFiles()) {
					files.put(file.getAbsolutePath(), Utils.convertToHumanReadableScale(file.length()));
				}
				response.setFiles(files);
				downloadedList.add(response);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new ResponseEntity<List<Response>>(downloadedList, HttpStatus.OK);
	}

	@RequestMapping(value = { "/downloadTorrent" }, method = RequestMethod.POST)
	public ResponseEntity<Response> downloadTorrent(@RequestBody Request body) {
		Response response = new Response();

		try {
			// download torrent file
			System.out.println("Downloading torrent Name: " + body.getTaskName());
			System.out.println("Downloading torrent URL: " + body.getTorrentURL());
			System.out.println("Downloading torrent Subs: " + body.getSubtitleURL());

			torrentClient.dowload(body.getTaskName(), new URL(body.getTorrentURL()),
					new File(Application.ROOT_DIR + "/" + body.getTaskName()));

			// TODO: download subs
			// DownloadUtils.downloadFile(file.getInputStream(),
			// getFileName(request))
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}

}
