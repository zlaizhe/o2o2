package com.imooc.o2o.web.frontend;

import com.imooc.o2o.dto.ModelMap;
import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Area;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.ShopCategory;
import com.imooc.o2o.service.AreaService;
import com.imooc.o2o.service.ShopCategoryService;
import com.imooc.o2o.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/frontend")
public class ShopListController {

    @Autowired
    private AreaService areaService;

    @Autowired
    private ShopCategoryService shopCategoryService;

    @Autowired
    private ShopService shopService;


    //返回商品列表中的ShopCategory列表（二级或一级），以及区域信息列表
    @GetMapping("/listshopspageinfo")
    private ModelMap listShopsPageInfo(Long parentId) {
        ModelMap modelMap = ModelMap.newInstance();
        List<ShopCategory> shopCategoryList;
        if (parentId != null) {//有一级分类，查询一级分类下的所有二级分类的商铺
            ShopCategory shopCategoryCondition = new ShopCategory();
            ShopCategory parent = new ShopCategory();
            parent.setShopCategoryId(parentId);
            shopCategoryCondition.setParent(parent);
            shopCategoryList = shopCategoryService.getShopCategoryList(shopCategoryCondition);
        } else {//没有一级分类，查询所有一级分类
            shopCategoryList = shopCategoryService.getShopCategoryList(null);
        }
        modelMap.put("shopCategoryList", shopCategoryList);
        //查询区域信息
        List<Area> areaList = areaService.getAreaList();
        modelMap.put("areaList", areaList);
        return modelMap.putSuccess(true);
    }

    //获取指定条件下的店铺列表
    @GetMapping("/listshops")
    private ModelMap listShopsPageInfo(String shopName,//商铺名（模糊查询）
                                       Long parentId, //一级类别
                                       Long shopCategoryId,//二级类别
                                       Integer areaId,//区域
                                       Integer pageIndex, Integer pageSize) {
        ModelMap modelMap = ModelMap.newInstance();
        if (pageIndex == null || pageIndex <= 0) {
            pageIndex = 1;
        }
        if (pageSize == null || pageSize <= 0) {
            pageSize = 5;
        }
        //封装组合查询条件
        Shop shopCondition = new Shop();
        shopCondition.setShopName(shopName);
        if (parentId != null) {//封装一级类别
            ShopCategory childCategory = new ShopCategory();
            ShopCategory parentCategory = new ShopCategory();
            parentCategory.setShopCategoryId(parentId);
            childCategory.setParent(parentCategory);
            shopCondition.setShopCategory(childCategory);
        }
        if (shopCategoryId != null) {//封装二级类别
            ShopCategory shopCategory = new ShopCategory();
            shopCategory.setShopCategoryId(shopCategoryId);
            shopCondition.setShopCategory(shopCategory);
        }
        if (areaId != null) {//封装地区
            Area area = new Area();
            area.setAreaId(areaId);
            shopCondition.setArea(area);
        }
        shopCondition.setEnableStatus(1);//前端展示的店铺都是审核成功的店铺

        //执行查询
        ShopExecution se = shopService.getShopList(shopCondition, pageIndex, pageSize);
        modelMap.put("shopList", se.getShopList());
        modelMap.put("count", se.getCount());
        return modelMap.putSuccess(true);
    }
}
