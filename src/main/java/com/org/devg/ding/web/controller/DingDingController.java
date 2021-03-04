package com.org.devg.ding.web.controller;

import com.alibaba.fastjson.JSON;
import com.dingtalk.api.response.OapiChatSendResponse;
import com.dingtalk.api.response.OapiMessageCorpconversationAsyncsendV2Response;
import com.dingtalk.api.response.OapiRobotSendResponse;
import com.org.devg.ding.platform.model.DingDing;
import com.org.devg.ding.platform.model.JsonModel;
import com.org.devg.ding.platform.model.Message;
import com.org.devg.ding.platform.utils.CommonUtil;
import com.org.devg.ding.platform.utils.DingUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/dingDing")
public class DingDingController {

  @Autowired
  private DingDing dingDing;

  @RequestMapping("/sendMsg")
  public JsonModel sendMsg(@RequestBody Map<String, Object> params) {

    String msgType = CommonUtil.getMapString(params, "msgType");
    String msgContent = CommonUtil.getMapString(params, "msgContent");
    if (CommonUtil.isNullOrEmpty(msgType)) {
      return JsonModel.newFail("msgType can not be null");
    }
    if (CommonUtil.isNullOrEmpty(msgContent)) {
      return JsonModel.newFail("msgContent can not be null");
    }
    //第一步：获取accessToken
    DingUtil dingUtil = new DingUtil(dingDing);
    String accessToken = dingUtil.getAccessToken();
    String chatId = dingDing.getChatId();
    Message message = new Message(msgType, msgContent);
    // 第二步：发送消息
    OapiChatSendResponse rsp = dingUtil.sendMessage(message, chatId, accessToken);
    return JsonModel.newSuccess(rsp);
  }

  @RequestMapping("/noticeMsg")
  public JsonModel noticeMsg(@RequestBody Map<String, Object> params) {

    String msgType = CommonUtil.getMapString(params, "msgType");
    String msgContent = CommonUtil.getMapString(params, "msgContent");
    if (CommonUtil.isNullOrEmpty(msgType)) {
      return JsonModel.newFail("msgType can not be null");
    }
    if (CommonUtil.isNullOrEmpty(msgContent)) {
      return JsonModel.newFail("msgContent can not be null");
    }
    //第一步：获取accessToken
    DingUtil dingUtil = new DingUtil(dingDing);
    String accessToken = dingUtil.getAccessToken();
    Long agentId = dingDing.getAgentId();
    Message message = new Message(msgType, msgContent);
    // 第二步：通知消息
    OapiMessageCorpconversationAsyncsendV2Response rsp = dingUtil.noticeMessage(message, agentId, accessToken);
    return JsonModel.newSuccess(rsp);
  }

  @RequestMapping("/robotMsg")
  public JsonModel robotMsg(@RequestBody Map<String, Object> params) {
    String title = CommonUtil.getMapString(params, "title", "");
    String msgType = CommonUtil.getMapString(params, "msgType");
    String msgUrl = CommonUtil.getMapString(params, "msgUrl", "");
    String picUrl = CommonUtil.getMapString(params, "picUrl", "");
    String msgContent = CommonUtil.getMapString(params, "msgContent");
    String isAtAll = CommonUtil.getMapString(params, "isAtAll", "false");
    if (CommonUtil.isNullOrEmpty(msgType)) {
      return JsonModel.newFail("msgType can not be null");
    }
    if (CommonUtil.isNullOrEmpty(msgContent)) {
      return JsonModel.newFail("msgContent can not be null");
    }
    DingUtil dingUtil = new DingUtil(dingDing);
    Message message = new Message(msgType, title, msgUrl, picUrl, msgContent, isAtAll);
    OapiRobotSendResponse rsp = dingUtil.robotMsg(message);
    JsonModel result = JsonModel.newSuccess(rsp);
    log.info("send robot message result:" + JSON.toJSONString(result));
    return result;
  }

  @RequestMapping("/callbackMsg")
  public JsonModel callbackMsg(@RequestBody Map<String, Object> params) {
    log.info(JSON.toJSONString(params));
    return JsonModel.newSuccess(params);
  }
}
