package com.github.binarywang.demo.wx.mp.utils;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Random;

public class PrintImage {

//    private Font font = new Font("黑体", Font.PLAIN, 50); // 添加字体的属性设置
    private Font font = new Font("微软雅黑 Bold", Font.PLAIN, 50); // 添加字体的属性设置

    private Graphics2D g = null;

    private int fontsize = 0;

    /**
     * 导入本地图片到缓冲区
     */
    public BufferedImage loadImageLocal(String imgName) {
        try {
            return ImageIO.read(new File(imgName));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * 导入网络图片到缓冲区
     */
    public BufferedImage loadImageUrl(String imgName) {
        try {
            URL url = new URL(imgName);
            return ImageIO.read(url);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * 生成新图片到本地
     */
    public void writeImageLocal(String newImage, BufferedImage img) {
        if (newImage != null && img != null) {
            try {
                File outputfile = new File(newImage);
                ImageIO.write(img, "jpg", outputfile);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * 设定文字的字体等
     */
    public void setFont(String fontStyle, int fontSize) {
        this.fontsize = fontSize;
        this.font = new Font(fontStyle, Font.PLAIN, fontSize);
    }

    /**
     * 修改图片,返回修改后的图片缓冲区（只输出一行文本）
     */
    public BufferedImage modifyImage(BufferedImage img, Object content, int x, int y,boolean isBlank) {

        try {
            int w = img.getWidth();
            int h = img.getHeight();
            g = img.createGraphics();
            g.setBackground(Color.WHITE);
//            g.setColor(new Color(120, 120, 110));//设置字体颜色
            g.setColor(new Color(255, 250, 250));//设置字体颜色
            if (isBlank) {
                g.setColor(new Color(96, 96, 94));//设置字体颜色
            }
            if (this.font != null)
                g.setFont(this.font);
            if (content != null) {
                g.translate(w / 2, h / 2);
//                g.rotate(8 * Math.PI / 180);
                g.drawString(content.toString(), x, y);
            }
            g.dispose();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return img;
    }

    public static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    /**
     * 修改图片,返回修改后的图片缓冲区（只输出一行文本）
     * <p>
     * 时间:2007-10-8
     *
     * @param img
     * @return
     */
    public BufferedImage modifyImageYe(BufferedImage img) {

        try {
            int w = img.getWidth();
            int h = img.getHeight();
            g = img.createGraphics();
            g.setBackground(Color.WHITE);
            g.setColor(Color.blue);//设置字体颜色
            if (this.font != null)
                g.setFont(this.font);
            g.drawString("www.hi.baidu.com?xia_mingjian", w - 85, h - 5);
            g.dispose();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return img;
    }

    public BufferedImage modifyImagetogeter(BufferedImage b, BufferedImage d) {

        try {
            int w = b.getWidth();
            int h = b.getHeight();
            g = d.createGraphics();
            g.drawImage(b, 100, 10, w, h, null);
            g.dispose();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return d;
    }

    public static void main(String[] args) {
        PrintImage tt = new PrintImage();
        BufferedImage d = tt.loadImageLocal("C:\\Users\\h\\Desktop\\中秋艺卡\\ya\\3.jpg");
        BufferedImage img = deepCopy(d);

        String Cname = "寒洛先生";
        tt.modifyImage(img, Cname, -435, -1000, true);
//        tt.modifyImageYe(d);
        tt.writeImageLocal("C:\\Users\\h\\Desktop\\中秋艺卡\\tmp\\嫦娥.jpg", img);
        System.out.println("success");

    }

}
