package com.imooc.o2o.web.frontend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

//此控制器仅用于转发到前端页面
@Controller
@RequestMapping("/frontend")
public class FrontendController {

    @GetMapping("/index")
    private String index() {
        return "frontend/index";
    }

    @GetMapping("/shoplist")
    private String shopList() {
        return "frontend/shoplist";
    }

    @GetMapping("/shopdetail")
    private String shopDetail() {
        return "frontend/shopdetail";
    }

    @GetMapping("/productdetail")
    private String productDetail() {
        return "frontend/productdetail";
    }

    /**
     * 店铺的奖品列表页路由
     *
     * @return
     */
    @GetMapping("/awardlist")
    private String showAwardList() {
        return "frontend/awardlist";
    }

    /**
     * 奖品兑换列表页路由
     *
     * @return
     */
    @GetMapping("/pointrecord")
    private String showPointRecord() {
        return "frontend/pointrecord";
    }

    /**
     * 奖品详情页路由
     *
     * @return
     */
    @GetMapping("/myawarddetail")
    private String showMyAwardDetail() {
        return "frontend/myawarddetail";
    }

    /**
     * 消费记录列表页路由
     *
     * @return
     */
    @GetMapping("/myrecord")
    private String showMyRecord() {
        return "frontend/myrecord";
    }

    /**
     * 用户各店铺积分信息页路由
     *
     * @return
     */
    @GetMapping("/mypoint")
    private String showMyPoint() {
        return "frontend/mypoint";
    }
}
