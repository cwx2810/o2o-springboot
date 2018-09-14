package com.imooc.o2o.dto;

import java.io.InputStream;

/**
 * 将图片名称和图片组合在一起，不用在service中每次传入thumbnail和图片名称两个参数
 * @author: LieutenantChen
 * @create: 2018-09-10 14:04
 **/
public class ImageHolder {

    private String imageName;
    private InputStream image;

    public ImageHolder(String imageName, InputStream image) {
        this.imageName = imageName;
        this.image = image;
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
