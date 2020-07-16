package com.imooc.o2o.service.impl;

import com.imooc.o2o.dao.HeadLineDao;
import com.imooc.o2o.entity.HeadLine;
import com.imooc.o2o.service.HeadLineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@Service
public class HeadLineServiceImpl implements HeadLineService {

    @Autowired
    private HeadLineDao headLineDao;

    @Resource
    private RedisTemplate<String, HeadLine> redisTemplate;

    private static Logger logger = LoggerFactory.getLogger(HeadLineServiceImpl.class);

    @Override
    public List<HeadLine> getHeadLineList(HeadLine headLineCondition) {
        String key = HLLISTKEY;
        List<HeadLine> headLineList;
        if (headLineCondition != null && headLineCondition.getEnableStatus() != null) {
            key = key + "_" + headLineCondition.getEnableStatus();//将不同状态头条分成不同key
        }
        if (!redisTemplate.hasKey(key)) {//缓存不存在key
            headLineList = headLineDao.queryHeadLine(headLineCondition);//从数据库查询
            if (headLineList != null && !headLineList.isEmpty()) {
                redisTemplate.opsForList().rightPushAll(key, headLineList);//存入缓存
                logger.debug("将headLineList存入缓存, key=" + key);
            }
        } else {//缓存存在key
            headLineList = redisTemplate.opsForList().range(key, 0, -1);//从缓存查询
            logger.debug("从缓存查询headLineList, key=" + key);
        }
        return headLineList;
    }
}
