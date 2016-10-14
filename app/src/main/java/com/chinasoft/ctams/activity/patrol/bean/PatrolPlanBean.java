package com.chinasoft.ctams.activity.patrol.bean;

import java.util.List;

/**
 * Created by Kyrie on 2016/7/26.
 * Email:kyrie_liu@sina.com
 */
public class PatrolPlanBean {
    /**
     * patteamArea : 喀群乡
     * patteamBuildtime : 2016-06-08
     * patteamCaptain : 颜能晖
     * patteamContact : 18910777777
     * patteamId : 4028802c559be85f01559c3fbca300fe
     * patteamName : 反恐二组
     * patteamRemarks : 在册
     * planEstimate : 2
     * planId : 402880fc5605c7ae015605c7ae630000
     * planName : 测试1
     * rolroteID : 4028801e5590c43e015590c43e760000
     * routeName : 路线1
     * trajpointList : ["路线点1","路线点2","路线点3","路线点4","路线点5","路线点6"]
     */

    private String patteamArea;
    private String patteamBuildtime;
    private String patteamCaptain;
    private String patteamContact;
    private String patteamId;
    private String patteamName;
    private String patteamRemarks;
    private String planEstimate;
    private String planId;
    private String planName;
    private String rolroteID;
    private String routeName;
    private List<String> trajpointList;
    private int num;

    public String getPatteamArea() {
        return patteamArea;
    }

    public void setPatteamArea(String patteamArea) {
        this.patteamArea = patteamArea;
    }

    public String getPatteamBuildtime() {
        return patteamBuildtime;
    }

    public void setPatteamBuildtime(String patteamBuildtime) {
        this.patteamBuildtime = patteamBuildtime;
    }

    public String getPatteamCaptain() {
        return patteamCaptain;
    }

    public void setPatteamCaptain(String patteamCaptain) {
        this.patteamCaptain = patteamCaptain;
    }

    public String getPatteamContact() {
        return patteamContact;
    }

    public void setPatteamContact(String patteamContact) {
        this.patteamContact = patteamContact;
    }

    public String getPatteamId() {
        return patteamId;
    }

    public void setPatteamId(String patteamId) {
        this.patteamId = patteamId;
    }

    public String getPatteamName() {
        return patteamName;
    }

    public void setPatteamName(String patteamName) {
        this.patteamName = patteamName;
    }

    public String getPatteamRemarks() {
        return patteamRemarks;
    }

    public void setPatteamRemarks(String patteamRemarks) {
        this.patteamRemarks = patteamRemarks;
    }

    public String getPlanEstimate() {
        return planEstimate;
    }

    public void setPlanEstimate(String planEstimate) {
        this.planEstimate = planEstimate;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getRolroteID() {
        return rolroteID;
    }

    public void setRolroteID(String rolroteID) {
        this.rolroteID = rolroteID;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public List<String> getTrajpointList() {
        return trajpointList;
    }

    public void setTrajpointList(List<String> trajpointList) {
        this.trajpointList = trajpointList;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
