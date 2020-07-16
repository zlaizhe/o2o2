package com.imooc.o2o.web.frontend;

import java.util.Map;

import javax.servlet.http.HttpSession;

import com.imooc.o2o.dto.ModelMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.imooc.o2o.dto.UserShopMapExecution;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.UserShopMap;
import com.imooc.o2o.service.UserShopMapService;

@Controller
@RequestMapping("/frontend")
public class MyShopPointController {
    @Autowired
    private UserShopMapService userShopMapService;

    /**
     * 列出用户的积分情况
     */
    @RequestMapping(value = "/listusershopmapsbycustomer", method = RequestMethod.GET)
    @ResponseBody
    private ModelMap listUserShopMapsByCustomer(Integer pageIndex, Integer pageSize,
                                                           Long shopId, String shopName,
                                                           HttpSession session) {
        ModelMap modelMap = ModelMap.newInstance();
        // 空值判断
        if (pageIndex == null || pageIndex < 1) {
            pageIndex = 1;
        }
        if (pageSize == null || pageSize < 1) {
            pageIndex = 3;
        }
        // 从session中获取顾客信息
        PersonInfo user = (PersonInfo) session.getAttribute("user");
        // 空值判断
        if (user == null || user.getUserId() == null) {
            return modelMap.putErrMsg(false, "empty shopId");
        }
        UserShopMap userShopMapCondition = new UserShopMap();
        userShopMapCondition.setUser(user);
        if (shopId != null && shopId > 0) {
            // 若传入的店铺id不为空，则取出该店铺该顾客的积分情况
            Shop shop = new Shop();
            shop.setShopId(shopId);
            userShopMapCondition.setShop(shop);
        }
        if (shopName != null) {
            // 若商品名为非空，则将其添加进查询条件里进行模糊查询
            Shop shop = new Shop();
            shop.setShopName(shopName);
            userShopMapCondition.setShop(shop);
        }
        // 根据查询条件获取顾客的各店铺积分情况
        UserShopMapExecution ue = userShopMapService.listUserShopMap(userShopMapCondition, pageIndex, pageSize);
        modelMap.put("userShopMapList", ue.getUserShopMapList());
        modelMap.put("count", ue.getCount());
        return modelMap.putSuccess(true);
    }
}
