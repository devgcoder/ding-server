package com.org.devg.ding.platform.utils;

import com.org.devg.ding.platform.model.DingConfig;
import java.util.ArrayList;
import java.util.List;

public class MemoryUtil {

  public static final String tokenUrl = "https://oapi.dingtalk.com/gettoken";
  public static final String sendMsgUrl = "https://oapi.dingtalk.com/chat/send";
  public static final String noticeMsgUrl = "https://oapi.dingtalk.com/topapi/message/corpconversation/asyncsend_v2";
  public static final String robotMsgUrl = "https://oapi.dingtalk.com/robot/send?access_token=549b9b9040c2998f1c908187f170e649962b3f7a6aa5dc0e54d7d298e36e3498";
  public static final List<DingConfig> dingConfigList = new ArrayList<>();
}
