package com.imooc.o2o.web.shopadmin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

//此Controller仅用于路由
@Controller
@RequestMapping(path = "/shopadmin", method = RequestMethod.GET)
public class ShopAdminController {

    @RequestMapping("/shopoperation")
    public String shopOperation() {
        return "shop/shopoperation";//转发到/WEB-INF/html/shop/shopoperation.html页面
    }

    @RequestMapping("/shoplist")
    public String shopList() {
        return "shop/shoplist";
    }

    @RequestMapping("/shopmanagement")
    public String shopManagement() {
        return "shop/shopmanagement";
    }

    @RequestMapping("/productcategorymanagement")
    public String productCategoryManagement() {
        return "shop/productcategorymanagement";
    }

    @RequestMapping("/productoperation")
    public String productOperation() {
        return "shop/productoperation";
    }

    @RequestMapping("/productmanagement")
    public String productManagement() {
        return "shop/productmanagement";
    }


    @RequestMapping("/shopauthmanagement")
    public String shopAuthManagement() {
        // 转发至店铺授权页面
        return "shop/shopauthmanagement";
    }

    @RequestMapping("/shopauthedit")
    public String shopAuthEdit() {
        // 转发至授权信息修改页面
        return "shop/shopauthedit";
    }

    @RequestMapping(value = "/operationsuccess", method = RequestMethod.GET)
    private String operationSuccess() {
        // 转发至操作失败的页面
        return "shop/operationsuccess";
    }

    @RequestMapping(value = "/operationfail", method = RequestMethod.GET)
    private String operationFail() {
        // 转发至操作成功的页面
        return "shop/operationfail";
    }

    @RequestMapping(value = "/productbuycheck", method = RequestMethod.GET)
    private String productBuyCheck() {
        // 转发至店铺的消费记录的页面
        return "shop/productbuycheck";
    }

    @RequestMapping(value = "/awardmanagement", method = RequestMethod.GET)
    private String awardManagement() {
        // 奖品管理页路由
        return "shop/awardmanagement";
    }

    @RequestMapping(value = "/awardoperation", method = RequestMethod.GET)
    private String awardEdit() {
        // 奖品编辑页路由
        return "shop/awardoperation";
    }

    @RequestMapping(value = "/usershopcheck", method = RequestMethod.GET)
    private String userShopCheck() {
        // 店铺用户积分统计路由
        return "shop/usershopcheck";
    }

    @RequestMapping(value = "/awarddelivercheck", method = RequestMethod.GET)
    private String awardDeliverCheck() {
        // 店铺用户积分兑换路由
        return "shop/awarddelivercheck";
    }

}
