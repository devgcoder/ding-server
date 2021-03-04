package com.org.devg.ding.web.controller;

import com.alibaba.fastjson.JSON;
import com.org.devg.ding.platform.model.DingConfig;
import com.org.devg.ding.platform.model.JsonModel;
import com.org.devg.ding.platform.utils.CommonUtil;
import com.org.devg.ding.platform.utils.MemoryUtil;
import com.org.devg.ding.platform.utils.SockerUtil;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author devg
 * @Date 2020/5/10 17:25
 */

@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {

  @RequestMapping("/testDing")
  public JsonModel<List> testDing() {
    List<DingConfig> configList = MemoryUtil.dingConfigList;
    if (null != configList && configList.size() > 0) {
      for (DingConfig dingConfig : configList) {
        System.out.println(JSON.toJSONString(dingConfig));
      }
    }
    return JsonModel.newSuccess(configList);
  }

  @RequestMapping("sayHello")
  public String sayHello() {
    return "hello word";
  }

  @RequestMapping("checkServerAccess")
  public JsonModel<Boolean> checkServerAccess(@RequestBody Map<String, Object> params) {
    String server = CommonUtil.getMapString(params, "server");
    String[] serverConfig = server.split(":");
    if (serverConfig.length != 2) {
      log.error("monitor server config error!");
      return JsonModel.newSuccess(false);
    } else {
      String host = serverConfig[0];
      Integer port = Integer.valueOf(serverConfig[1]);
      SockerUtil sockerUtil = new SockerUtil(host, port);
      boolean bool = sockerUtil.checkHostLogin();
      return JsonModel.newSuccess(bool);
    }
  }
}
