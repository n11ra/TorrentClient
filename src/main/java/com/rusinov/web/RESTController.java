package com.rusinov.web;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rusinov.main.Application;
import com.rusinov.main.DownloadedInfo;
import com.rusinov.main.StorageManager;
import com.rusinov.main.Utils;

@RestController
public class RESTController {

	private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

	@Resource
	private StorageManager storageManager;

	@RequestMapping(value = { "reloadStorage" }, method = RequestMethod.GET)
	public void reloadStorage() {
		try {
			storageManager.loadStorage();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = { "rootDir" }, method = RequestMethod.GET)
	public String getRootDir() {
		try {
			return Application.getRootDirForWeb();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping(value = { "setRootDir" }, method = RequestMethod.GET)
	public void setRootDir(@RequestParam(value = "rootDir", required = true) String rootDir) {
		try {
			Application.switchRootDir(rootDir);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				storageManager.loadStorage();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@RequestMapping(value = { "findRenameAndMoveSubtitles" }, method = RequestMethod.GET)
	public void findRenameAndMoveSubtitles(@RequestParam(value = "taskName", required = true) String taskName) {
		try {
			Utils.findRenameAndMoveSubtitles(new File(Application.getRootDir() + "/" + taskName));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				storageManager.loadStorage();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@RequestMapping(value = { "deleteFile" }, method = RequestMethod.DELETE)
	public void deleteFile(@RequestParam(value = "taskName", required = true) String taskName,
			@RequestParam(value = "filePath", required = false) String filePath) {
		try {
			storageManager.deleteFile(taskName, filePath);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
			e.printStackTrace();
		}

		return new ResponseEntity<List<Response>>(downloadedList, HttpStatus.OK);
	}

	@RequestMapping(value = { "/downloadTorrent" }, method = RequestMethod.POST)
	public ResponseEntity<Response> downloadTorrent(@RequestBody Request body) {
		Response response = new Response();

		try {
			// download torrent file
			System.out.println("Downloading torrent Name:" + body.getTaskName() + " URL:" + body.getTorrentURL()
					+ " Subs: " + body.getSubtitleURL());

			File taskDir = new File(Application.getRootDir() + "/" + body.getTaskName());
			if (!taskDir.exists()) {
				taskDir.mkdirs();
			}

			// download and unzip/unrar subs
			try {
				if (body.getSubtitleURL().trim().length() != 0) {
					File subs = Utils.downloadFile(new URL(body.getSubtitleURL()), body.getTaskName(), null);
					Utils.unarchive(subs);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (body.getTorrentURL().trim().length() == 0) {
				return null;
			}
			// download torrent file
			File torrent = Utils.downloadZamundaTorrent(new URL(body.getTorrentURL()), body.getTaskName(), Application.ZAMUNDA_COOKIE);
			if(torrent != null && torrent.getAbsolutePath().endsWith("torrent")) {
				Utils.startTorrentDownload(body.getTaskName(), torrent);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				storageManager.loadStorage();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}

}
