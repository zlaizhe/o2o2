package com.imooc.o2o.web.superadmin;

import com.imooc.o2o.dto.ModelMap;
import com.imooc.o2o.entity.Area;
import com.imooc.o2o.service.AreaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/superadmin")
public class AreaController {
    Logger logger = LoggerFactory.getLogger(AreaController.class);

    @Autowired
    private AreaService areaService;

    @GetMapping("/listarea")
    private ModelMap listArea() {//测试Controller和日志用
        logger.info("===start===");
        long startTime = System.currentTimeMillis();
        ModelMap modelMap = ModelMap.newInstance();
        List<Area> list = null;
        try {
            list = areaService.getAreaList();
            modelMap.put("rows", list);
            modelMap.put("total", list.size());
        } catch (Exception e) {
            e.printStackTrace();
            modelMap.putErrMsg(false, e.toString());
        }
        logger.error("test error!");
        long endTime = System.currentTimeMillis();
        logger.debug("costTime:[{}ms]", endTime - startTime);
        logger.info("===end===");
        return modelMap;
    }

}
