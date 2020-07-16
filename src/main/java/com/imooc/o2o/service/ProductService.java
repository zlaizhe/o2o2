package com.imooc.o2o.service;

import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ProductExecution;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.exceptions.ProductOperationException;

import java.io.InputStream;
import java.util.List;

public interface ProductService {
    //添加商品信息以及图片处理
    public ProductExecution addProduct(Product product,
                                       ImageHolder thumbnail,//缩略图
                                       List<ImageHolder> productImgList//详情图片
    ) throws ProductOperationException;

    public Product getProductById(long productId);

    //条件+分页查询
    public ProductExecution getProductList(Product productCondition, int pageIndex, int pageSize);

    public ProductExecution modifyProduct(Product product,
                                          ImageHolder thumbnail,//缩略图
                                          List<ImageHolder> productImgList//详情图片
    ) throws ProductOperationException;
}
