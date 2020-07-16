package com.imooc.o2o.web.shopadmin;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.code.kaptcha.Constants;
import com.imooc.o2o.dto.ModelMap;
import com.imooc.o2o.dto.WechatInfo;
import com.imooc.o2o.util.ShortNetAddressUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.imooc.o2o.dto.ShopAuthMapExecution;
import com.imooc.o2o.dto.UserAccessToken;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.ShopAuthMap;
import com.imooc.o2o.entity.WechatAuth;
import com.imooc.o2o.enums.ShopAuthMapStateEnum;
import com.imooc.o2o.service.PersonInfoService;
import com.imooc.o2o.service.ShopAuthMapService;
import com.imooc.o2o.service.WechatAuthService;
import com.imooc.o2o.util.CodeUtil;
import com.imooc.o2o.util.wechat.WechatUtil;

@Controller
@RequestMapping("/shopadmin")
public class ShopAuthManagementController {
    @Autowired
    private ShopAuthMapService shopAuthMapService;

    @GetMapping("/listshopauthmapsbyshop")
    @ResponseBody
    private ModelMap listShopAuthMapsByShop(Integer pageIndex, Integer pageSize, HttpSession session) {
        ModelMap modelMap = ModelMap.newInstance();
        // 从Session中获取店铺信息
        Shop currentShop = (Shop) session.getAttribute("currentShop");
        // 空值判断
        if (pageIndex == null || pageIndex < 1) {
            pageIndex = 1;
        }
        if (pageSize == null || pageSize < 3) {
            pageSize = 3;
        }
        if (currentShop == null || currentShop.getShopId() == null) {
            return modelMap.putErrMsg(false, "empty pageSize or pageIndex or shopId");
        }
        // 分页取出该店铺下面的授权信息列表
        ShopAuthMapExecution se = shopAuthMapService.listShopAuthMapByShopId(currentShop.getShopId(), pageIndex,
                pageSize);
        modelMap.put("shopAuthMapList", se.getShopAuthMapList());
        modelMap.put("count", se.getCount());
        return modelMap.putSuccess(true);
    }

    @GetMapping("/getshopauthmapbyid")
    @ResponseBody
    private ModelMap getShopAuthMapById(@RequestParam Long shopAuthId) {
        ModelMap modelMap = ModelMap.newInstance();
        // 非空判断
        if (shopAuthId == null || shopAuthId < 0) {
            return modelMap.putErrMsg(false, "empty shopAuthId");
        }
        // 根据前台传入的shopAuthId查找对应的授权信息
        ShopAuthMap shopAuthMap = shopAuthMapService.getShopAuthMapById(shopAuthId);
        modelMap.put("shopAuthMap", shopAuthMap);
        return modelMap.putSuccess(true);
    }

    @PostMapping("/modifyshopauthmap")
    @ResponseBody
    private ModelMap modifyShopAuthMap(Boolean statusChange,// 是授权编辑时候调用还是删除/恢复授权操作的时候调用
                                       String shopAuthMapStr, // 若为前者则进行验证码判断，后者则跳过验证码判断
                                       String verifyCodeActual,
                                       HttpSession session) throws IOException {
        ModelMap modelMap = ModelMap.newInstance();
        // 验证码校验
        if (statusChange != null && !statusChange && (verifyCodeActual != null
                || !verifyCodeActual.equals(session.getAttribute(Constants.KAPTCHA_SESSION_KEY)))) {
            return modelMap.putErrMsg(false, "输入了错误的验证码");
        }
        // 将前台传入的字符串json转换成shopAuthMap实例
        ShopAuthMap shopAuthMap = new ObjectMapper().readValue(shopAuthMapStr, ShopAuthMap.class);
        // 空值判断
        if (shopAuthMap == null || shopAuthMap.getShopAuthId() == null) {
            return modelMap.putErrMsg(false, "请输入要修改的授权信息");
        }
        // 看看被操作的对方是否为店家本身，店家本身不支持修改
        if (!checkPermission(shopAuthMap.getShopAuthId())) {
            return modelMap.putErrMsg(false, "无法对店家本身权限做操作(已是店铺的最高权限)");
        }
        ShopAuthMapExecution se = shopAuthMapService.modifyShopAuthMap(shopAuthMap);
        return se.getState() == ShopAuthMapStateEnum.SUCCESS.getState() ?
                modelMap.putSuccess(true) : modelMap.putErrMsg(false, se.getStateInfo());
    }

    /**
     * 检查被操作的对象是否可修改
     *
     * @param shopAuthId
     * @return
     */
    private boolean checkPermission(Long shopAuthId) {
        ShopAuthMap grantedPerson = shopAuthMapService.getShopAuthMapById(shopAuthId);
        return grantedPerson.getTitleFlag() != 0;// 若是店家本身，不能操作
    }

    // 微信获取用户信息的api前缀
    private static String urlPrefix;
    // 微信获取用户信息的api中间部分
    private static String urlMiddle;
    // 微信获取用户信息的api后缀
    private static String urlSuffix;
    // 微信回传给的响应添加授权信息的url
    private static String authUrl;

    @Value("${wechat.prefix}")
    public void setUrlPrefix(String urlPrefix) {
        ShopAuthManagementController.urlPrefix = urlPrefix;
    }

    @Value("${wechat.middle}")
    public void setUrlMiddle(String urlMiddle) {
        ShopAuthManagementController.urlMiddle = urlMiddle;
    }

    @Value("${wechat.suffix}")
    public void setUrlSuffix(String urlSuffix) {
        ShopAuthManagementController.urlSuffix = urlSuffix;
    }

    @Value("${wechat.auth.url}")
    public void setAuthUrl(String authUrl) {
        ShopAuthManagementController.authUrl = authUrl;
    }

    /**
     * 生成带有URL的二维码，微信扫一扫就能链接到对应的URL里面
     *
     * @param request
     * @param response
     */
    @GetMapping("/generateqrcode4shopauth")
    @ResponseBody
    private void generateQRCode4ShopAuth(HttpServletRequest request, HttpServletResponse response) {
        // 从session里获取当前shop的信息
        Shop shop = (Shop) request.getSession().getAttribute("currentShop");
        if (shop != null && shop.getShopId() != null) {
            // 获取当前时间戳，以保证二维码的时间有效性，精确到毫秒
            long timpStamp = System.currentTimeMillis();
            // 将店铺id和timestamp传入content，赋值到state中，这样微信获取到这些信息后会回传到授权信息的添加方法里
            // 加上aaa是为了一会的在添加信息的方法里替换这些信息使用
            String content = "{aaashopIdaaa:" + shop.getShopId() + ",aaacreateTimeaaa:" + timpStamp + "}";
            try {
                // 将content的信息先进行base64编码以避免特殊字符造成的干扰，之后拼接目标URL
                String longUrl = urlPrefix + authUrl + urlMiddle + URLEncoder.encode(content, "UTF-8") + urlSuffix;
                // 将目标URL转换成短的URL
                String shortUrl = ShortNetAddressUtil.getShortURL(longUrl);
                // 调用二维码生成的工具类方法，传入短的URL，生成二维码
                System.out.println("shortUrl" + shortUrl);
                //BitMatrix qRcodeImg = CodeUtil.generateQRCodeStream(shortUrl, response);
                BitMatrix qRcodeImg = CodeUtil.generateQRCodeStream(longUrl, response);//目前百度的短URL生成服务需要企业认证，先用长URL直接生成二维码
                // 将二维码以图片流的形式输出到前端
                MatrixToImageWriter.writeToStream(qRcodeImg, "png", response.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Autowired
    private WechatAuthService wechatAuthService;
    @Autowired
    private PersonInfoService personInfoService;

    /**
     * 根据微信回传回来的参数添加店铺的授权信息
     */
    @GetMapping("/addshopauthmap")
    private String addShopAuthMap(HttpServletRequest request) throws IOException {
        // 从request里面获取微信用户的信息
        WechatAuth auth = getEmployeeInfo(request);
        if (auth != null) {
            // 根据userId获取用户信息
            PersonInfo user = personInfoService.getPersonInfoById(auth.getPersonInfo().getUserId());
            // 将用户信息添加进user里
            request.getSession().setAttribute("user", user);
            // 解析微信回传过来的自定义参数state,由于之前进行了编码，这里需要解码一下
            String qrCodeinfo = URLDecoder.decode(request.getParameter("state"), "UTF-8");
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
                System.out.println("二维码已过期");
                return "shop/operationfail";
            }

            // 去重校验
            // 获取该店铺下所有的授权信息
            ShopAuthMapExecution allMapList = shopAuthMapService.listShopAuthMapByShopId(wechatInfo.getShopId(), 1, 999);
            List<ShopAuthMap> shopAuthList = allMapList.getShopAuthMapList();
            for (ShopAuthMap sm : shopAuthList) {
                if (sm.getEmployee().getUserId() == user.getUserId()) {
                    System.out.println("该店员已存在");
                    return "shop/operationfail";
                }
            }

            try {
                // 根据获取到的内容，添加微信授权信息
                ShopAuthMap shopAuthMap = new ShopAuthMap();
                Shop shop = new Shop();
                shop.setShopId(wechatInfo.getShopId());
                shopAuthMap.setShop(shop);
                shopAuthMap.setEmployee(user);
                shopAuthMap.setTitle("员工");
                shopAuthMap.setTitleFlag(1);
                ShopAuthMapExecution se = shopAuthMapService.addShopAuthMap(shopAuthMap);
                if (se.getState() == ShopAuthMapStateEnum.SUCCESS.getState()) {
                    return "shop/operationsuccess";
                } else {
                    System.out.println("添加授权失败");
                    return "shop/operationfail";
                }
            } catch (RuntimeException e) {
                e.printStackTrace();
                return "shop/operationfail";
            }
        }
        return "shop/operationfail";
    }

    /**
     * 根据二维码携带的createTime判断其是否超过了10分钟，超过十分钟则认为过期
     *
     * @param wechatInfo
     * @return
     */
    private boolean checkQRCodeInfo(WechatInfo wechatInfo) {
        //空值判断
        if (wechatInfo != null && wechatInfo.getShopId() != null && wechatInfo.getCreateTime() != null) {
            long nowTime = System.currentTimeMillis();
            if ((nowTime - wechatInfo.getCreateTime()) <= 600000) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 根据微信回传的code获取用户信息
     *
     * @param request
     * @return
     */
    private WechatAuth getEmployeeInfo(HttpServletRequest request) {
        String code = request.getParameter("code");
        WechatAuth auth = null;
        if (null != code) {
            UserAccessToken token;
            try {
                token = WechatUtil.getUserAccessToken(code);
                String openId = token.getOpenId();
                request.getSession().setAttribute("openId", openId);
                auth = wechatAuthService.getWechatAuthByOpenId(openId);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return auth;
    }
}
