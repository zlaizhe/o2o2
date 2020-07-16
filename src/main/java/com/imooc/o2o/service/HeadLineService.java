package com.imooc.o2o.service;

import com.imooc.o2o.entity.HeadLine;

import java.io.IOException;
import java.util.List;

public interface HeadLineService {
    public static final String HLLISTKEY = "headlinelist";//头条存储在redis中的key

    public List<HeadLine> getHeadLineList(HeadLine headLineCondition);
}
