package com.imooc.o2o.interceptor.shopadmin;

import com.imooc.o2o.entity.Shop;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Objects;

//店家管理系统操作验证拦截器
public class ShopPermissionInterceptor extends HandlerInterceptorAdapter {
    //事前拦截
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        //从session中获取当前用户选择的店铺
        Shop currentShop = (Shop) session.getAttribute("currentShop");
        //获取session中当前用户的可操作店铺列表
        List<Shop> shopList = (List<Shop>) session.getAttribute("shopList");
        //非空判断
        if (currentShop != null && shopList != null) {
            for (Shop shop : shopList) {//如果当前店铺是用户可操作店铺列表中的元素就放行
                if (shop.getShopId() == currentShop.getShopId()) {
                    return true;
                }
            }
        }
        return false;
    }
}
