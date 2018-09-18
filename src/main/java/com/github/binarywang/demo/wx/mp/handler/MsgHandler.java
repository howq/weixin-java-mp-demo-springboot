package com.github.binarywang.demo.wx.mp.handler;

import com.github.binarywang.demo.wx.mp.config.ImageConfig;
import com.github.binarywang.demo.wx.mp.utils.JsonUtils;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.result.WxMediaUploadResult;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutImageMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Map;
import java.util.Random;

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
        WxMediaUploadResult res = null;
        try {
            Random rand = new Random();
            int i = rand.nextInt(imageConfig.getImgs().size() - 1); //生成0-100以内的随机数
            File file = imageConfig.getImgs().get(i);
//            InputStream inputStream = new FileInputStream(file);
//            res = weixinService.getMaterialService().mediaUpload(WxConsts.MediaFileType.IMAGE, WxConsts.MediaFileType.FILE, inputStream);
//// 或者
            res = weixinService.getMaterialService().mediaUpload(WxConsts.MediaFileType.IMAGE, file);
            logger.debug("rrrrrrrrrrrrrr" + res.toString());
//            res.getType();
//            res.getCreatedAt();
//            res.getMediaId();
//            res.getThumbMediaId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null == res) {
            logger.debug("gggggggggggggg" + res.toString());
            res = new WxMediaUploadResult();
        }
        WxMpXmlOutImageMessage outImageMessage = WxMpXmlOutMessage.IMAGE()
            .mediaId(res.getMediaId())
            .fromUser(wxMessage.getToUser())
            .toUser(wxMessage.getFromUser())
            .build();

        return outImageMessage;
    }

}
