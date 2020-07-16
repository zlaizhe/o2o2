package com.imooc.o2o.service.impl;

import com.imooc.o2o.dao.ShopCategoryDao;
import com.imooc.o2o.entity.HeadLine;
import com.imooc.o2o.entity.ShopCategory;
import com.imooc.o2o.service.ShopCategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ShopCategoryServiceImpl implements ShopCategoryService {

    @Autowired
    private ShopCategoryDao shopCategoryDao;

    @Resource
    private RedisTemplate<String, ShopCategory> redisTemplate;

    private static Logger logger = LoggerFactory.getLogger(ShopCategoryServiceImpl.class);

    @Override
    public List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryCondition) {
        String key = SCLISTKEY;
        if (shopCategoryCondition == null) {
            key = key + "_allfirstlevel";//查询条件为空，查询所有一级分类
        } else if (shopCategoryCondition != null && shopCategoryCondition.getParent() != null
                && shopCategoryCondition.getParent().getShopCategoryId() != null) {
            //如果parentId不为空，查询对应一级分类下的二级分类
            key = key + "_parent" + shopCategoryCondition.getParent().getShopCategoryId();
        } else if (shopCategoryCondition != null) {
            //列出所有子类别，不管属于哪个分类
            key = key + "_allsecondlevel";
        }

        List<ShopCategory> shopCategoryList;
        if (!redisTemplate.hasKey(key)) {//缓存不存在key
            shopCategoryList = shopCategoryDao.queryShopCategory(shopCategoryCondition);//从数据库查询
            if (shopCategoryList != null && !shopCategoryList.isEmpty()) {
                redisTemplate.opsForList().rightPushAll(key, shopCategoryList);//存入缓存
                logger.debug("将shopCategoryList存入缓存，key=" + key);
            }
        } else {//缓存存在key
            shopCategoryList = redisTemplate.opsForList().range(key, 0, -1);//从缓存查询
            logger.debug("从缓存查询shopCategoryList，key=" + key);
        }
        return shopCategoryList;
    }
}
