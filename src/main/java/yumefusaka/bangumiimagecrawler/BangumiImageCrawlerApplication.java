package yumefusaka.bangumiimagecrawler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BangumiImageCrawlerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BangumiImageCrawlerApplication.class, args);
    }

}
