package com.org.devg.ding.platform.model;


public class Message {

  private String title;
  private String msgType;
  //  msgType为link时用
  private String msgUrl;
  // msgType为link时用
  private String picUrl;
  private String msgContent;
  private String isAtAll;

  public Message(String msgType, String msgContent) {
    this.msgType = msgType;
    this.msgContent = msgContent;
    this.isAtAll = "false";
  }

  public Message(String msgType, String msgContent, String isAtAll) {
    this.msgType = msgType;
    this.msgContent = msgContent;
    this.isAtAll = isAtAll;
  }

  public Message(String msgType, String title, String msgUrl, String picUrl, String msgContent, String isAtAll) {
    this.title = title;
    this.msgType = msgType;
    this.msgUrl = msgUrl;
    this.picUrl = picUrl;
    this.msgContent = msgContent;
    this.isAtAll = isAtAll;
  }

  public String getMsgType() {
    return msgType;
  }

  public void setMsgType(String msgType) {
    this.msgType = msgType;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getMsgUrl() {
    return msgUrl;
  }

  public void setMsgUrl(String msgUrl) {
    this.msgUrl = msgUrl;
  }

  public String getPicUrl() {
    return picUrl;
  }

  public void setPicUrl(String picUrl) {
    this.picUrl = picUrl;
  }

  public String getMsgContent() {
    return msgContent;
  }

  public void setMsgContent(String msgContent) {
    this.msgContent = msgContent;
  }

  public String getIsAtAll() {
    return isAtAll;
  }

  public void setIsAtAll(String isAtAll) {
    this.isAtAll = isAtAll;
  }

}
