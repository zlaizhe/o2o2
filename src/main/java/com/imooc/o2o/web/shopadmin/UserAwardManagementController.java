package com.imooc.o2o.web.shopadmin;

import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.http.HttpSession;

import com.imooc.o2o.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.o2o.entity.Award;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.ShopAuthMap;
import com.imooc.o2o.entity.UserAwardMap;
import com.imooc.o2o.entity.WechatAuth;
import com.imooc.o2o.enums.UserAwardMapStateEnum;
import com.imooc.o2o.service.PersonInfoService;
import com.imooc.o2o.service.ShopAuthMapService;
import com.imooc.o2o.service.UserAwardMapService;
import com.imooc.o2o.service.WechatAuthService;
import com.imooc.o2o.util.wechat.WechatUtil;

@Controller
@RequestMapping("/shopadmin")
public class UserAwardManagementController {
    @Autowired
    private UserAwardMapService userAwardMapService;
    @Autowired
    private PersonInfoService personInfoService;
    @Autowired
    private ShopAuthMapService shopAuthMapService;
    @Autowired
    private WechatAuthService wechatAuthService;

    /**
     * 列出某个店铺的用户奖品领取情况列表
     */
    @GetMapping("/listuserawardmapsbyshop")
    @ResponseBody
    private ModelMap listUserAwardMapsByShop(Integer pageIndex,
                                             Integer pageSize,
                                             String awardName,
                                             HttpSession session) {
        ModelMap modelMap = ModelMap.newInstance();
        if (pageIndex == null || pageIndex < 1) {
            pageIndex = 1;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = 3;
        }
        // 从session里获取店铺信息
        Shop currentShop = (Shop) session.getAttribute("currentShop");
        // 获取分页信息
        // 空值判断
        if (currentShop == null || currentShop.getShopId() == null) {
            return modelMap.putErrMsg(false, "empty shopId");
        }
        UserAwardMap userAwardMap = new UserAwardMap();
        userAwardMap.setShop(currentShop);
        // 从请求中获取奖品名
        if (awardName != null) {
            // 如果需要按照奖品名称搜索，则添加搜索条件
            Award award = new Award();
            award.setAwardName(awardName);
            userAwardMap.setAward(award);
        }
        // 分页返回结果
        UserAwardMapExecution ue = userAwardMapService.listReceivedUserAwardMap(userAwardMap, pageIndex, pageSize);
        modelMap.put("userAwardMapList", ue.getUserAwardMapList());
        modelMap.put("count", ue.getCount());
        return modelMap.putSuccess(true);
    }

    /**
     * 操作员扫顾客的奖品二维码并派发奖品，证明顾客已领取过
     */
    @GetMapping("/exchangeaward")
    private String exchangeAward(String state, String code, HttpSession session) throws IOException {
        // 获取负责扫描二维码的店员信息
        WechatAuth auth = getOperatorInfo(code, session);
        if (auth != null) {
            // 通过userId获取店员信息
            PersonInfo operator = personInfoService.getPersonInfoById(auth.getPersonInfo().getUserId());
            // 设置上用户的session
            session.setAttribute("user", operator);
            // 解析微信回传过来的自定义参数state,由于之前进行了编码，这里需要解码一下
            String qrCodeinfo = URLDecoder.decode(state, "UTF-8");
            ObjectMapper mapper = new ObjectMapper();
            WechatInfo wechatInfo = null;
            try {
                // 将解码后的内容用aaa去替换掉之前生成二维码的时候加入的aaa前缀，转换成WechatInfo实体类
                wechatInfo = mapper.readValue(qrCodeinfo.replace("aaa", "\""), WechatInfo.class);
            } catch (Exception e) {
                e.printStackTrace();
                return "shop/operationfail";
            }
            // 校验二维码是否已经过期
            if (!checkQRCodeInfo(wechatInfo)) {
                return "shop/operationfail";
            }
            // 获取用户奖品映射主键
            Long userAwardId = wechatInfo.getUserAwardId();
            // 获取顾客Id
            Long customerId = wechatInfo.getCustomerId();
            // 将顾客信息，操作员信息以及奖品信息封装成userAwardMap
            UserAwardMap userAwardMap = compactUserAwardMap4Exchange(customerId, userAwardId, operator);
            if (userAwardMap != null) {
                try {
                    // 检查该员工是否具有扫码权限
                    if (!checkShopAuth(operator.getUserId(), userAwardMap)) {
                        return "shop/operationfail";
                    }
                    // 修改奖品的领取状态
                    UserAwardMapExecution se = userAwardMapService.modifyUserAwardMap(userAwardMap);
                    if (se.getState() == UserAwardMapStateEnum.SUCCESS.getState()) {
                        return "shop/operationsuccess";
                    }
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return "shop/operationfail";
                }

            }
        }
        return "shop/operationfail";
    }

    /**
     * 获取扫描二维码的店员信息
     *
     * @return
     */
    private WechatAuth getOperatorInfo(String code, HttpSession session) {
        WechatAuth auth = null;
        if (null != code) {
            UserAccessToken token;
            try {
                token = WechatUtil.getUserAccessToken(code);
                String openId = token.getOpenId();
                session.setAttribute("openId", openId);
                auth = wechatAuthService.getWechatAuthByOpenId(openId);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return auth;
    }

    /**
     * 根据二维码携带的createTime判断其是否超过了10分钟，超过十分钟则认为过期
     *
     * @param wechatInfo
     * @return
     */
    private boolean checkQRCodeInfo(WechatInfo wechatInfo) {
        // 空值判断
        return wechatInfo != null && wechatInfo.getUserAwardId() != null &&
				wechatInfo.getCustomerId() != null && wechatInfo.getCreateTime() != null &&
                System.currentTimeMillis() - wechatInfo.getCreateTime() <= 600000;
    }

    /**
     * 封装用户奖品映射实体类，以供扫码使用，主要将其领取状态变为已领取
     *
     * @param customerId
     * @param userAwardId
     * @return
     */
    private UserAwardMap compactUserAwardMap4Exchange(Long customerId, Long userAwardId, PersonInfo operator) {
        UserAwardMap userAwardMap = null;
        // 空值判断
        if (customerId != null && userAwardId != null && operator != null) {
            // 获取原有userAwardMap信息
            userAwardMap = userAwardMapService.getUserAwardMapById(userAwardId);
            userAwardMap.setUsedStatus(1);
            PersonInfo customer = new PersonInfo();
            customer.setUserId(customerId);
            userAwardMap.setUser(customer);
            userAwardMap.setOperator(operator);
        }
        return userAwardMap;
    }

    /**
     * 检查员工是否具有授权权限
     *
     * @param userId
     * @param userAwardMap
     * @return
     */
    private boolean checkShopAuth(long userId, UserAwardMap userAwardMap) {
        // 取出该店铺所有的授权信息
        ShopAuthMapExecution shopAuthMapExecution = shopAuthMapService
                .listShopAuthMapByShopId(userAwardMap.getShop().getShopId(), 1, 1000);
        // 逐条遍历，看看扫描二维码的员工是否具有扫码权限
        for (ShopAuthMap shopAuthMap : shopAuthMapExecution.getShopAuthMapList()) {
            if (shopAuthMap.getEmployee().getUserId() == userId && shopAuthMap.getEnableStatus() == 1) {
                return true;
            }
        }
        return false;
    }
}
