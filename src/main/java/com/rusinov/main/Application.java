package com.rusinov.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({ ApplicationConfig.class, SecurityConfiguration.class })
public class Application {

	public static final String ROOT_OSMC = "OSMC";
	public static final String ROOT_FLASH = "FLASH";
	
	// https://subsunacs.net/subtitles/Inside_No_9_01x05-127611/

//	private static final String _ROOT_OSMC = "/home/osmc/Movies";
//	private static final String _ROOT_FLASH = "/media/FLASH/Movies";
	
//	private static final String _ROOT_OSMC = "D:\\Develop\\TorrentClient\\OSMC";
//	private static final String _ROOT_FLASH = "D:\\Develop\\TorrentClient\\FLASH";

	private static final String _ROOT_OSMC = "D:\\eclipse-workspace\\TorrentClient\\OSMC";
	private static final String _ROOT_FLASH = "D:\\eclipse-workspace\\TorrentClient\\FLASH";

	public static final String ZAMUNDA_COOKIE = "PHPSESSID=lgkd3qhbc3p6evkr9mjqebbml2; __utmc=100172053; "
			+ "__utmz=100172053.1521606519.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); swidth=1536; "
			+ "sheight=824; uid=700178; pass=d43acbb4c3dfc8a577c2d2bab159ec72; cats=7; periods=31; statuses=1; "
			+ "howmanys=1; g=m; cookieconsent_dismissed=yes; xodbpb=; a=19; russian_lang=no; ismobile=no; "
			+ "__utma=100172053.1167555036.1521606519.1530430888.1530644256.62; __utmt=1; __utmb=100172053.8.10.1530644256";

	private static String rootDir = _ROOT_OSMC;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		
//		boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
	}

	public static void switchRootDir(String root) {
		switch (root) {
		case ROOT_OSMC:
			rootDir = _ROOT_OSMC;
			break;
		case ROOT_FLASH:
			rootDir = _ROOT_FLASH;
			break;
		}
	}

	public static String getRootDir() {
		return rootDir;
	}

	public static String getRootDirForWeb() {
		if (rootDir.equals(_ROOT_OSMC)) {
			return ROOT_OSMC;
		}
		return ROOT_FLASH;
	}

}
