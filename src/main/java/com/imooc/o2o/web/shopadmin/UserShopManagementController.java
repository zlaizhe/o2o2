package com.imooc.o2o.web.shopadmin;

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

import com.imooc.o2o.dto.UserShopMapExecution;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.UserShopMap;
import com.imooc.o2o.service.UserShopMapService;

@Controller
@RequestMapping("/shopadmin")
public class UserShopManagementController {
    @Autowired
    private UserShopMapService userShopMapService;

    /**
     * 获取某个店铺的用户积分信息
     */
    @GetMapping("/listusershopmapsbyshop")
    @ResponseBody
    private ModelMap listUserShopMapsByShop(Integer pageIndex, Integer pageSize,
                                            String userName,
                                            HttpSession session) {
        ModelMap modelMap = ModelMap.newInstance();
        // 获取当前的店铺信息
        Shop currentShop = (Shop) session.getAttribute("currentShop");
        // 空值校验，主要确保shopId不为空
        if (pageIndex == null || pageIndex < 1) {
            pageIndex = 1;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = 3;
        }
        // 空值判断
        if (currentShop == null || currentShop.getShopId() == null) {
            return modelMap.putErrMsg(false, "empty shopId");
        }
        UserShopMap userShopMapCondition = new UserShopMap();
        // 传入查询条件
        userShopMapCondition.setShop(currentShop);
        if (userName != null) {
            // 若传入顾客名，则按照顾客名模糊查询
            PersonInfo customer = new PersonInfo();
            customer.setName(userName);
            userShopMapCondition.setUser(customer);
        }
        // 分页获取该店铺下的顾客积分列表
        UserShopMapExecution ue = userShopMapService.listUserShopMap(userShopMapCondition, pageIndex, pageSize);
        modelMap.put("userShopMapList", ue.getUserShopMapList());
        modelMap.put("count", ue.getCount());
        return modelMap.putSuccess(true);
    }

}
