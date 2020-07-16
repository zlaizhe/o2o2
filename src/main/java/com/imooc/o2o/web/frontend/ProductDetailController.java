package com.imooc.o2o.web.frontend;

import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.imooc.o2o.dto.ModelMap;
import com.imooc.o2o.dto.ProductExecution;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.service.ProductCategoryService;
import com.imooc.o2o.service.ProductService;
import com.imooc.o2o.service.ShopService;
import com.imooc.o2o.util.CodeUtil;
import com.imooc.o2o.util.ShortNetAddressUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

//商品详细页面的控制器
@RestController
@RequestMapping("/frontend")
public class ProductDetailController {

    @Autowired
    private ProductService productService;

    //根据Id获取商品详情，包括商品的所有详情图
    @GetMapping("/listproductdetailpageinfo")
    private ModelMap listProductDetailPageInfo(Long productId, HttpSession session) {
        ModelMap modelMap = ModelMap.newInstance();
        if (productId == null) {
            return modelMap.putErrMsg(false, "emtpy productId");
        }
        Product product = productService.getProductById(productId);
        //2.0新增
        PersonInfo user = (PersonInfo) session.getAttribute("user");
        if (user == null) {
            modelMap.put("needQRCode", false);
        } else {
            modelMap.put("needQRCode", true);
        }
        modelMap.put("product", product);
        return modelMap.putSuccess(true);
    }


    // 微信获取用户信息的api前缀
    private static String urlPrefix;
    // 微信获取用户信息的api中间部分
    private static String urlMiddle;
    // 微信获取用户信息的api后缀
    private static String urlSuffix;
    // 微信回传给的响应添加顾客商品映射信息的url
    private static String productmapUrl;

    @Value("${wechat.prefix}")
    public void setUrlPrefix(String urlPrefix) {
        ProductDetailController.urlPrefix = urlPrefix;
    }

    @Value("${wechat.middle}")
    public void setUrlMiddle(String urlMiddle) {
        ProductDetailController.urlMiddle = urlMiddle;
    }

    @Value("${wechat.suffix}")
    public void setUrlSuffix(String urlSuffix) {
        ProductDetailController.urlSuffix = urlSuffix;
    }

    @Value("${wechat.productmap.url}")
    public void setProductmapUrl(String productmapUrl) {
        ProductDetailController.productmapUrl = productmapUrl;
    }

    /**
     * 生成商品的消费凭证二维码，供操作员扫描，证明已消费，微信扫一扫就能链接到对应的URL里面
     */
    @GetMapping("/generateqrcode4product")
    private void generateQRCode4Product(HttpServletRequest request, HttpServletResponse response) {
        // 获取前端传递过来的商品Id
        Long productId = Long.valueOf(request.getParameter("productId"));
        // 从session里获取当前顾客的信息
        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
        // 空值判断
        if (productId != -1 && user != null && user.getUserId() != null) {
            // 获取当前时间戳，以保证二维码的时间有效性，精确到毫秒
            long timpStamp = System.currentTimeMillis();
            // 将商品id，顾客Id和timestamp传入content，赋值到state中，这样微信获取到这些信息后会回传到用户商品映射信息的添加方法里
            // 加上aaa是为了一会的在添加信息的方法里替换这些信息使用
            String content = "{aaaproductIdaaa:" + productId + ",aaacustomerIdaaa:" + user.getUserId()
                    + ",aaacreateTimeaaa:" + timpStamp + "}";
            try {
                // 将content的信息先进行base64编码以避免特殊字符造成的干扰，之后拼接目标URL
                String longUrl = urlPrefix + productmapUrl + urlMiddle + URLEncoder.encode(content, "UTF-8")
                        + urlSuffix;
                System.out.println(longUrl);
                // 将目标URL转换成短的URL
                String shortUrl = ShortNetAddressUtil.getShortURL(longUrl);
                // 调用二维码生成的工具类方法，传入短的URL，生成二维码
                BitMatrix qRcodeImg = CodeUtil.generateQRCodeStream(longUrl, response);
                // 将二维码以图片流的形式输出到前端
                MatrixToImageWriter.writeToStream(qRcodeImg, "png", response.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
