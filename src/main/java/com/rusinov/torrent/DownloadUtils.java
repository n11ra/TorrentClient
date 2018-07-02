package com.rusinov.torrent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.util.FileCopyUtils;

import com.rusinov.main.Application;

public class DownloadUtils {

	public static File downloadFile(URL url, String fileName) throws IOException {
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		File file = new File(Application.DOWNLOAD_DIR + fileName);
		file.getParentFile().mkdirs();
		FileCopyUtils.copy(conn.getInputStream(), new FileOutputStream(file));
		return file;
	}

	public static File downloadFile(InputStream in, String fileName) throws IOException {
		File file = new File(Application.DOWNLOAD_DIR + fileName);
		file.getParentFile().mkdirs();
		FileCopyUtils.copy(in, new FileOutputStream(file));
		return file;
	}

	public static File downloadTorrentFile(String torrentPath, String fileName) throws Exception {
		Map<String, String> queries = splitQuery(new URL(torrentPath));
		String torrentId = queries.get("id");
		if (torrentId == null) {
			throw new Exception("Torrent id not found in link");
		}
		String _fileName = fileName + ".torrent";
		return downloadFile(new URL(Application.ZAMUNDA_DOWNLOAD_PATH + torrentId), _fileName);
	}

	private static Map<String, String> splitQuery(URL url) {
		try {
			Map<String, String> query_pairs = new LinkedHashMap<String, String>();
			String query = url.getQuery();
			String[] pairs = query.split("&");
			for (String pair : pairs) {
				int idx = pair.indexOf("=");

				query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"),
						URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
			}
			return query_pairs;
		} catch (UnsupportedEncodingException e) {
			// should not happen
		}
		return null;
	}

}
