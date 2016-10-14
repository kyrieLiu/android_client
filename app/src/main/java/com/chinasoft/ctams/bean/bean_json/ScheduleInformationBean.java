package com.chinasoft.ctams.bean.bean_json;

import java.util.List;

/**
 * Created by Kyrie on 2016/6/27.
 * Email:kyrie_liu@sina.com
 */
public class ScheduleInformationBean {

    /**
     * deleflag :
     * orgName :
     * schedulEndtime : 2016-05-22
     * schedulId : 4028801c558fcde601558fd1044d0013
     * schedulIndicate : 表6
     * schedulLeader : 小名
     * schedulMechanism : 社会治安办
     * schedulPhone : 12345678911
     * schedulStarttime : 2016-05-16
     * schedulType : 平时值班
     */

    private String deleflag;
    private String orgName;
    private String schedulEndtime;
    private String schedulId;
    private String schedulIndicate;
    private String schedulLeader;
    private String schedulMechanism;
    private String schedulPhone;
    private String schedulStarttime;
    private String schedulType;

    private List<ScheduleDetailsBean> list;
    public String getDeleflag() {
        return deleflag;
    }

    public void setDeleflag(String deleflag) {
        this.deleflag = deleflag;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getSchedulEndtime() {
        return schedulEndtime;
    }

    public void setSchedulEndtime(String schedulEndtime) {
        this.schedulEndtime = schedulEndtime;
    }

    public String getSchedulId() {
        return schedulId;
    }

    public void setSchedulId(String schedulId) {
        this.schedulId = schedulId;
    }

    public String getSchedulIndicate() {
        return schedulIndicate;
    }

    public void setSchedulIndicate(String schedulIndicate) {
        this.schedulIndicate = schedulIndicate;
    }

    public String getSchedulLeader() {
        return schedulLeader;
    }

    public void setSchedulLeader(String schedulLeader) {
        this.schedulLeader = schedulLeader;
    }

    public String getSchedulMechanism() {
        return schedulMechanism;
    }

    public void setSchedulMechanism(String schedulMechanism) {
        this.schedulMechanism = schedulMechanism;
    }

    public String getSchedulPhone() {
        return schedulPhone;
    }

    public void setSchedulPhone(String schedulPhone) {
        this.schedulPhone = schedulPhone;
    }

    public String getSchedulStarttime() {
        return schedulStarttime;
    }

    public void setSchedulStarttime(String schedulStarttime) {
        this.schedulStarttime = schedulStarttime;
    }

    public String getSchedulType() {
        return schedulType;
    }

    public void setSchedulType(String schedulType) {
        this.schedulType = schedulType;
    }

    public List<ScheduleDetailsBean> getList() {
        return list;
    }

    public void setList(List<ScheduleDetailsBean> list) {
        this.list = list;
    }
}
