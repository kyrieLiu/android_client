package com.chinasoft.ctams.activity.task.bean;

import java.io.Serializable;

/**
 * Created by ls1213 on 2016/8/10.
 */

public class TaskListBean implements Serializable {
    private String thingId;
    private String thingIntroduction;
    private String thingDate;
    private String thingMechanism;
    private String thingName;
    private String thingPlace;
    private String thingType;

    public String getThingId() {
        return thingId;
    }

    public void setThingId(String thingId) {
        this.thingId = thingId;
    }

    public String getThingIntroduction() {
        return thingIntroduction;
    }

    public void setThingIntroduction(String thingIntroduction) {
        this.thingIntroduction = thingIntroduction;
    }

    public String getThingDate() {
        return thingDate;
    }

    public void setThingDate(String thingDate) {
        this.thingDate = thingDate;
    }

    public String getThingMechanism() {
        return thingMechanism;
    }

    public void setThingMechanism(String thingMechanism) {
        this.thingMechanism = thingMechanism;
    }

    public String getThingName() {
        return thingName;
    }

    public void setThingName(String thingName) {
        this.thingName = thingName;
    }

    public String getThingPlace() {
        return thingPlace;
    }

    public void setThingPlace(String thingPlace) {
        this.thingPlace = thingPlace;
    }

    public String getThingType() {
        return thingType;
    }

    public void setThingType(String thingType) {
        this.thingType = thingType;
    }
}
