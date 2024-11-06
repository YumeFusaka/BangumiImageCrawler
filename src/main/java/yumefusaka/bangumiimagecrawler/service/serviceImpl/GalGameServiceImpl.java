package yumefusaka.bangumiimagecrawler.service.serviceImpl;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yumefusaka.bangumiimagecrawler.mapper.GalGameMapper;
import yumefusaka.bangumiimagecrawler.pojo.GalGame;
import yumefusaka.bangumiimagecrawler.service.IGalGameService;

import java.util.List;

@Service
public class GalGameServiceImpl extends ServiceImpl<GalGameMapper, GalGame> implements IGalGameService {

    @Autowired
    private GalGameMapper galGameMapper;

    @Override
    public void updateUrl(String url,Long subjectId) {
        QueryWrapper<GalGame> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("subject_id", subjectId);
        List<GalGame> galGames = galGameMapper.selectList(queryWrapper);
        for(GalGame galGame:galGames){
            if(galGame.getImgUrl()!=null)
                continue;
            galGame.setImgUrl(url);
            galGameMapper.update(galGame,new UpdateWrapper<GalGame>().eq("id",galGame.getId()));
        }
    }

    @Override
    public List<GalGame> getNotUrlGalGame() {
        QueryWrapper<GalGame> queryWrapper = new QueryWrapper<GalGame>().isNull("img_url");
        return galGameMapper.selectList(queryWrapper);
    }
}
