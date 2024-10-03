package yumefusaka.bangumiimagecrawler.service;


import com.baomidou.mybatisplus.extension.service.IService;
import yumefusaka.bangumiimagecrawler.pojo.GalGame;

import java.util.List;

public interface IGalGameService extends IService<GalGame> {
    void updateUrl(String url,Long subjectId);

    List<GalGame> getNotUrlGalGame();
}
