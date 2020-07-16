package com.imooc.o2o.web.frontend;

import java.util.Map;

import javax.servlet.http.HttpSession;

import com.imooc.o2o.dto.ModelMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.imooc.o2o.dto.AwardExecution;
import com.imooc.o2o.entity.Award;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.UserShopMap;
import com.imooc.o2o.service.AwardService;
import com.imooc.o2o.service.UserShopMapService;

@Controller
@RequestMapping("/frontend")
public class ShopAwardController {
    @Autowired
    private AwardService awardService;
    @Autowired
    private UserShopMapService userShopMapService;

    /**
     * 列出店铺设定的奖品列表
     */
    @GetMapping("/listawardsbyshop")
    @ResponseBody
    private Map<String, Object> listAwardsByShop(Integer pageIndex, Integer pageSize,
                                                 Integer shopId, String awardName,
                                                 HttpSession session) {
        ModelMap modelMap = ModelMap.newInstance();
        // 空值判断
        if (pageIndex == null || pageIndex < 1) {
            pageIndex = 1;
        }
        if (pageSize == null || pageSize < 1) {
            pageIndex = 3;
        }
        if (shopId == null || shopId < 1) {
            return modelMap.putErrMsg(false, "empty shopId");
        }
        // 获取前端可能输入的奖品名模糊查询
        Award awardCondition = compactAwardCondition4Search(shopId, awardName);
        // 传入查询条件分页获取奖品信息
        AwardExecution ae = awardService.getAwardList(awardCondition, pageIndex, pageSize);
        modelMap.put("awardList", ae.getAwardList());
        modelMap.put("count", ae.getCount());
        // 从Session中获取用户信息，主要是为了显示该用户在本店铺的积分
        PersonInfo user = (PersonInfo) session.getAttribute("user");
        // 空值判断
        if (user != null && user.getUserId() != null) {
            // 获取该用户在本店铺的积分信息
            UserShopMap userShopMap = userShopMapService.getUserShopMap(user.getUserId(), shopId);
            modelMap.put("totalPoint", userShopMap == null ? 0 : userShopMap.getPoint());
        }
        return modelMap.putSuccess(true);
    }

    /**
     * 封装查询条件
     *
     * @param shopId
     * @param awardName
     * @return
     */
    private Award compactAwardCondition4Search(long shopId, String awardName) {
        Award awardCondition = new Award();
        awardCondition.setShopId(shopId);
        // 只取出可用的奖品
        awardCondition.setEnableStatus(1);
        if (awardName != null) {
            awardCondition.setAwardName(awardName);
        }
        return awardCondition;
    }
}
