package com.imooc.o2o.service;

import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.exceptions.ShopOperationException;

import java.io.InputStream;

public interface ShopService {
    public ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize);

    public Shop getByShopId(long shopId);

    public ShopExecution modifyShop(Shop shop, ImageHolder thumbnail) throws ShopOperationException;

    public ShopExecution addShop(Shop shop, ImageHolder thumbnail) throws ShopOperationException;
}
