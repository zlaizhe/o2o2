package com.imooc.o2o.web.handler;

import com.imooc.o2o.dto.ModelMap;
import com.imooc.o2o.dto.ProductCategoryExectution;
import com.imooc.o2o.exceptions.*;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * SpringMVC
 * 统一的异常处理器，Controller层出现异常时，响应固定格式JSON数据
 * {"success" : false, "errMsg" : "...", "errCode":500}
 */
@ControllerAdvice("com.imooc.o2o.web")
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ModelMap handle(Exception e) {
        e.printStackTrace();

        ModelMap modelMap = ModelMap.newInstance();
        String errMsg;
        if (e instanceof ShopOperationException) {
            ShopOperationException soe = (ShopOperationException) e;
            errMsg = "INNER ERROR(ShopOperationException):" + soe.getMessage();
        } else if (e instanceof ProductCategoryOperationException) {
            ProductCategoryOperationException psoe = (ProductCategoryOperationException) e;
            errMsg = "INNER ERROR(ProductCategoryOperationException):" + psoe.getMessage();
        } else if (e instanceof ProductOperationException) {
            ProductOperationException poe = (ProductOperationException) e;
            errMsg = "INNER ERROR(ProductOperationException):" + poe.getMessage();
        } else if (e instanceof WechatAuthOperationException) {
            WechatAuthOperationException waoe = (WechatAuthOperationException) e;
            errMsg = "INNER ERROR(WechatAuthOperationException):" + waoe.getMessage();
        } else if (e instanceof LocalAuthOperationException) {
            LocalAuthOperationException laoe = (LocalAuthOperationException) e;
            errMsg = "INNER ERROR(LocalAuthOperationException):" + laoe.getMessage();
        } else {
            errMsg = "INNER ERROR(Other Exception):" + e.getMessage();
        }
        return modelMap.putErrCodeMsg(false, 500, errMsg);
    }
}
