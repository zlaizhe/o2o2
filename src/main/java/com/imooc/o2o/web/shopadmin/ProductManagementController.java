package com.imooc.o2o.web.shopadmin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.code.kaptcha.Constants;
import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ModelMap;
import com.imooc.o2o.dto.ProductExecution;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.enums.ProductStateEnum;
import com.imooc.o2o.service.ProductCategoryService;
import com.imooc.o2o.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/shopadmin")
public class ProductManagementController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductCategoryService productCategoryService;

    //支持上传的商品详情图最大数量
    private static final int IMAGEMAXCOUNT = 6;

    @GetMapping("/getproductlistbyshop")
    private ModelMap getProductListByShop(Long productCategoryId,
                                      String productName,
                                      Integer pageIndex,
                                      Integer pageSize,
                                      HttpSession session) {
        ModelMap modelMap = ModelMap.newInstance();
        Shop currentShop = (Shop) session.getAttribute("currentShop");
        if (currentShop == null || currentShop.getShopId() == null) {
            return modelMap.putErrMsg(false, "empty shopId");
        }
        if (pageIndex == null || pageIndex <= 0) {
            pageIndex = 1;//默认页面第一页
        }
        if (pageSize == null || pageSize <= 0) {
            pageSize = 5;//默认一页五个
        }
        //封装查询条件
        Product productCondition = new Product();
        productCondition.setShop(currentShop);
        productCondition.setProductName(productName);
        ProductCategory pc = new ProductCategory();
        pc.setProductCategoryId(productCategoryId);
        productCondition.setProductCategory(pc);
        //条件查询+分页
        ProductExecution pe = productService.getProductList(productCondition, pageIndex, pageSize);
        modelMap.put("productList", pe.getProductList());
        modelMap.put("count", pe.getCount());
        return modelMap.putSuccess(true);
    }

    @PostMapping("/addproduct")
    private ModelMap addProduct(String verifyCodeActual, //验证码
                                String productStr, //Json格式的商品信息字符串
                                MultipartFile thumbnail,//上传的产品缩略图
                                MultipartFile[] productImg,//上传的产品详情图文件（可能有多个）
                                HttpSession session) throws IOException {
        ModelMap modelMap = ModelMap.newInstance();
        if (verifyCodeActual == null
                || !verifyCodeActual.equals(session.getAttribute(Constants.KAPTCHA_SESSION_KEY))) {//验证码判断
            return modelMap.putErrMsg(false, "验证码错误");
        }
        //接收前端参数的变量的初始化，包括商品、缩略图、详情图
        if (thumbnail == null || productImg == null || productImg.length == 0) {
            return modelMap.putErrMsg(false, "上传图片不能为空");
        }
        //获取上传的缩略图和详情图
        ImageHolder thumbnailImage = new ImageHolder(thumbnail.getOriginalFilename(), thumbnail.getInputStream());
        List<ImageHolder> productImgList = new ArrayList<>();
        for (int i = 0; i < productImg.length && i < IMAGEMAXCOUNT; i++) {
            productImgList.add(new ImageHolder(productImg[i].getOriginalFilename(), productImg[i].getInputStream()));
        }
        //将productStr转为实体类
        Product product = new ObjectMapper().readValue(productStr, Product.class);
        //从session获取当前店铺id，赋值给product
        Shop currentShop = (Shop) session.getAttribute("currentShop");
        product.setShop(currentShop);
        //执行添加操作
        ProductExecution pe = productService.addProduct(product, thumbnailImage, productImgList);
        return pe.getState() == ProductStateEnum.SUCCESS.getState() ?
                modelMap.putSuccess(true) : modelMap.putErrMsg(false, pe.getStateInfo());
    }

    @GetMapping("/getproductbyid")
    private ModelMap addProduct(Long productId) {
        ModelMap modelMap = ModelMap.newInstance();
        if (productId == null || productId <= 0) {
            return modelMap.putErrMsg(false, "empty productId");
        }
        //获取商品
        Product product = productService.getProductById(productId);
        //获取该店铺下的商品分类列表
        List<ProductCategory> productCategoryList = productCategoryService.getProductCategoryList(product.getShop().getShopId());
        modelMap.put("product", product);
        modelMap.put("productCategoryList", productCategoryList);
        return modelMap.putSuccess(true);
    }

    @PostMapping("/modifyproduct")
    private ModelMap modifyProduct(Boolean statusChange,
                                   String verifyCodeActual, //验证码
                                   String productStr, //Json格式的商品信息字符串
                                   MultipartFile thumbnail,//上传的产品缩略图
                                   MultipartFile[] productImg,//上传的产品详情图文件（可能有多个）
                                   HttpSession session) throws IOException {
        ModelMap modelMap = ModelMap.newInstance();
        // 是商品编辑时候调用还是上下架操作的时候调用，若为前者则进行验证码判断，后者则跳过验证码判断
        if (statusChange == null || !statusChange) {//statusChange为null或false，需要输入验证码
            if (verifyCodeActual == null
                    || !verifyCodeActual.equals(session.getAttribute(Constants.KAPTCHA_SESSION_KEY))) {//验证码判断
                return modelMap.putErrMsg(false, "验证码错误");//0.判断验证码
            }
        }

        //接收前端参数的变量的初始化，包括商品、缩略图、详情图
        ImageHolder thumbnailImage = null;
        if (thumbnail != null) {
            thumbnailImage = new ImageHolder(thumbnail.getOriginalFilename(), thumbnail.getInputStream());
        }
        //获取上传的缩略图和详情图
        List<ImageHolder> productImgList = null;
        if (productImg != null && productImg.length > 0) {
            productImgList = new ArrayList<>();
            for (int i = 0; i < productImg.length && i < IMAGEMAXCOUNT; i++) {
                productImgList.add(new ImageHolder(productImg[i].getOriginalFilename(), productImg[i].getInputStream()));
            }
        }
        //将productStr转为实体类
        Product product = new ObjectMapper().readValue(productStr, Product.class);
        //从session获取当前店铺id，赋值给product
        Shop currentShop = (Shop) session.getAttribute("currentShop");
        product.setShop(currentShop);
        //执行修改操作
        ProductExecution pe = productService.modifyProduct(product, thumbnailImage, productImgList);
        return pe.getState() == ProductStateEnum.SUCCESS.getState() ?
                modelMap.putSuccess(true) : modelMap.putErrMsg(false, pe.getStateInfo());
    }
}
