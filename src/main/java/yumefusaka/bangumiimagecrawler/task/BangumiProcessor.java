package yumefusaka.bangumiimagecrawler.task;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.processor.PageProcessor;

import yumefusaka.bangumiimagecrawler.pojo.GalGame;
import yumefusaka.bangumiimagecrawler.service.IGalGameService;
import yumefusaka.bangumiimagecrawler.utils.BeanUtils;


import java.util.List;


@Component
public class BangumiProcessor implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(0).setTimeOut(3000).setCharset("UTF-8").setUserAgent(
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/129.0.0.0 Safari/537.36")
            .addCookie("chii_cookietime","0")
            .addCookie("chii_auth","719hWEfc4pTTfJzb%2FHueRPMAAYvNFBNVoYXRXE9SlydLqVUNgK5ts22Xyq%2Fj5Vsf8kPf6yvI5lKRIVo39hBUWQYBbVowUELsoff%2B")
            .addCookie("chii_sec_id","71FmVUrZ5pjGe8%2FR%2B3CUIaN1ANyyaXV0q6S5BGU")
            .addCookie("chii_theme","light")
            .addCookie("chii_sid","N01klV");

    @Override
    public Site getSite() {
        return site;
    }

    @Override
    public void process(Page page) {
        String imageUrl = page.getHtml().css("a.thickbox.cover img", "src").get();
        Long subjectId  = Long.valueOf(page.getUrl().toString().replaceAll(".*subject/(\\d+)", "$1"));
        if(imageUrl==null){
            return;
        }
        imageUrl = "https:" + imageUrl;
        saveUrl(imageUrl,subjectId);
    }

    public void saveUrl(String url,Long subjectId) {
        IGalGameService galGameService = BeanUtils.getBean(IGalGameService.class);
        galGameService.updateUrl(url,subjectId);
    }


    private String url = "https://bangumi.tv/subject/";

    @Autowired
    private BangumiPipeline bangumiPipeline;

    @Scheduled(initialDelay = 1000, fixedDelay = 100 * 1000)
    public void process() {
        HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
        IGalGameService galGameService = BeanUtils.getBean(IGalGameService.class);
        List<GalGame> galGameList = galGameService.getNotUrlGalGame();
        List<Long> subjectIds = galGameList.stream().map(GalGame::getSubjectId).toList();
        for(Long subjectId:subjectIds){
            Spider.create(new BangumiProcessor())
                    .addUrl(url + subjectId)
                    .thread(10)
                    .setDownloader(httpClientDownloader)
                    .addPipeline(bangumiPipeline)
                    .run();
        }
    }
}
