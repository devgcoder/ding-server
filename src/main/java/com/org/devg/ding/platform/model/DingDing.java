package com.org.devg.ding.platform.model;

import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "dingding")
public class DingDing {

  private String appkey;
  private String appsecret;
  private Long agentId;
  private String chatId;
  private String robotMsgUrl;
  private String robotSecret;
  private Boolean isAtAll;
  private List<Map<String, DingConfig>> configList;
}
