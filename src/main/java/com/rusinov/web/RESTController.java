package com.rusinov.web;

import java.io.File;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.rusinov.main.Application;
import com.rusinov.main.DownloadStorage;
import com.rusinov.torrent.TorrentClient;

@RestController
public class RESTController {

	@Autowired
	private TorrentClient torrentClient;

	@Autowired
	private DownloadStorage downloadStorage;

	@Resource
	private HttpServletRequest request;

	@Resource
	private HttpServletResponse response;

	@RequestMapping(value = { "hello" }, method = RequestMethod.GET)
	public ResponseEntity<String> hello() {
		return new ResponseEntity<String>("hello", HttpStatus.OK);
	}

	@RequestMapping(value = { "downloadingNow" }, method = RequestMethod.GET)
	public ResponseEntity<List<Response>> downloadingNow() {

		Response res1 = new Response();
		res1.setDownloadProgress("100%");
		res1.setTaskName("task1");

		Response res2 = new Response();
		res2.setDownloadProgress("90%");
		res2.setTaskName("task2");

		List<Response> downloadingNowList = new LinkedList<>();
		downloadingNowList.add(res1);
		downloadingNowList.add(res2);

		return new ResponseEntity<List<Response>>(downloadingNowList, HttpStatus.OK);

	}

	@RequestMapping(value = { "downloadedList" }, method = RequestMethod.GET)
	public ResponseEntity<List<Response>> downloadedList() {
		
		Response res1 = new Response();
		res1.setDownloadProgress("100%");
		res1.setTaskName("task1");

		Response res2 = new Response();
		res2.setDownloadProgress("90%");
		res2.setTaskName("task2");

		List<Response> downloadList = new LinkedList<>();
		downloadList.add(res1);
		downloadList.add(res2);
		
		return new ResponseEntity<List<Response>>(downloadList, HttpStatus.OK);
	}

	@RequestMapping(value = { "/downloadTorrent" }, method = RequestMethod.POST)
	public ResponseEntity<Response> downloadTorrent(@RequestBody Request body) {
		Response response = new Response();

		try {
			// download torrent file
			System.out.println("Downloading torrent Name: " + body.getTaskName());
			System.out.println("Downloading torrent URL: " + body.getTorrentURL());
			System.out.println("Downloading torrent Subs: " + body.getSubtitleURL());

			torrentClient.dowload(new URL(body.getTorrentURL()),
					new File(Application.DOWNLOAD_DIR + "/" + body.getTaskName()));
			
			// TODO: download subs
			// DownloadUtils.downloadFile(file.getInputStream(), getFileName(request))
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}

}
