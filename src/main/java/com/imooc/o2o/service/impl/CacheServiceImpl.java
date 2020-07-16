package com.imooc.o2o.service.impl;

import com.imooc.o2o.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Set;

@Service
public class CacheServiceImpl implements CacheService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void removeFromCache(String keyPrefix) {//删除redis中以keyPrefix为前缀的键
        Set<String> keys = redisTemplate.keys(keyPrefix + "*");
        redisTemplate.delete(keys);
    }
}
