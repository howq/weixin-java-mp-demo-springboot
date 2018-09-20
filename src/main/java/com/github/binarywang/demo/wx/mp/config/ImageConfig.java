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
import java.util.*;

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
                List fileList = Arrays.asList(files);
                Collections.sort(fileList, new Comparator<File>() {
                    @Override
                    public int compare(File o1, File o2) {
                        if (o1.isDirectory() && o2.isFile())
                            return -1;
                        if (o1.isFile() && o2.isDirectory())
                            return 1;
                        return o1.getName().compareTo(o2.getName());
                    }
                });
                logger.debug("fileList---->" + fileList.toString());
                logger.debug("files---->" + files.toString());
                if (files != null) {
                    for (File file1 : files) {
                        imgs.add(file1);
                        InputStream is=new FileInputStream(file1);
                        BufferedImage bi = ImageIO.read(is);
                        bufferedImages.add(bi);
                    }

                }
            }
        } catch (Exception exception) {
            logger.debug("img list init fail" + exception.getMessage());
        }
        logger.debug("imgs---->" + imgs.toString());
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
