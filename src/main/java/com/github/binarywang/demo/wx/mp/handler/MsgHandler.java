package com.github.binarywang.demo.wx.mp.handler;

import com.github.binarywang.demo.wx.mp.config.ImageConfig;
import com.github.binarywang.demo.wx.mp.config.SysConstant;
import com.github.binarywang.demo.wx.mp.utils.EmojiFilter;
import com.github.binarywang.demo.wx.mp.utils.JsonUtils;
import com.github.binarywang.demo.wx.mp.utils.PrintImage;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.result.WxMediaUploadResult;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutImageMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Map;
import java.util.Random;

import static com.github.binarywang.demo.wx.mp.utils.PrintImage.deepCopy;
import static me.chanjar.weixin.common.api.WxConsts.XmlMsgType;

/**
 * @author Binary Wang(https://github.com/binarywang)
 */
@Component
public class MsgHandler extends AbstractHandler {

    @Autowired
    private ImageConfig imageConfig;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
                                    Map<String, Object> context, WxMpService weixinService,
                                    WxSessionManager sessionManager) {

        if (!wxMessage.getMsgType().equals(XmlMsgType.EVENT)) {
            //TODO 可以选择将消息保存到本地
        }

        //当用户输入关键词如“你好”，“客服”等，并且有客服在线时，把消息转发给在线客服
        try {
            if (StringUtils.startsWithAny(wxMessage.getContent(), "你好", "客服")
                && weixinService.getKefuService().kfOnlineList()
                .getKfOnlineList().size() > 0) {
                return WxMpXmlOutMessage.TRANSFER_CUSTOMER_SERVICE()
                    .fromUser(wxMessage.getToUser())
                    .toUser(wxMessage.getFromUser()).build();
            }
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        //TODO 组装回复消息
        String content = "收到信息内容：" + JsonUtils.toJson(wxMessage);
        logger.debug(content);
        WxMediaUploadResult res = null;
        try {
            if (StringUtils.startsWithAny(wxMessage.getContent(), "中秋", "快乐") || XmlMsgType.IMAGE == wxMessage.getMsgType()) {
                String lang = "zh_CN"; //语言
                WxMpUser user = weixinService.getUserService().userInfo(wxMessage.getFromUser(), lang);
                logger.debug("user：" + wxMessage.getFromUser() + "----" + user.getNickname());
                logger.debug("user：" + wxMessage.getFromUser() + "----" + EmojiFilter.filterEmoji(user.getNickname()));

                Random rand = new Random();
                int i = rand.nextInt(imageConfig.getImgs().size() / 2);

                //男性
                if (user.getSex() == 1) {
                    i += imageConfig.getImgs().size() / 2;
                }

                PrintImage tt = new PrintImage();
                BufferedImage img = imageConfig.getBufferedImages().get(i);
                BufferedImage d = deepCopy(img);

                String name = EmojiFilter.filterEmoji(user.getNickname());
                if (i == 1 || i == 2 || i == 5) {
                    tt.modifyImage(d, name, -435, -1000, true);
                } else {
                    tt.modifyImage(d, name, -435, -1000, false);
                }
                String fileName = SysConstant.IMG_TMP_LOCATION + wxMessage.getFromUser() + ".jpg";
                tt.writeImageLocal(fileName, d);
                logger.debug("fileName：" + fileName);

//            File file = imageConfig.getImgs().get(i);
                File file = new File(fileName);
                res = weixinService.getMaterialService().mediaUpload(WxConsts.MediaFileType.IMAGE, file);

                WxMpXmlOutImageMessage outImageMessage = WxMpXmlOutMessage.IMAGE()
                    .mediaId(res.getMediaId())
                    .fromUser(wxMessage.getToUser())
                    .toUser(wxMessage.getFromUser())
                    .build();

                logger.debug("组装回复消息：" + outImageMessage.toString());
                return outImageMessage;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
//        WxMpXmlOutImageMessage outImageMessage = WxMpXmlOutMessage.IMAGE()
//            .mediaId(res.getMediaId())
//            .fromUser(wxMessage.getToUser())
//            .toUser(wxMessage.getFromUser())
//            .build();
//
//        logger.debug("组装回复消息：" + outImageMessage.toString());
        return null;
    }
}
