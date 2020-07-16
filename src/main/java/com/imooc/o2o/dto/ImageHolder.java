package com.imooc.o2o.dto;

import java.io.InputStream;

public class ImageHolder {

    private String imageName;
    private InputStream image;

    public ImageHolder() {
    }

    public ImageHolder(String imageName, InputStream image) {
        this.imageName = imageName;
        this.image = image;
    }

    //判断该ImageHolder对象是否包含了图片的输入流和图片名称
    public boolean hasImage() {
        return image != null && imageName != null && !imageName.isEmpty();
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public InputStream getImage() {
        return image;
    }

    public void setImage(InputStream image) {
        this.image = image;
    }

}
