package com.imooc.o2o.web.wechat;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.imooc.o2o.dto.UserAccessToken;
import com.imooc.o2o.dto.WechatAuthExecution;
import com.imooc.o2o.dto.WechatUser;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.WechatAuth;
import com.imooc.o2o.enums.WechatAuthStateEnum;
import com.imooc.o2o.service.PersonInfoService;
import com.imooc.o2o.service.WechatAuthService;
import com.imooc.o2o.util.wechat.WechatUtil;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 获取关注公众号之后的微信用户信息的接口，如果在微信浏览器里访问
 * https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxd7f6c5b8899fba83&redirect_uri=http://o2o.yitiaojieinfo.com/o2o/wechatlogin/logincheck&role_type=1&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect
 * 则这里将会获取到code,之后再可以通过code获取到access_token 进而获取到用户信息
 * <p>
 * https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx6b816d37665d3835&redirect_uri=http://182.92.57.21/o2o/wechatlogin/logincheck&role_type=1&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect
 *
 * @author xiangze
 */
@Controller
@RequestMapping("wechatlogin")
public class WechatLoginController {

    private static Logger log = LoggerFactory.getLogger(WechatLoginController.class);
    private static final String FRONTEND = "1";
    private static final String SHOPEND = "2";
    @Autowired
    private PersonInfoService personInfoService;
    @Autowired
    private WechatAuthService wechatAuthService;

    @RequestMapping(value = "/logincheck", method = RequestMethod.GET)
    public String doGet(String code,// 获取微信公众号传输过来的code,通过code可获取access_token,进而获取用户信息
                        @RequestParam("state") String roleType,  // 这个state可以用来传我们自定义的信息，方便程序调用，这里也可以不用
                        HttpSession session) {
        log.debug("weixin login get...");
        log.debug("weixin login code:" + code);
        WechatUser user = null;
        String openId = null;
        WechatAuth auth = null;
        if (null != code) {
            UserAccessToken token;
            try {
                // 通过code获取access_token
                token = WechatUtil.getUserAccessToken(code);
                log.debug("weixin login token:" + token.toString());
                // 通过token获取accessToken
                String accessToken = token.getAccessToken();
                // 通过token获取openId
                openId = token.getOpenId();
                // 通过access_token和openId获取用户昵称等信息
                user = WechatUtil.getUserInfo(accessToken, openId);
                log.debug("weixin login user:" + user.toString());
                session.setAttribute("openId", openId);
                auth = wechatAuthService.getWechatAuthByOpenId(openId);
            } catch (IOException e) {
                log.error("error in getUserAccessToken or getUserInfo or findByOpenId: " + e.toString());
                e.printStackTrace();
            }
        }
        // 若微信帐号为空则需要注册微信帐号，同时注册用户信息
        if (auth == null) {
            PersonInfo personInfo = WechatUtil.getPersonInfoFromRequest(user);
            auth = new WechatAuth();
            auth.setOpenId(openId);
            int type = FRONTEND.equals(roleType) ? 1 : 2;
            personInfo.setUserType(type);
            auth.setPersonInfo(personInfo);
            WechatAuthExecution we = wechatAuthService.register(auth);
            if (we.getState() != WechatAuthStateEnum.SUCCESS.getState()) {
                return null;
            }
            personInfo = personInfoService.getPersonInfoById(auth.getPersonInfo().getUserId());//注册成功后，重新查询用户信息存入session
            auth.setPersonInfo(personInfo);
        }
        session.setAttribute("user", auth.getPersonInfo());

        // 若用户点击的是前端展示系统按钮则进入前端展示系统
        return FRONTEND.equals(roleType) ? "frontend/index" : "shop/shoplist";
    }

    public static void main(String[] args) {
        String url1 = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx6b816d37665d3835&redirect_uri=http://182:92:57:21/o2o/shopadmin/addshopauthmap&role_type=1&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect";
        String url2 = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx6b816d37665d3835&redirect_uri=http://182.92.57.21/o2o/shopadmin/addshopauthmap&role_type=1&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect";
        System.out.println(url1.equals(url2));
    }
}
