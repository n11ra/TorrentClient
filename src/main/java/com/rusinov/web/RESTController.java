package com.rusinov.web;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.rusinov.torrent.DownloadUtils;
import com.rusinov.torrent.TorrentClient;

@RestController
public class RESTController {

	@Resource
	protected HttpServletRequest request;

	@Resource
	protected HttpServletResponse response;

	@RequestMapping(value = { "hello" }, method = RequestMethod.GET)
	public ResponseEntity<String> hello() {
		return new ResponseEntity<String>("hello", HttpStatus.OK);
	}

	@RequestMapping(value = { "hello" }, method = RequestMethod.POST)
	public ResponseEntity<String> downloadTorrent() {
		return new ResponseEntity<String>("hello", HttpStatus.OK);
	}

	@RequestMapping(value = { "upload" }, method = RequestMethod.POST)
	public ResponseEntity<String> upload(@RequestParam("torrentName") String torrentName,
			@RequestParam("torrentPath") String torrentPath,
			@RequestParam(value = "file", required = false) MultipartFile file) throws Exception {

		// download torrent file
		File torrent = DownloadUtils.downloadTorrentFile(torrentPath, torrentName);
		// download additional file (subtitles)
		if (file != null && !file.isEmpty()) {
			DownloadUtils.downloadFile(file.getInputStream(), getFileName(request));
		}

		TorrentClient.dowload(torrent);

		response.sendRedirect("/");
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	private String getFileName(HttpServletRequest request) throws IOException, ServletException {
		for (Part part : request.getParts()) {
			if ("file".equals(part.getName())) {
				Collection<String> headers = part.getHeaders("content-disposition");
				if (headers == null || headers.isEmpty())
					continue;
				return parseFileName(headers.iterator().next());
			}
		}
		return "default";
	}

	private String parseFileName(String contentDisposition) {
		// form-data; name=\"file\"; filename=\"nameeeee.jpg\"
		int begin = contentDisposition.indexOf("filename=") + "filename=".length() + 1;
		return contentDisposition.substring(begin, contentDisposition.length() - 1);
	}
}
