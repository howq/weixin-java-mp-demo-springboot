package com.github.binarywang.demo.wx.mp.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "img")
public class ImageConfig {

    private List<File> imgs = new ArrayList<>();

    private Logger logger = LoggerFactory.getLogger(getClass());

    private String location="/imgs";

    public ImageConfig() {
        try{
            File file = ResourceUtils.getFile(location);
            logger.debug("sssssss--->" + file.getPath());

            if(file.exists()){
                File[] files = file.listFiles();
                if(files != null){
                    for(File childFile:files){
                        logger.debug("sssssss--->" + childFile.getPath());
                        imgs.add(childFile);
                    }
                }
            }
            logger.debug("sssssss--->" + imgs.size() + imgs.toString());
        }catch (FileNotFoundException exception){
            logger.debug("img list init fail" + exception.getMessage());
        }
    }

    public List<File> getImgs() {
        return imgs;
    }

    public void setImgs(List<File> imgs) {
        this.imgs = imgs;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
