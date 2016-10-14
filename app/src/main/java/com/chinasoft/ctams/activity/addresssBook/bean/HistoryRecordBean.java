package com.chinasoft.ctams.activity.addresssBook.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/7/6.
 */
public class HistoryRecordBean implements Serializable {
    private String contactId;
    private String contactName;
    private String contactPhone;
    private String contactState;
    private String contactTime;

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getContactTime() {
        return contactTime;
    }

    public void setContactTime(String contactTime) {
        this.contactTime = contactTime;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactState() {
        return contactState;
    }

    public void setContactState(String contactState) {
        this.contactState = contactState;
    }
}
