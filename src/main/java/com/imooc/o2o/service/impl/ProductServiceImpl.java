package com.imooc.o2o.service.impl;

import com.imooc.o2o.dao.ProductDao;
import com.imooc.o2o.dao.ProductImgDao;
import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ProductExecution;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.entity.ProductImg;
import com.imooc.o2o.enums.ProductStateEnum;
import com.imooc.o2o.exceptions.ProductOperationException;
import com.imooc.o2o.service.ProductService;
import com.imooc.o2o.util.ImageUtil;
import com.imooc.o2o.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductDao productDao;
    @Autowired
    private ProductImgDao productImgDao;

    // 1.处理缩略图，获取缩略图相对路径并赋值给product
    // 2.往tb_product写入商品信息，获取productId
    // 3.结合productId批量处理商品详情图
    // 4.将商品详情图列表批量插入tb_product_img中
    @Override
    @Transactional
    public ProductExecution addProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImgList) throws ProductOperationException {
        if (product == null || product.getShop() == null || product.getShop().getShopId() == null) {
            return new ProductExecution(ProductStateEnum.EMPTY);
        }
        //设置商品默认属性
        product.setCreateTime(new Date());
        product.setLastEditTime(new Date());
        product.setEnableStatus(1);//默认上架状态
        if (thumbnail != null) {//如果有缩略图，添加缩略图
            addThumbnail(product, thumbnail);
        }
        //添加商品和图片
        int effectNum = productDao.insertProduct(product);
        if (effectNum <= 0) {
            throw new ProductOperationException("创建商品失败");
        }
        if (productImgList != null && !productImgList.isEmpty()) {//如果有详情图，添加详情图
            addProductImgList(product, productImgList);
        }
        return new ProductExecution(ProductStateEnum.SUCCESS, product);
    }

    //查询一个商品
    @Override
    public Product getProductById(long productId) {
        return productDao.queryProductById(productId);
    }

    @Override
    public ProductExecution getProductList(Product productCondition, int pageIndex, int pageSize) {
        //计算页码对应的行码
        int rowIndex = (pageIndex - 1) * pageSize;
        //条件分页查询
        int count = productDao.queryProductCount(productCondition);
        List<Product> productList = productDao.queryProductList(productCondition, rowIndex, pageSize);
        //封装结果返回
        ProductExecution pe = new ProductExecution();
        pe.setProductList(productList);
        pe.setCount(count);
        return pe;
    }

    // 1.若缩略图参数有值，则处理缩略图，
    // 若原先存在缩略图则先删除再添加新图，之后获取缩略图相对路径并赋值给product
    // 2.若商品详情图列表参数有值，对商品详情图片列表进行同样的操作
    // 3.将tb_product_img下面的该商品原先的商品详情图记录全部清除
    // 4.更新tb_product_img以及tb_product的信息
    @Override
    @Transactional
    public ProductExecution modifyProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImgList) throws ProductOperationException {
        if (product == null || product.getShop() == null || product.getShop().getShopId() == null) {
            return new ProductExecution(ProductStateEnum.EMPTY);
        }
        //设置商品默认属性
        product.setLastEditTime(new Date());
        //如果上传了商品缩略图并且原来有缩略图，先删除原缩略图再添加新缩略图
        if (thumbnail != null) {
            //查询product中的缩略图地址
            Product tempProduct = productDao.queryProductById(product.getProductId());
            if (tempProduct.getImgAddr() != null) {
                ImageUtil.deleteFileOrPath(tempProduct.getImgAddr());
            }
            addThumbnail(product, thumbnail);
        }
        //如果上传了商品详情图，先删除原所有详情图再添加新详情图
        if (productImgList != null && !productImgList.isEmpty()) {
            deleteProductImgList(product.getProductId());
            addProductImgList(product, productImgList);
        }
        //更新商品信息
        int effectNum = productDao.updateProduct(product);
        if (effectNum <= 0) {
            throw new ProductOperationException("更新商品失败");
        }
        return new ProductExecution(ProductStateEnum.SUCCESS, product);
    }

    //删除某个商品的所有详情图
    private void deleteProductImgList(Long productId) {
        List<ProductImg> productImgList = productImgDao.queryProductImgList(productId);
        //删除图片
        for (ProductImg productImg : productImgList) {
            ImageUtil.deleteFileOrPath(productImg.getImgAddr());
        }
        //删除数据库中的原来图片信息
        productImgDao.deleteProductImgByProductId(productId);
    }

    //批量添加商品详情图
    private void addProductImgList(Product product, List<ImageHolder> productImgList) {
        String dest = PathUtil.getShopImagePath(product.getShop().getShopId());//直接存在店铺文件夹下
        List<ProductImg> piList = new ArrayList<>();
        for (ImageHolder imageHolder : productImgList) {//遍历上传的商品详情图，存储到文件夹内，生成每个图片的相对路径存入ProductImg
            String imgAddr = ImageUtil.generateNomalImg(imageHolder, dest);
            ProductImg pi = new ProductImg();
            pi.setImgAddr(imgAddr);
            pi.setProductId(product.getProductId());
            pi.setCreateTime(new Date());
            piList.add(pi);
        }
        if (piList.size() > 0) {//有ProductImg需要添加到数据库
            int effectNum = productImgDao.batchInsertProductImg(piList);
            if (effectNum <= 0) {
                throw new ProductOperationException("创建图片详情图片失败");
            }
        }
    }

    //添加缩略图，并把路径存入product
    private void addThumbnail(Product product, ImageHolder thumbnail) {
        String dest = PathUtil.getShopImagePath(product.getShop().getShopId());
        String thumbnailAddr = ImageUtil.generateThumbnail(thumbnail, dest);
        product.setImgAddr(thumbnailAddr);
    }
}
