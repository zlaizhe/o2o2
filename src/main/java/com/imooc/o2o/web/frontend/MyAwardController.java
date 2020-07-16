package com.imooc.o2o.web.frontend;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.imooc.o2o.dto.ModelMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.imooc.o2o.dto.UserAwardMapExecution;
import com.imooc.o2o.entity.Award;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.UserAwardMap;
import com.imooc.o2o.enums.UserAwardMapStateEnum;
import com.imooc.o2o.service.AwardService;
import com.imooc.o2o.service.PersonInfoService;
import com.imooc.o2o.service.UserAwardMapService;
import com.imooc.o2o.util.CodeUtil;
import com.imooc.o2o.util.ShortNetAddressUtil;

@Controller
@RequestMapping("/frontend")
public class MyAwardController {
    @Autowired
    private UserAwardMapService userAwardMapService;
    @Autowired
    private AwardService awardService;
    @Autowired
    private PersonInfoService personInfoService;

    /**
     * 根据顾客奖品映射Id获取单条顾客奖品的映射信息
     */
    @GetMapping("/getawardbyuserawardid")
    @ResponseBody
    private ModelMap getAwardbyId(Long userAwardId) {
        ModelMap modelMap = ModelMap.newInstance();
        // 获取前端传递过来的userAwardId
        // 空值判断
        if (userAwardId == null || userAwardId < 1) {
            modelMap.putErrMsg(false, "empty awardId");
        }
        // 根据Id获取顾客奖品的映射信息，进而获取奖品Id
        UserAwardMap userAwardMap = userAwardMapService.getUserAwardMapById(userAwardId);
        // 根据奖品Id获取奖品信息
        Award award = awardService.getAwardById(userAwardMap.getAward().getAwardId());
        // 将奖品信息和领取状态返回给前端
        modelMap.put("award", award);
        modelMap.put("usedStatus", userAwardMap.getUsedStatus());
        modelMap.put("userAwardMap", userAwardMap);
        return modelMap.putSuccess(true);
    }

    /**
     * 获取顾客的兑换列表
     */
    @GetMapping("/listuserawardmapsbycustomer")
    @ResponseBody
    private ModelMap listUserAwardMapsByCustomer(Integer pageIndex, Integer pageSize,
                                                 Long shopId, String awardName,
                                                 HttpSession session) {
        ModelMap modelMap = ModelMap.newInstance();
        // 空值判断
        if (pageIndex == null || pageIndex < 1) {
            pageIndex = 1;
        }
        if (pageSize == null || pageSize < 1) {
            pageIndex = 3;
        }
        // 从session中获取用户信息
        PersonInfo user = (PersonInfo) session.getAttribute("user");
        // 空值判断，主要确保用户Id为非空
        if (user == null || user.getUserId() == null) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty userId");
        }
        UserAwardMap userAwardMapCondition = new UserAwardMap();
        userAwardMapCondition.setUser(user);
        if (shopId != null && shopId > 0) {
            // 若店铺id为非空，则将其添加进查询条件，即查询该用户在某个店铺的兑换信息
            Shop shop = new Shop();
            shop.setShopId(shopId);
            userAwardMapCondition.setShop(shop);
        }
        if (awardName != null) {
            // 若奖品名为非空，则将其添加进查询条件里进行模糊查询
            Award award = new Award();
            award.setAwardName(awardName);
            userAwardMapCondition.setAward(award);
        }
        // 根据传入的查询条件分页获取用户奖品映射信息
        UserAwardMapExecution ue = userAwardMapService.listUserAwardMap(userAwardMapCondition, pageIndex, pageSize);
        modelMap.put("userAwardMapList", ue.getUserAwardMapList());
        modelMap.put("count", ue.getCount());
        return modelMap.putSuccess(true);
    }

    /**
     * 在线兑换礼品
     *
     * @return
     */
    @PostMapping("/adduserawardmap")
    @ResponseBody
    private ModelMap addUserAwardMap(Long awardId, HttpSession session) {
        ModelMap modelMap = ModelMap.newInstance();
        // 从session中获取用户信息
        PersonInfo user = (PersonInfo) session.getAttribute("user");
        // 从前端请求中获取奖品Id
        // 封装成用户奖品映射对象
        UserAwardMap userAwardMap = compactUserAwardMap4Add(user, awardId);
        // 空值判断
        if (userAwardMap == null) {
            modelMap.putErrMsg(false, "请选择领取的奖品");
        }
        // 添加兑换信息
        UserAwardMapExecution se = userAwardMapService.addUserAwardMap(userAwardMap);
        return se.getState() == UserAwardMapStateEnum.SUCCESS.getState() ?
                modelMap.putSuccess(true) : modelMap.putErrMsg(false, se.getStateInfo());
    }

    // 微信获取用户信息的api前缀
    private static String urlPrefix;
    // 微信获取用户信息的api中间部分
    private static String urlMiddle;
    // 微信获取用户信息的api后缀
    private static String urlSuffix;
    // 微信回传给的响应添加用户奖品映射信息的url
    private static String exchangeUrl;

    @Value("${wechat.prefix}")
    public void setUrlPrefix(String urlPrefix) {
        MyAwardController.urlPrefix = urlPrefix;
    }

    @Value("${wechat.middle}")
    public void setUrlMiddle(String urlMiddle) {
        MyAwardController.urlMiddle = urlMiddle;
    }

    @Value("${wechat.suffix}")
    public void setUrlSuffix(String urlSuffix) {
        MyAwardController.urlSuffix = urlSuffix;
    }

    @Value("${wechat.exchange.url}")
    public void setExchangeUrl(String exchangeUrl) {
        MyAwardController.exchangeUrl = exchangeUrl;
    }

    /**
     * 生成奖品的领取二维码，供操作员扫描，证明已领取，微信扫一扫就能链接到对应的URL里面
     *
     * @param request
     * @param response
     */
    @GetMapping("/generateqrcode4award")
    @ResponseBody
    private void generateQRCode4Product(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 获取前端传递过来的用户奖品映射Id
        Long userAwardId = Long.valueOf(request.getParameter("userAwardId"));
        // 根据Id获取顾客奖品映射实体类对象
        UserAwardMap userAwardMap = userAwardMapService.getUserAwardMapById(userAwardId);
        // 从session中获取顾客的信息
        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
        // 空值判断
        if (userAwardMap != null && user != null && user.getUserId() != null
                && userAwardMap.getUser().getUserId() == user.getUserId()) {
            // 获取当前时间戳，以保证二维码的时间有效性，精确到毫秒
            long timpStamp = System.currentTimeMillis();
            // 将顾客奖品映射id，顾客Id和timestamp传入content，赋值到state中，这样微信获取到这些信息后会回传到用户奖品映射信息的添加方法里
            // 加上aaa是为了一会的在添加信息的方法里替换这些信息使用
            String content = "{aaauserAwardIdaaa:" + userAwardId + ",aaacustomerIdaaa:" + user.getUserId()
                    + ",aaacreateTimeaaa:" + timpStamp + "}";
            // 将content的信息先进行base64编码以避免特殊字符造成的干扰，之后拼接目标URL
            String longUrl = urlPrefix + exchangeUrl + urlMiddle + URLEncoder.encode(content, "UTF-8") + urlSuffix;
            // 将目标URL转换成短的URL
            String shortUrl = ShortNetAddressUtil.getShortURL(longUrl);
            // 调用二维码生成的工具类方法，传入短的URL，生成二维码
            BitMatrix qRcodeImg = CodeUtil.generateQRCodeStream(longUrl, response);
            // 将二维码以图片流的形式输出到前端
            MatrixToImageWriter.writeToStream(qRcodeImg, "png", response.getOutputStream());
        }
    }

    /**
     * 封装用户奖品映射实体类
     *
     * @param user
     * @param awardId
     * @return
     */
    private UserAwardMap compactUserAwardMap4Add(PersonInfo user, Long awardId) {
        UserAwardMap userAwardMap = null;
        // 空值判断
        if (user != null && user.getUserId() != null && awardId != -1) {
            userAwardMap = new UserAwardMap();
            // 根据用户Id获取用户实体类对象
            PersonInfo personInfo = personInfoService.getPersonInfoById(user.getUserId());
            // 根据奖品Id获取奖品实体类对象
            Award award = awardService.getAwardById(awardId);
            userAwardMap.setUser(personInfo);
            userAwardMap.setAward(award);
            Shop shop = new Shop();
            shop.setShopId(award.getShopId());
            userAwardMap.setShop(shop);
            // 设置积分
            userAwardMap.setPoint(award.getPoint());
            userAwardMap.setCreateTime(new Date());
            // 设置兑换状态为已领取
            userAwardMap.setUsedStatus(1);
        }
        return userAwardMap;
    }
}
