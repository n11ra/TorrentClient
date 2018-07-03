package com.rusinov.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(ApplicationConfig.class)
public class Application {

	public static final String ROOT_DIR = "D:\\eclipse-workspace\\TorrentClient\\testFiles";
	public static final String ZAMUNDA_DOWNLOAD_PATH = "http://zamunda.net/magnetlink/download_go.php?m=x&id=";

	public static final String ZAMUNDA_COOKIE = "PHPSESSID=lgkd3qhbc3p6evkr9mjqebbml2; __utmc=100172053; "
			+ "__utmz=100172053.1521606519.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); swidth=1536; "
			+ "sheight=824; uid=700178; pass=d43acbb4c3dfc8a577c2d2bab159ec72; cats=7; periods=31; statuses=1; "
			+ "howmanys=1; g=m; cookieconsent_dismissed=yes; xodbpb=; a=19; russian_lang=no; ismobile=no; "
			+ "__utma=100172053.1167555036.1521606519.1530430888.1530644256.62; __utmt=1; __utmb=100172053.8.10.1530644256";
	
	public static final String SUB_COOKIE = "PHPSESSID=dtseen7h9pugoog2lsu9p8bg12; _c=y; __utmc=210296727; session_id=8c55d0fc042e4b146b61a42cf928ef3e; __utma=210296727.2003444379.1521752884.1529517070.1530655623.10; __utmb=210296727; __utmz=210296727.1530655623.10.10.utmccn=(referral)|utmcsr=zamunda.net|utmcct=/banan|utmcmd=referral; noprpkedvhozafiwrcnt=1; noprpkedvhozafiwrexp=Tue, 03 Jul 2018 22:37:43 GMT";
	
//	public static final String COOKIE = "PHPSESSID=lgkd3qhbc3p6evkr9mjqebbml2; __utmc=100172053; __utmz=100172053.1521606519.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); swidth=1536; sheight=824; uid=700178; pass=d43acbb4c3dfc8a577c2d2bab159ec72; cats=7; periods=31; statuses=1; howmanys=1; g=m; cookieconsent_dismissed=yes; xodbpb=; a=19; russian_lang=no; ismobile=no; __utma=100172053.1167555036.1521606519.1530644256.1530654292.63; __utmt=1; __utmb=100172053.15.10.1530654292";

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
