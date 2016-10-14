package com.chinasoft.ctams.activity.search.bean;

import java.io.Serializable;

/**
 * Created by Kyrie on 2016/7/28.
 * Email:kyrie_liu@sina.com
 */
public class ComprehensiveSearchBean implements Serializable {


    /**
     * lFounder : 超管管理员
     * lOrganization :
     * lTimer : 2016-07-11
     * lTitle : 游客于清真寺掉了一块钱
     * lType : 交通事故
     * lwordurl : wordUpload\output\1469673359639.doc
     */

    private String lFounder;
    private String lOrganization;
    private String lTimer;
    private String lTitle;
    private String lType;
    private String lwordurl;



    public String getLFounder() {
        return lFounder;
    }

    public void setLFounder(String lFounder) {
        this.lFounder = lFounder;
    }

    public String getLOrganization() {
        return lOrganization;
    }

    public void setLOrganization(String lOrganization) {
        this.lOrganization = lOrganization;
    }

    public String getLTimer() {
        return lTimer;
    }

    public void setLTimer(String lTimer) {
        this.lTimer = lTimer;
    }

    public String getLTitle() {
        return lTitle;
    }

    public void setLTitle(String lTitle) {
        this.lTitle = lTitle;
    }

    public String getLType() {
        return lType;
    }

    public void setLType(String lType) {
        this.lType = lType;
    }

    public String getLwordurl() {
        return lwordurl;
    }

    public void setLwordurl(String lwordurl) {
        this.lwordurl = lwordurl;
    }
}
