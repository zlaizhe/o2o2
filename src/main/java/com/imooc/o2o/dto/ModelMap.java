package com.imooc.o2o.dto;

import java.util.HashMap;

//用于Controller响应json数据，为简化Controller操作
public class ModelMap extends HashMap<String, Object> {
    //不允许使用构造方法创建ModeMap
    private ModelMap() {
    }

    //使用这个方法创建ModeMap
    public static ModelMap newInstance() {
        return new ModelMap();
    }

    //存入成功标志后返回（仅供响应成功时使用）
    public ModelMap putSuccess(boolean success) {
        this.put("success", success);
        return this;
    }

    //存入成功标志和响应数据后返回（仅供响应成功时使用）
    public ModelMap putSuccessData(boolean success, Object data) {
        this.put("success", success);
        this.put("data", data);
        return this;
    }

    //存入错误标志和错误信息后返回（仅供错误时使用）
    public ModelMap putErrMsg(boolean success, String errMsg) {
        this.put("success", success);
        this.put("errMsg", errMsg);
        return this;
    }

    //存入错误标志和错误状态码和错误信息后返回（仅供错误时使用）
    public ModelMap putErrCodeMsg(boolean success, int errCode, String errMsg) {
        this.put("success", success);
        this.put("errCode", errCode);
        this.put("errMsg", errMsg);
        return this;
    }
}
