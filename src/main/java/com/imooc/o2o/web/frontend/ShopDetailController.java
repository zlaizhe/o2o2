package com.imooc.o2o.web.frontend;

import com.imooc.o2o.dto.ModelMap;
import com.imooc.o2o.dto.ProductExecution;
import com.imooc.o2o.entity.*;
import com.imooc.o2o.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

//店铺详细页面的控制器
@RestController
@RequestMapping("/frontend")
public class ShopDetailController {

    @Autowired
    private ShopService shopService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductCategoryService productCategoryService;

    //获取店铺信息和店铺的商品类别列表
    @GetMapping("/listshopdetailpageinfo")
    private ModelMap listShopDetailPageInfo(Long shopId) {
        ModelMap modelMap = ModelMap.newInstance();
        if (shopId == null) {
            return modelMap.putErrMsg(false, "emtpy shopId");
        }
        Shop shop = shopService.getByShopId(shopId);
        List<ProductCategory> productCategoryList = productCategoryService.getProductCategoryList(shopId);
        modelMap.put("shop", shop);
        modelMap.put("productCategoryList", productCategoryList);
        return modelMap.putSuccess(true);
    }

    //依据查询条件分页列出该商铺下的所有商品
    @GetMapping("/listproductsbyshop")
    private ModelMap listProductsByShop(Long shopId,
                                        Long productCategoryId,
                                        String productName,
                                        Integer pageIndex, Integer pageSize) {
        ModelMap modelMap = ModelMap.newInstance();
        if (shopId == null) {
            return modelMap.putErrMsg(false, "emtpy shopId");
        }
        //设置默认页码和每页条数
        if (pageIndex == null || pageIndex <= 0) {
            pageIndex = 1;
        }
        if (pageSize == null || pageSize <= 0) {
            pageSize = 5;
        }
        //封装组合查询条件
        Product productCondition = new Product();
        productCondition.setProductName(productName);
        Shop shop = new Shop();
        shop.setShopId(shopId);
        productCondition.setShop(shop);
        if (productCategoryId != null) {
            ProductCategory pc = new ProductCategory();
            pc.setProductCategoryId(productCategoryId);
            productCondition.setProductCategory(pc);
        }
        //前端只显示上架的商品
        productCondition.setEnableStatus(1);
        ProductExecution pe = productService.getProductList(productCondition, pageIndex, pageSize);
        modelMap.put("productList", pe.getProductList());
        modelMap.put("count", pe.getCount());
        return modelMap.putSuccess(true);
    }
}
