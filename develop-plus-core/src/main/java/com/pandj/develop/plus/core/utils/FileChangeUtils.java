package com.pandj.develop.plus.core.utils;

import com.pandj.develop.plus.core.result.ResultEnums;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

/**
 * 文件转换
 * <p>
 * &#064;Author  pandujun
 * <p>
 * &#064;Date  2023/10/27 15:19
 */
public class FileChangeUtils {
    private static final Logger logger = LoggerFactory.getLogger(FileChangeUtils.class);

    /**
     * 图片转化为Base64
     *
     * @param image 图片流
     * @return Base64图片
     */
    public static String getImageBASE64(BufferedImage image) {
        try (ByteArrayOutputStream bao = new ByteArrayOutputStream()) {
            ImageIO.write(image, "png", bao);
            byte[] imageData = bao.toByteArray();
            String baseImage = Base64.getEncoder().encodeToString(imageData);
            baseImage = baseImage.replaceAll("\\s+", "");
            return baseImage;
        } catch (IOException e) {
            logger.error("FileChangeUtils#getImageBASE64 ERROR：{ }", e);
            throw ResultEnums.CHANGE_ERROR.getException();
        }
    }

    /**
     * 将base64文件转成文件
     *
     * @param base64     文件的base64
     * @param fileName   文件名称
     * @param fileFormat 文件后缀
     * @return 文件输出流
     */
    public static FileOutputStream getFile(String base64, String fileName, String fileFormat) {
        // 将 Base64 字符串解码为字节数组
        byte[] decodedBytes = Base64.getDecoder().decode(base64);
        // 创建文件输出流
        try (FileOutputStream outputStream = new FileOutputStream(fileName + "." + fileFormat)) {
            // 将字节数组写入文件输出流
            outputStream.write(decodedBytes);
            return outputStream;
        } catch (IOException e) {
            logger.error("FileChangeUtils#getFile ERROR：{ }", e);
            throw ResultEnums.CHANGE_ERROR.getException();
        }
    }
}
