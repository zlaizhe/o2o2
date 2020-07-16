package com.imooc.o2o.web.shopadmin;

import com.imooc.o2o.dto.ModelMap;
import com.imooc.o2o.dto.ProductCategoryExectution;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.enums.ProductCategoryStateEnum;
import com.imooc.o2o.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/shopadmin")
public class ProductCategoryManagementController {

    @Autowired
    private ProductCategoryService productCategoryService;

    @GetMapping("/getproductcategorylist")
    private ModelMap getProductCategoryList(HttpSession session) {
        ModelMap modelMap = ModelMap.newInstance();
        Shop currentShop = (Shop) session.getAttribute("currentShop");
        if (currentShop != null && currentShop.getShopId() > 0) {
            List<ProductCategory> list = productCategoryService.getProductCategoryList(currentShop.getShopId());
            return modelMap.putSuccessData(true, list);
        } else {
            ProductCategoryStateEnum ps = ProductCategoryStateEnum.INNER_ERROR;
            return modelMap.putErrCodeMsg(false, ps.getState(), ps.getStateInfo());
        }
    }

    @PostMapping("/addproductcategorys")//批量添加商品类别，获取请求提交的json格式信息
    private ModelMap addProductCategorys(@RequestBody List<ProductCategory> productCategoryList, HttpSession session) {
        ModelMap modelMap = ModelMap.newInstance();
        if (productCategoryList == null || productCategoryList.isEmpty()) {
            return modelMap.putErrMsg(false, "请至少输入一个商品类别");
        }
        //获取session中当前店铺
        Shop currentShop = (Shop) session.getAttribute("currentShop");
        for (ProductCategory pc : productCategoryList) {
            pc.setShopId(currentShop.getShopId());//设置提交商品类别的店铺Id
        }
        //批量添加商品类别
        ProductCategoryExectution pe = productCategoryService.batchAddProductCategory(productCategoryList);
        return pe.getState() == ProductCategoryStateEnum.SUCCESS.getState() ?
                modelMap.putSuccess(true) : modelMap.putErrMsg(false, pe.getStateInfo());
    }

    @PostMapping("/removeproductcategory")//删除商品类别
    private ModelMap addProductCategorys(Long productCategoryId, HttpSession session) {
        ModelMap modelMap = ModelMap.newInstance();
        if (productCategoryId == null || productCategoryId <= 0) {
            return modelMap.putErrMsg(false, "请至少选择一个商品类别");
        }
        //获取session中当前店铺
        Shop currentShop = (Shop) session.getAttribute("currentShop");
        ProductCategoryExectution pe = productCategoryService.deleteProductCategory(productCategoryId,
                currentShop.getShopId());
        return pe.getState() == ProductCategoryStateEnum.SUCCESS.getState() ?
                modelMap.putSuccess(true) : modelMap.putErrMsg(false, pe.getStateInfo());
    }
}