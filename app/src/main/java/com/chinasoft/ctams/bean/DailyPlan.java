package com.chinasoft.ctams.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/6/17.
 */
public class DailyPlan implements Serializable {
    private String title;

    public DailyPlan(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
