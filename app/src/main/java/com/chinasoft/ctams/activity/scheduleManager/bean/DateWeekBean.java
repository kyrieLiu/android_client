package com.chinasoft.ctams.activity.scheduleManager.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/6/18.
 */
public class DateWeekBean implements Serializable {
    private String week;
    private String dateName;
    private String phone;

    public DateWeekBean(String week, String dateName, String phone) {
        this.week = week;
        this.dateName = dateName;
        this.phone = phone;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getDateName() {
        return dateName;
    }

    public void setDateName(String dateName) {
        this.dateName = dateName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
