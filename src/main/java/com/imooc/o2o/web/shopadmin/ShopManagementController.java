package com.imooc.o2o.web.shopadmin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.code.kaptcha.Constants;
import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ModelMap;
import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Area;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.ShopCategory;
import com.imooc.o2o.enums.ShopStateEnum;
import com.imooc.o2o.exceptions.ShopOperationException;
import com.imooc.o2o.service.AreaService;
import com.imooc.o2o.service.ShopCategoryService;
import com.imooc.o2o.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/shopadmin")
public class ShopManagementController {

    @Autowired
    private ShopService shopService;

    @Autowired
    private ShopCategoryService shopCategoryService;

    @Autowired
    private AreaService areaService;

    @GetMapping("/getshopinitinfo")
    private ModelMap getShopInitInfo() {//注册店铺表单所需的 地区信息 和 商铺类别信息
        ModelMap modelMap = ModelMap.newInstance();
        List<ShopCategory> shopCategoryList = shopCategoryService.getShopCategoryList(new ShopCategory());//只查询出二级类别（parentId非空）
        List<Area> areaList = areaService.getAreaList();
        modelMap.put("shopCategoryList", shopCategoryList);
        modelMap.put("areaList", areaList);
        return modelMap.putSuccess(true);
    }


    @PostMapping("/registershop")
    private ModelMap registerShop(String verifyCodeActual,//验证码
                                  String shopStr,//接收json格式的注册店铺信息
                                  MultipartFile shopImg,//上传文件
                                  HttpSession session) throws IOException {//抛出的异常通过异常处理器JsonExceptionHandler处理
        ModelMap modelMap = ModelMap.newInstance();
        //0.判断验证码
        if (verifyCodeActual == null
                || !verifyCodeActual.equals(session.getAttribute(Constants.KAPTCHA_SESSION_KEY))) {//验证码判断
            return modelMap.putErrMsg(false, "验证码错误");
        }
        //1.接收并转换相应的参数，包括店铺信息以及图片信息
        if (shopImg == null || shopImg.getSize() == 0) {//上传图片相关，要求有上传文件流
            return modelMap.putErrMsg(false, "上传图片不能为空");
        }
        Shop shop = new ObjectMapper().readValue(shopStr, Shop.class);//将json转换为Shop对象，包括封装area和shopCategory属性
        //2.注册店铺
        PersonInfo owner = (PersonInfo) session.getAttribute("user");
        // PersonInfo owner = new PersonInfo();
        //owner.setUserId(1L);//先用id=1，以后从session获取...
        shop.setOwner(owner);
        //店铺缩略图
        ImageHolder imageHolder = new ImageHolder();
        if (shopImg != null && shopImg.getSize() > 0) {//上传了图片
            imageHolder.setImage(shopImg.getInputStream());
            imageHolder.setImageName(shopImg.getOriginalFilename());
        }
        ShopExecution se = shopService.addShop(shop, imageHolder);
        if (se.getState() != ShopStateEnum.CHECK.getState()) {  //2.1注册失败
            return modelMap.putErrMsg(false, se.getStateInfo());
        }
        //2.2注册成功
        @SuppressWarnings("unchecked")
        List<Shop> shopList = (List<Shop>) session.getAttribute("shopList");
        if (shopList == null) {
            shopList = new ArrayList<>();
        }
        shopList.add(se.getShop());
        session.setAttribute("shopList", shopList);//将用户的所有店铺存入session
        return modelMap.putSuccess(true);
    }

    @GetMapping("/getshopbyid")
    private ModelMap getShopById(Long shopId) {
        ModelMap modelMap = ModelMap.newInstance();
        if (shopId == null || shopId < 0) {
            return modelMap.putErrMsg(false, "empty shopId");
        }
        Shop shop = shopService.getByShopId(shopId);
        List<Area> areaList = areaService.getAreaList();
        modelMap.put("shop", shop);
        modelMap.put("areaList", areaList);
        return modelMap.putSuccess(true);
    }

    @PostMapping("/modifyshop")
    private ModelMap modifyShop(String verifyCodeActual,
                                String shopStr, //接收json格式的注册店铺信息
                                MultipartFile shopImg,
                                HttpSession session) throws IOException {
        ModelMap modelMap = ModelMap.newInstance();
        //0.判断验证码
        if (verifyCodeActual == null
                || !verifyCodeActual.equals(session.getAttribute(Constants.KAPTCHA_SESSION_KEY))) {//验证码判断
            return modelMap.putErrMsg(false, "验证码错误");
        }
        //1.接收并转换相应的参数，包括店铺信息以及图片信息
        Shop shop = new ObjectMapper().readValue(shopStr, Shop.class);//将json转换为Shop对象，包括封装area和shopCategory属性
        if (shop == null || shop.getShopId() == null) {
            return modelMap.putErrMsg(false, "请输入店铺ID");
        }
        //2.修改店铺信息
        //上传图片相关
        ImageHolder imageHolder = new ImageHolder();
        if (shopImg != null && shopImg.getSize() > 0) {//上传了图片
            imageHolder.setImage(shopImg.getInputStream());
            imageHolder.setImageName(shopImg.getOriginalFilename());
        }
        ShopExecution se = shopService.modifyShop(shop, imageHolder);
        return se.getState() == ShopStateEnum.SUCCESS.getState() ? //修改成功
                modelMap.putSuccess(true) : modelMap.putErrMsg(false, se.getStateInfo());//修改失败
    }

    @GetMapping("/getshopmanagementinfo")
    private ModelMap getShopManagementInfo(Long shopId, HttpSession session) {
        ModelMap modelMap = ModelMap.newInstance();
        if (shopId == null || shopId <= 0) {//没有请求参数shopId
            Shop currentShop = (Shop) session.getAttribute("currentShop");//没有参数，从session中获取
            if (currentShop == null) {//session中没有
                modelMap.put("redirect", true);//重定向
                modelMap.put("url", "/o2o/shopadmin/shoplist");
            } else {//session有数据，从session获取
                modelMap.put("redirect", false);
                modelMap.put("shopId", currentShop.getShopId());
            }
        } else {//有请求参数shopId
            Shop currentShop = new Shop();
            currentShop.setShopId(shopId);
            session.setAttribute("currentShop", currentShop);//将currentShop存入session
            modelMap.put("redirect", false);
        }
        return modelMap;
    }

    @GetMapping("/getshoplist")
    private ModelMap getShopList(HttpSession session) {
        ModelMap modelMap = ModelMap.newInstance();
        //TODO 以后设计登录功能，从session中获取
//        PersonInfo user = new PersonInfo();
//        user.setUserId(1L);
//        user.setName("临时用户");
//        session.setAttribute("user", user);
        //从session中获取user
        PersonInfo user = (PersonInfo) session.getAttribute("user");
        Shop shopCondition = new Shop();
        shopCondition.setOwner(user);

        ShopExecution se = shopService.getShopList(shopCondition, 1, 100);//一页显示全部
        session.setAttribute("shopList",se.getShopList());//将用户的可操作店铺列表存入session
        modelMap.put("shopList", se.getShopList());
        modelMap.put("user", user);
        return modelMap.putSuccess(true);
    }
}
