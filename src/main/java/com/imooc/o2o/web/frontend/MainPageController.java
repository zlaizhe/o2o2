package com.imooc.o2o.web.frontend;

import com.imooc.o2o.dto.ModelMap;
import com.imooc.o2o.entity.HeadLine;
import com.imooc.o2o.entity.ShopCategory;
import com.imooc.o2o.service.HeadLineService;
import com.imooc.o2o.service.ShopCategoryService;
import com.imooc.o2o.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//处理主页前端请求的控制器
@RestController
@RequestMapping("/frontend")
public class MainPageController {
    @Autowired
    private ShopCategoryService shopCategoryService;

    @Autowired
    private HeadLineService headLineService;

    //初始化前端展示系统的主页信息，包括一级店铺类别列表和头条列表
    //注意需要将硬盘上存储主页图片的文件夹D:\\projectdev\\image\\upload部署(在deployment中设置)到tomcat的虚拟路径/upload，否则前端获取不到图片
    @GetMapping("/listmainpageinfo")
    private ModelMap listMainPageInfo() throws IOException {
        ModelMap modelMap = ModelMap.newInstance();
        //获取一级店铺类别列表（parentId为空）
        List<ShopCategory> shopCategoryList = shopCategoryService.getShopCategoryList(null);
        //将所有图片的路径加上ShopCategory图片的基路径
        modelMap.put("shopCategoryList", shopCategoryList);
        //获取可用的头条
        HeadLine headLineCondition = new HeadLine();
        headLineCondition.setEnableStatus(1);
        List<HeadLine> headLineList = headLineService.getHeadLineList(headLineCondition);
        modelMap.put("headLineList", headLineList);
        return modelMap.putSuccess(true);
    }
}
