package com.org.devg.ding.platform.utils;

import com.alibaba.fastjson.JSON;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiChatSendRequest;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.response.OapiChatSendResponse;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.dingtalk.api.response.OapiMessageCorpconversationAsyncsendV2Response;
import com.dingtalk.api.response.OapiRobotSendResponse;
import com.org.devg.ding.platform.model.DingConfig;
import com.org.devg.ding.platform.model.DingDing;
import com.org.devg.ding.platform.model.Message;
import com.taobao.api.ApiException;
import java.net.URLEncoder;
import java.util.List;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

/**
 * 钉钉JS-API实时控制台
 * <p>
 * https://wsdebug.dingtalk.com/
 * <p>
 * 获取chatId： v0.1.2 biz.chat.chooseConversationByCorpId根据corpId选择会话(2.6新增) chatf4ba3f20f363a3cd420f7826a82bdfdc
 *
 * http://47.111.253.191/dingDing/callbackMsg
 */

@Slf4j
public class DingUtil {

  private static final String split = "@_@";

  private DingDing dingDing;

  public DingUtil(DingDing dingDing) {
    this.dingDing = dingDing;
  }

  public OapiGettokenResponse getDingToken() {
    try {
      DingTalkClient client = new DefaultDingTalkClient(MemoryUtil.tokenUrl);
      OapiGettokenRequest req = new OapiGettokenRequest();
      req.setAppkey(dingDing.getAppkey());
      req.setAppsecret(dingDing.getAppsecret());
      req.setHttpMethod("GET");
      OapiGettokenResponse rsp = client.execute(req);
      log.info("ding gettoken response:{}", JSON.toJSON(rsp));
      return rsp;
//            System.out.println(rsp.getBody());
    } catch (ApiException e) {
      log.error("ding gettoken error", e);
    }
    return null;
  }

  public String getAccessToken() {
    OapiGettokenResponse rsp = getDingToken();
    if (null == rsp) {
      return null;
    }
    return rsp.getAccessToken();
  }

  public OapiChatSendResponse sendMessage(Message message, String chartId, String accessToken) {
    try {
      DingTalkClient client = new DefaultDingTalkClient(MemoryUtil.sendMsgUrl);
      OapiChatSendRequest req = new OapiChatSendRequest();
      req.setChatid(chartId);
      OapiChatSendRequest.Msg obj1 = new OapiChatSendRequest.Msg();
      OapiChatSendRequest.Text obj2 = new OapiChatSendRequest.Text();
      obj2.setContent(message.getMsgContent());
      obj1.setText(obj2);
      obj1.setMsgtype(message.getMsgType());
      req.setMsg(obj1);
      OapiChatSendResponse rsp = client.execute(req, accessToken);
      log.info("ding sendMsg response:{}", JSON.toJSON(rsp));
//            System.out.println(rsp.getBody());
      return rsp;
    } catch (ApiException e) {
      log.error("ding sendMsg error", e);
    }
    return null;
  }

  public OapiMessageCorpconversationAsyncsendV2Response noticeMessage(Message message, Long agentId, String accessToken, boolean toAllUser) {
    try {
      DingTalkClient client = new DefaultDingTalkClient(MemoryUtil.noticeMsgUrl);
      OapiMessageCorpconversationAsyncsendV2Request req = new OapiMessageCorpconversationAsyncsendV2Request();
      req.setAgentId(agentId);
      req.setToAllUser(toAllUser);
      OapiMessageCorpconversationAsyncsendV2Request.Msg obj1 = new OapiMessageCorpconversationAsyncsendV2Request.Msg();
      obj1.setMsgtype(message.getMsgType());
      OapiMessageCorpconversationAsyncsendV2Request.Text obj2 = new OapiMessageCorpconversationAsyncsendV2Request.Text();
      obj2.setContent(message.getMsgContent());
      obj1.setText(obj2);
      req.setMsg(obj1);
      OapiMessageCorpconversationAsyncsendV2Response rsp = client.execute(req, accessToken);
      log.info("ding noticeMsg response:{}", JSON.toJSON(rsp));
      return rsp;
    } catch (ApiException e) {
      log.error("ding noticeMsg error", e);
    }
    return null;
  }

  public OapiMessageCorpconversationAsyncsendV2Response noticeMessage(Message message, Long agentId, String accessToken) {
    return noticeMessage(message, agentId, accessToken, true);
  }

  public OapiRobotSendResponse robotMsg(Message message) {
    try {
      //      https://oapi.dingtalk.com/robot/send?access_token=XXXXXX&timestamp=XXX&sign=XXX
      String robotMsgUrl = getDingRobotMsgUrl(message);
      Long nowTime = System.currentTimeMillis();
      String dingSecret = getDingSecret(message);
      if (CommonUtil.isNullOrEmpty(dingSecret)) {
        log.error("ding get dingSecret error");
        return null;
      }
      robotMsgUrl += ("&timestamp=" + nowTime);
      robotMsgUrl += ("&sign=" + dingSecret);
      log.info("robotMsgUrl:" + robotMsgUrl);
      DingTalkClient client = new DefaultDingTalkClient(robotMsgUrl);
      OapiRobotSendRequest request = new OapiRobotSendRequest();
      request.setMsgtype(message.getMsgType());
      if (null != message.getMsgType() && message.getMsgType().equals("text")) {
        OapiRobotSendRequest.At at = new OapiRobotSendRequest.At();
        // at.setAtMobiles(Arrays.asList("132xxxxxxxx"));
        at.setIsAtAll(message.getIsAtAll());
        request.setAt(at);
        OapiRobotSendRequest.Text text = new OapiRobotSendRequest.Text();
        text.setContent(message.getMsgContent());
        request.setText(text);
      } else if (null != message.getMsgType() && message.getMsgType().equals("link")) {
        OapiRobotSendRequest.Link link = new OapiRobotSendRequest.Link();
        link.setMessageUrl(message.getMsgUrl());
        link.setPicUrl(message.getPicUrl());
        link.setTitle(message.getTitle());
        link.setText(message.getMsgContent());
        request.setLink(link);
      } else if (null != message.getMsgType() && message.getMsgType().equals("markdown")) {
        OapiRobotSendRequest.Markdown markdown = new OapiRobotSendRequest.Markdown();
        markdown.setTitle(message.getTitle());
        markdown.setText(message.getMsgContent());
        request.setMarkdown(markdown);
      }
      OapiRobotSendResponse response = client.execute(request);
      return response;
    } catch (Exception e) {
      log.error("ding robotMsg error", e);
    }
    return null;
  }

  public String getDingRobotMsgUrl(Message message) {
    String robotMsgUrl = dingDing.getRobotMsgUrl();
    List<DingConfig> dingConfigList = MemoryUtil.dingConfigList;
    if (null == dingConfigList || dingConfigList.size() <= 0) {
      return robotMsgUrl;
    }
    for (DingConfig dingConfig : dingConfigList) {
      String keyword = dingConfig.getKeyword();
      if (CommonUtil.isNullOrEmpty(keyword)) {
        continue;
      }
      if (null != message && null != message.getMsgContent() && message.getMsgContent().indexOf(keyword) >= 0) {
        robotMsgUrl = dingConfig.getRobotMsgUrl();
        break;
      }
    }
    return robotMsgUrl;
  }

  public String getDingSecret(Message message) {
    try {
      Long timestamp = System.currentTimeMillis();
      String secret = dingDing.getRobotSecret();
      List<DingConfig> dingConfigList = MemoryUtil.dingConfigList;
      if (null != dingConfigList && dingConfigList.size() > 0) {
        for (DingConfig dingConfig : dingConfigList) {
          String keyword = dingConfig.getKeyword();
          if (CommonUtil.isNullOrEmpty(keyword)) {
            continue;
          }
          if (null != message && null != message.getMsgContent() && message.getMsgContent().indexOf(keyword) >= 0) {
            secret = dingConfig.getRobotSecret();
            break;
          }
        }
      }
      String stringToSign = timestamp + "\n" + secret;
      Mac mac = Mac.getInstance("HmacSHA256");
      mac.init(new SecretKeySpec(secret.getBytes("UTF-8"), "HmacSHA256"));
      byte[] signData = mac.doFinal(stringToSign.getBytes("UTF-8"));
      String sign = URLEncoder.encode(new String(Base64.encodeBase64(signData)), "UTF-8");
      return sign;
    } catch (Exception e) {
      log.error("ding getSecret error", e);
    }
    return null;
  }

}
