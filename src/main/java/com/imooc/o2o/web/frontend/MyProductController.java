package com.imooc.o2o.web.frontend;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.imooc.o2o.dto.ModelMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.imooc.o2o.dto.UserProductMapExecution;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.UserProductMap;
import com.imooc.o2o.service.UserProductMapService;

@Controller
@RequestMapping("/frontend")
public class MyProductController {
    @Autowired
    private UserProductMapService userProductMapService;

    /**
     * 列出某个顾客的商品消费信息
     *
     * @return
     */
    @GetMapping("/listuserproductmapsbycustomer")
    @ResponseBody
    private ModelMap listUserProductMapsByCustomer(Integer pageIndex, Integer pageSize,
                                                              Long shopId, String productName,
                                                              HttpSession session) {
        ModelMap modelMap = ModelMap.newInstance();
        // 空值判断
        if (pageIndex == null || pageIndex < 1) {
            pageIndex = 1;
        }
        if (pageSize == null || pageSize < 1) {
            pageIndex = 3;
        }
        // 从session里获取顾客信息
        PersonInfo user = (PersonInfo) session.getAttribute("user");
        // 空值判断
        if (user == null || user.getUserId() < 1) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty shopId");
        }
        UserProductMap userProductMapCondition = new UserProductMap();
        userProductMapCondition.setUser(user);
        if (shopId != null && shopId > 0) {
            // 若传入店铺信息，则列出某个店铺下该顾客的消费历史
            Shop shop = new Shop();
            shop.setShopId(shopId);
            userProductMapCondition.setShop(shop);
        }
        if (productName != null) {
            // 若传入的商品名不为空，则按照商品名模糊查询
            Product product = new Product();
            product.setProductName(productName);
            userProductMapCondition.setProduct(product);
        }
        // 根据查询条件分页返回用户消费信息
        UserProductMapExecution ue = userProductMapService.listUserProductMap(userProductMapCondition, pageIndex,
                pageSize);
        modelMap.put("userProductMapList", ue.getUserProductMapList());
        modelMap.put("count", ue.getCount());
        return modelMap.putSuccess(true);
    }
}
