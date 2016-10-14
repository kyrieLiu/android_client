package com.chinasoft.ctams.activity.scheduleManager.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/6/23.
 */
public class DateListBean implements Serializable {
    private String dateListId;
    private String dateListName;
    private String organization;
    private String startTime;
    private String endTime;

    public String getDateListId() {
        return dateListId;
    }

    public void setDateListId(String dateListId) {
        this.dateListId = dateListId;
    }

    public String getDateListName() {
        return dateListName;
    }

    public void setDateListName(String dateListName) {
        this.dateListName = dateListName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
