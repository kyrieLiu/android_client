package com.chinasoft.ctams.activity.sendPaper.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/6/28.
 */
public class MessageMergeMainParentBean implements Serializable {
    private String messageParentId;
    private String eventName;
    private String eventPlace;
    private String eventTime;
    private String eventOrganization;
    private String eventIntroduction;
    private String eventType;

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getMessageParentId() {
        return messageParentId;
    }

    public void setMessageParentId(String messageParentId) {
        this.messageParentId = messageParentId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventPlace() {
        return eventPlace;
    }

    public void setEventPlace(String eventPlace) {
        this.eventPlace = eventPlace;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getEventOrganization() {
        return eventOrganization;
    }

    public void setEventOrganization(String eventOrganization) {
        this.eventOrganization = eventOrganization;
    }

    public String getEventIntroduction() {
        return eventIntroduction;
    }

    public void setEventIntroduction(String eventIntroduction) {
        this.eventIntroduction = eventIntroduction;
    }
}
