package com.imooc.o2o.web.shopadmin;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.google.code.kaptcha.Constants;
import com.imooc.o2o.dto.ModelMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.o2o.dto.AwardExecution;
import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.entity.Award;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.enums.AwardStateEnum;
import com.imooc.o2o.service.AwardService;
import com.imooc.o2o.util.CodeUtil;

@Controller
@RequestMapping("/shopadmin")
public class AwardManagementController {
    @Autowired
    private AwardService awardService;

    /**
     * 通过店铺id获取该店铺下的奖品列表
     */
    @GetMapping("/listawardsbyshop")
    @ResponseBody
    private Map<String, Object> listAwardsByShop(
            Integer pageIndex, Integer pageSize,
            String awardName,
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
        if (currentShop == null || currentShop.getShopId() == null) { // 空值校验
            return modelMap.putErrMsg(false, "empty shopId");
        }
        // 拼接查询条件
        Award awardCondition = compactAwardCondition4Search(currentShop.getShopId(), awardName);
        // 根据查询条件分页获取奖品列表即总数
        AwardExecution ae = awardService.getAwardList(awardCondition, pageIndex, pageSize);
        modelMap.put("awardList", ae.getAwardList());
        modelMap.put("count", ae.getCount());
        return modelMap.putSuccess(true);
    }

    /**
     * 通过商品id获取奖品信息
     */
    @GetMapping("/getawardbyid")
    @ResponseBody
    private ModelMap getAwardbyId(Long awardId) {
        ModelMap modelMap = ModelMap.newInstance();
        // 空值判断
        if (awardId == null) {
            return modelMap.putErrMsg(false, "empty awardId");

        }
        // 根据传入的Id获取奖品信息并返回
        Award award = awardService.getAwardById(awardId);
        modelMap.put("award", award);
        return modelMap.putSuccess(true);
    }

    @PostMapping("/addaward")
    @ResponseBody
    private Map<String, Object> addAward(String verifyCodeActual,
                                         String awardStr,
                                         MultipartFile thumbnail,
                                         HttpSession session) throws IOException {
        ModelMap modelMap = ModelMap.newInstance();
        // 验证码校验
        if (verifyCodeActual == null
                || !verifyCodeActual.equals(session.getAttribute(Constants.KAPTCHA_SESSION_KEY))) {//验证码判断
            return modelMap.putErrMsg(false, "验证码错误");
        }
        // 接收前端参数的变量的初始化，包括奖品，缩略图
        // 咱们的请求中都带有multi字样，因此没法过滤，只是用来拦截外部非图片流的处理，
        // 里边有缩略图的空值判断，请放心使用
        ImageHolder imageHolder = null;
        if (thumbnail != null) {
            imageHolder = new ImageHolder(thumbnail.getOriginalFilename(), thumbnail.getInputStream());
        }
        // 将前端传入的awardStr转换成奖品对象
        Award award = new ObjectMapper().readValue(awardStr, Award.class);
        // 空值判断
        if (award == null || imageHolder == null) {
            modelMap.putErrMsg(false, "请输入奖品信息");
        }
        // 从session中获取当前店铺的Id并赋值给award，减少对前端数据的依赖
        Shop currentShop = (Shop) session.getAttribute("currentShop");
        award.setShopId(currentShop.getShopId());
        // 添加award
        AwardExecution ae = awardService.addAward(award, imageHolder);
        return ae.getState() == AwardStateEnum.SUCCESS.getState() ?
                modelMap.putSuccess(true) : modelMap.putErrMsg(false, ae.getStateInfo());
    }

    @PostMapping("/modifyaward")
    @ResponseBody
    private ModelMap modifyAward(Boolean statusChange,
                                 String verifyCodeActual,
                                 String awardStr,
                                 MultipartFile thumbnail,
                                 HttpSession session) throws IOException {
        ModelMap modelMap = ModelMap.newInstance();
        // 根据传入的状态值决定是否跳过验证码校验
        if (!statusChange && (verifyCodeActual == null
                || !verifyCodeActual.equals(session.getAttribute(Constants.KAPTCHA_SESSION_KEY)))) {//验证码判断
            return modelMap.putErrMsg(false, "验证码错误");
        }
        // 接收前端参数的变量的初始化，包括商品，缩略图
        ImageHolder imageHolder = null;
        if (thumbnail != null) {
            imageHolder = new ImageHolder(thumbnail.getOriginalFilename(), thumbnail.getInputStream());
        }
        // 咱们的请求中都带有multi字样，因此没法过滤，只是用来拦截外部非图片流的处理，
        // 里边有缩略图的空值判断，请放心使用
        // 尝试获取前端传过来的表单string流并将其转换成Product实体类
        Award award = new ObjectMapper().readValue(awardStr, Award.class);
        // 空值判断
        if (award == null) {
            return modelMap.putErrMsg(false, "请输入商品信息");
        }
        // 从session中获取当前店铺的Id并赋值给award，减少对前端数据的依赖
        Shop currentShop = (Shop) session.getAttribute("currentShop");
        award.setShopId(currentShop.getShopId());
        AwardExecution pe = awardService.modifyAward(award, imageHolder);
        return pe.getState() == AwardStateEnum.SUCCESS.getState() ?
                modelMap.putSuccess(true) : modelMap.putErrMsg(false, pe.getStateInfo());
    }

    /**
     * 封装商品查询条件到award实例中
     *
     * @param shopId
     * @param awardName
     * @return
     */
    private Award compactAwardCondition4Search(long shopId, String awardName) {
        Award awardCondition = new Award();
        awardCondition.setShopId(shopId);
        if (awardName != null) {
            awardCondition.setAwardName(awardName);
        }
        return awardCondition;
    }
}
