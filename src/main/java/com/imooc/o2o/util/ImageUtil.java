package com.imooc.o2o.util;

import com.imooc.o2o.dto.ImageHolder;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;


public class ImageUtil {
    private static String basePath = PathUtil.getImgBasePath();
    private static final SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    private static final Random r = new Random();
    private static Logger logger = LoggerFactory.getLogger(ImageUtil.class);

    //将上传的文件对象CommonsMultipartFile转为通用的File对象
    @Deprecated
    public static File transferCommonsMultipartFileToFile(CommonsMultipartFile cFile) {
        File newFile = new File(cFile.getOriginalFilename());
        try {
            cFile.transferTo(newFile);
        } catch (IOException e) {
            logger.error(e.toString());
            e.printStackTrace();
        }
        return newFile;
    }

    //处理缩略图的方法，对上传图片压缩，添加水印，并存储在ImgBasePath的targetAddr目录下，返回新存储图片的相对路径
    public static String generateThumbnail(ImageHolder thumbnail, String targetAddr) {
        String realFileName = getRandomFileName();//生成随机文件名
        String extension = getFileExtension(thumbnail.getImageName());//获取文件扩展名 .jpg .png
        makeDirPath(targetAddr);//创建文件路径
        String relativeAddr = targetAddr + realFileName + extension;
        logger.debug("current relativeAddr is :" + relativeAddr);
        File dest = new File(PathUtil.getImgBasePath() + relativeAddr);
        logger.debug("current complete addr is :" + PathUtil.getImgBasePath() + relativeAddr);
        try {
            Thumbnails.of(thumbnail.getImage()).size(200, 200)
                    .watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath + "/watermark.jpg")), 0.25f)
                    .outputQuality(0.8f).toFile(dest);
        } catch (IOException e) {
            logger.error(e.toString());
            e.printStackTrace();
            //这里有个bug，如果出现了异常会导致service的addShop方法事务不回滚，因为没有将IOException作为运行时异常向上抛出
            //改正：
            throw new RuntimeException(e);
        }
        return relativeAddr;
    }

    //创建目标路径所涉及到的目录
    private static void makeDirPath(String targetAddr) {
        String realFileParentPath = PathUtil.getImgBasePath() + targetAddr;
        File dirPath = new File(realFileParentPath);
        if (!dirPath.exists()) {
            dirPath.mkdirs();
        }
    }

    //获取输入文件流的扩展名
    private static String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf('.'));
    }

    //生成随机文件名：年月日时分秒+五位随机数
    public static String getRandomFileName() {
        //五位随机数
        int rannum = r.nextInt(90000) + 10000;
        String nowTimeStr = sDateFormat.format(new Date());
        return nowTimeStr + rannum;
    }

    //如果storePath是文件路径，删除文件，是目录，删除目录下所有文件
    public static void deleteFileOrPath(String storePath) {
        File fileOrPath = new File(PathUtil.getImgBasePath() + storePath);
        if (fileOrPath.exists()) {
            if (fileOrPath.isDirectory()) {
                File[] files = fileOrPath.listFiles();
                for (File file : files) {
                    file.delete();
                }
            }
            fileOrPath.delete();
        }
    }

    //处理详情图 和generateThumbnail方法的区别只有生成图片的大小和压缩比例不同
    public static String generateNomalImg(ImageHolder thumbnail, String targetAddr) {
        String realFileName = getRandomFileName();//生成随机文件名
        String extension = getFileExtension(thumbnail.getImageName());//获取文件扩展名 .jpg .png
        makeDirPath(targetAddr);//创建文件路径
        String relativeAddr = targetAddr + realFileName + extension;
        logger.debug("current relativeAddr is :" + relativeAddr);
        File dest = new File(PathUtil.getImgBasePath() + relativeAddr);
        logger.debug("current complete addr is :" + PathUtil.getImgBasePath() + relativeAddr);
        try {
            Thumbnails.of(thumbnail.getImage()).size(337, 640)
                    .watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath + "/watermark.jpg")), 0.25f)
                    .outputQuality(0.9f).toFile(dest);
        } catch (IOException e) {
            logger.error(e.toString());
            e.printStackTrace();
            //这里有个bug，如果出现了异常会导致service的addShop方法事务不回滚，因为没有将IOException作为运行时异常向上抛出
            //改正：
            throw new RuntimeException(e);
        }
        return relativeAddr;
    }

}
