package com.imooc.o2o.interceptor.shopadmin;

import com.imooc.o2o.entity.PersonInfo;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//店铺管理系统登录拦截器
public class ShopLoginInterceptor extends HandlerInterceptorAdapter {

    //事前拦截
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //从session获取user
        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
        if (user != null && user.getUserId() != null
                && user.getUserType() > 0 && user.getEnableStatus() == 1) {
            return true;//放行
        }
        //若不满足登录验证，则直接重定向到账号登录页面
        response.sendRedirect(request.getContextPath() + "/local/login?userType=2");
        return false;
    }
}
