package com.chinasoft.ctams.activity.task.bean;

import java.io.Serializable;

/**
 * Created by ls1213 on 2016/8/10.
 */

public class TaskItemBean implements Serializable {
    private String itemTaskId;
    private String itemTaskDate;
    private String itemTaskType;
    private String itemTaskTitle;
    private String itemTaskMan;
    private String itemTaskContent;

    public String getItemTaskId() {
        return itemTaskId;
    }

    public void setItemTaskId(String itemTaskId) {
        this.itemTaskId = itemTaskId;
    }

    public String getItemTaskDate() {
        return itemTaskDate;
    }

    public void setItemTaskDate(String itemTaskDate) {
        this.itemTaskDate = itemTaskDate;
    }

    public String getItemTaskType() {
        return itemTaskType;
    }

    public void setItemTaskType(String itemTaskType) {
        this.itemTaskType = itemTaskType;
    }

    public String getItemTaskTitle() {
        return itemTaskTitle;
    }

    public void setItemTaskTitle(String itemTaskTitle) {
        this.itemTaskTitle = itemTaskTitle;
    }

    public String getItemTaskMan() {
        return itemTaskMan;
    }

    public void setItemTaskMan(String itemTaskMan) {
        this.itemTaskMan = itemTaskMan;
    }

    public String getItemTaskContent() {
        return itemTaskContent;
    }

    public void setItemTaskContent(String itemTaskContent) {
        this.itemTaskContent = itemTaskContent;
    }
}
