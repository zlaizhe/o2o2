package com.imooc.o2o.service.impl;

import com.imooc.o2o.dao.ShopAuthMapDao;
import com.imooc.o2o.dao.ShopDao;
import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.ShopAuthMap;
import com.imooc.o2o.enums.ShopStateEnum;
import com.imooc.o2o.exceptions.ShopOperationException;
import com.imooc.o2o.service.ShopService;
import com.imooc.o2o.util.ImageUtil;
import com.imooc.o2o.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class ShopServiceImpl implements ShopService {

    @Autowired
    private ShopDao shopDao;

    @Autowired
    private ShopAuthMapDao shopAuthMapDao;

    @Override
    public Shop getByShopId(long shopId) {
        return shopDao.queryByShodId(shopId);
    }

    @Override
    //有条件的分页查询
    public ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize) {
        int rowIndex = (pageIndex - 1) * pageSize;
        List<Shop> shopList = shopDao.queryShopList(shopCondition, rowIndex, pageSize);
        int count = shopDao.queryShopCount(shopCondition);
        ShopExecution se = new ShopExecution();
        if (shopList != null) {
            se.setShopList(shopList);
            se.setCount(count);
        } else {
            se.setState(ShopStateEnum.INNER_ERROR.getState());
        }
        return se;
    }

    @Override
    @Transactional
    public ShopExecution modifyShop(Shop shop, ImageHolder thumbnail) throws ShopOperationException {
        if (shop == null || shop.getShopId() == null) {
            return new ShopExecution(ShopStateEnum.NULL_SHOP);
        }
        //1.判断是否需要处理新的图片
        if (thumbnail.hasImage()) {
            Shop shoptemp = shopDao.queryByShodId(shop.getShopId());
            if (shoptemp.getShopImg() != null) {
                ImageUtil.deleteFileOrPath(shoptemp.getShopImg());//删除旧的商铺图片
            }
            addShopImg(shop, thumbnail);//添加新的图片，并将新路径存入shop对象中
        }
        //2.更新店铺图片
        shop.setLastEditTime(new Date());
        int effectedNum = shopDao.updateShop(shop);//更新数据库信息
        if (effectedNum <= 0) {
            throw new ShopOperationException("modifyShop error:" + "更新店铺失败");
        }
        Shop retshop = shopDao.queryByShodId(shop.getShopId());//查询更改后的shop信息，返回
        return new ShopExecution(ShopStateEnum.SUCCESS, retshop);
    }

    @Override
    @Transactional
    public ShopExecution addShop(Shop shop, ImageHolder thumbnail) throws ShopOperationException {
        if (shop == null) {//空值判断
            return new ShopExecution(ShopStateEnum.NULL_SHOP);
        }
        shop.setEnableStatus(0);//赋初值（未审核）
        shop.setCreateTime(new Date());
        shop.setLastEditTime(new Date());
        int effectedNum = shopDao.insertShop(shop);//添加店铺
        if (effectedNum <= 0) {
            throw new ShopOperationException("addShop error:" + "店铺创建失败");//抛出运行时异常事务才能回滚
        }
        if (thumbnail.hasImage()) {
            addShopImg(shop, thumbnail);//存储图片，并把图片地址赋值到shop对象里
            effectedNum = shopDao.updateShop(shop); //更新数据库店铺图片地址
            if (effectedNum <= 0) {
                throw new ShopOperationException("addShop error:" + "更新图片地址失败");
            }
        }
        // 执行增加shopAuthMap操作
        ShopAuthMap shopAuthMap = new ShopAuthMap();
        shopAuthMap.setEmployee(shop.getOwner());
        shopAuthMap.setShop(shop);
        shopAuthMap.setTitle("店家");
        shopAuthMap.setTitleFlag(0);
        shopAuthMap.setCreateTime(new Date());
        shopAuthMap.setLastEditTime(new Date());
        shopAuthMap.setEnableStatus(1);
        effectedNum = shopAuthMapDao.insertShopAuthMap(shopAuthMap);
        if (effectedNum <= 0) {
            throw new ShopOperationException("授权创建失败");
        }
        return new ShopExecution(ShopStateEnum.CHECK, shop);//操作成功，返回待审核状态的店铺
    }

    //存储图片，并把图片地址赋值到shop对象里
    private void addShopImg(Shop shop, ImageHolder thumbnail) {
        String dest = PathUtil.getShopImagePath(shop.getShopId());//获取shop图片准备存储目录的相对路径
        String shopImgAddr = ImageUtil.generateThumbnail(thumbnail, dest);//压缩，存储图片，返回新存储图片的相对路径
        shop.setShopImg(shopImgAddr);//将图片路径设置到shop对象里，以便数据库更新商铺图片地址
    }
}
