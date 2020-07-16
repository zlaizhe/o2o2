package com.imooc.o2o.web.local;

import javax.servlet.http.HttpSession;

import com.google.code.kaptcha.Constants;
import com.imooc.o2o.dto.ModelMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.imooc.o2o.dto.LocalAuthExecution;
import com.imooc.o2o.entity.LocalAuth;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.enums.LocalAuthStateEnum;
import com.imooc.o2o.service.LocalAuthService;

@RestController
@RequestMapping(path = "/local", method = {RequestMethod.GET, RequestMethod.POST})
public class LocalAuthController {
    @Autowired
    private LocalAuthService localAuthService;

    @PostMapping("/bindlocalauth") //将用户信息与平台帐号绑定（会创建一个新的平台账号存入数据库）
    private ModelMap bindLocalAuth(String verifyCodeActual,
                                   String userName, String password,
                                   HttpSession session) {
        ModelMap modelMap = ModelMap.newInstance();
        // 验证码校验
        if (verifyCodeActual == null ||
                !verifyCodeActual.equals(session.getAttribute(Constants.KAPTCHA_SESSION_KEY))) {
            return modelMap.putErrMsg(false, "输入了错误的验证码");
        }
        // 从session中获取当前用户信息(用户一旦通过微信登录之后，便能获取到用户的信息)
        PersonInfo user = (PersonInfo) session.getAttribute("user");
        // 非空判断，要求帐号密码以及当前的用户session非空
        if (userName == null || password == null || user == null || user.getUserId() == null) {
            return modelMap.putErrMsg(false, "用户名和密码均不能为空");
        }
        // 创建LocalAuth对象并赋值
        LocalAuth localAuth = new LocalAuth();
        localAuth.setUsername(userName);
        localAuth.setPassword(password);
        localAuth.setPersonInfo(user);
        // 绑定帐号
        LocalAuthExecution le = localAuthService.bindLocalAuth(localAuth);
        return le.getState() == LocalAuthStateEnum.SUCCESS.getState() ?
                modelMap.putSuccess(true) : modelMap.putErrMsg(false, le.getStateInfo());
    }

    @PostMapping("/changelocalpwd") // 修改密码
    private ModelMap changeLocalPwd(String verifyCodeActual,
                                    String userName, String password, String newPassword,
                                    HttpSession session) {
        ModelMap modelMap = ModelMap.newInstance();
        // 验证码校验
        if (verifyCodeActual == null ||
                !verifyCodeActual.equals(session.getAttribute(Constants.KAPTCHA_SESSION_KEY))) {
            return modelMap.putErrMsg(false, "输入了错误的验证码");
        }
        // 从session中获取当前用户信息(用户一旦通过微信登录之后，便能获取到用户的信息)
        PersonInfo user = (PersonInfo) session.getAttribute("user");
        // 非空判断，要求帐号新旧密码以及当前的用户session非空，且新旧密码不相同
        if (userName == null && password == null && newPassword == null && user == null && user.getUserId() == null
                || password.equals(newPassword)) {
            return modelMap.putErrMsg(false, "请输入密码");
        }
        // 查看原先帐号，看看与输入的帐号是否一致，不一致则认为是非法操作
        LocalAuth localAuth = localAuthService.getLocalAuthByUserId(user.getUserId());
        if (localAuth == null || !localAuth.getUsername().equals(userName)) {
            return modelMap.putErrMsg(false, "输入的帐号非本次登录的帐号");  // 不一致则直接退出
        }
        // 修改平台帐号的用户密码
        LocalAuthExecution le = localAuthService.modifyLocalAuth(user.getUserId(), userName, password,
                newPassword);
        return le.getState() == LocalAuthStateEnum.SUCCESS.getState() ?
                modelMap.putSuccess(true) : modelMap.putErrMsg(false, le.getStateInfo());
    }

    @PostMapping("/logincheck")//登录
    private ModelMap logincheck(Boolean needVerify, String verifyCodeActual,
                                String userName, String password, String newPassword,
                                HttpSession session) {
        ModelMap modelMap = ModelMap.newInstance();
        // 获取是否需要进行验证码校验的标识符
        if (needVerify != null && needVerify &&
                !verifyCodeActual.equals(session.getAttribute(Constants.KAPTCHA_SESSION_KEY))) {
            return modelMap.putErrMsg(false, "输入了错误的验证码");
        }
        // 非空校验
        if (userName == null || password == null) {
            return modelMap.putErrMsg(false, "用户名和密码均不能为空");
        }
        // 传入帐号和密码去获取平台帐号信息
        LocalAuth localAuth = localAuthService.getLocalAuthByUsernameAndPwd(userName, password);
        if (localAuth == null) {
            return modelMap.putErrMsg(false, "用户名或密码错误");
        }
        //  若能取到帐号信息则登录成功，在session里设置用户信息
        session.setAttribute("user", localAuth.getPersonInfo());
        return modelMap.putSuccess(true);
    }

    @PostMapping("/logout")//当用户点击登出按钮的时候注销session
    private ModelMap logout(HttpSession session) {
        session.setAttribute("user", null); // 将用户session置为空
        return ModelMap.newInstance().putSuccess(true);
    }
}
