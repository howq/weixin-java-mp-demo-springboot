package com.github.binarywang.demo.wx.mp.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "img")
public class ImageConfig {

    private List<File> imgs = new ArrayList<>();

    private List<BufferedImage> bufferedImages = new ArrayList<>();

    private Logger logger = LoggerFactory.getLogger(getClass());

    public ImageConfig() {
        try {
            File file = ResourceUtils.getFile(SysConstant.IMG_SRC_LOCATION);
            if (file.exists()) {
                File[] files = file.listFiles();
                if (files != null) {
                    for (File childFile : files) {
                        imgs.add(childFile);
                        InputStream is=new FileInputStream(childFile);
                        BufferedImage bi = ImageIO.read(is);
                        bufferedImages.add(bi);
                    }
                }
            }
        } catch (Exception exception) {
            logger.debug("img list init fail" + exception.getMessage());
        }
    }

    public List<File> getImgs() {
        return imgs;
    }

    public void setImgs(List<File> imgs) {
        this.imgs = imgs;
    }

    /**
     * Getter for property 'bufferedImages'.
     *
     * @return Value for property 'bufferedImages'.
     */
    public List<BufferedImage> getBufferedImages() {
        return bufferedImages;
    }

    /**
     * Setter for property 'bufferedImages'.
     *
     * @param bufferedImages Value to set for property 'bufferedImages'.
     */
    public void setBufferedImages(List<BufferedImage> bufferedImages) {
        this.bufferedImages = bufferedImages;
    }
}
