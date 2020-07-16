package com.imooc.o2o.web.local;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

//此Controller仅用于路由
@Controller
@RequestMapping(path = "/local", method = RequestMethod.GET)
public class LocalController {
    /**
     * 绑定帐号页路由
     */
    @RequestMapping("/accountbind")
    private String accountbind() {
        return "local/accountbind";
    }

    /**
     * 修改密码页路由
     */
    @RequestMapping("/changepsw")
    private String changepsw() {
        return "local/changepsw";
    }

    /**
     * 登录页路由
     */
    @RequestMapping("/login")
    private String login() {
        return "local/login";
    }
}
