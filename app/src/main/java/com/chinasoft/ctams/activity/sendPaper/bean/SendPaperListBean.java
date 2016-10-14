package com.chinasoft.ctams.activity.sendPaper.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/6/25.
 */
public class SendPaperListBean implements Serializable {
    private String sendPaperId;
    private String sendPaperTitle;
    private String sendPaperType;
    private String sendPaperStatus;
    private String sendPaperTime;

    public String getSendPaperId() {
        return sendPaperId;
    }

    public void setSendPaperId(String sendPaperId) {
        this.sendPaperId = sendPaperId;
    }

    public String getSendPaperTitle() {
        return sendPaperTitle;
    }

    public void setSendPaperTitle(String sendPaperTitle) {
        this.sendPaperTitle = sendPaperTitle;
    }

    public String getSendPaperType() {
        return sendPaperType;
    }

    public void setSendPaperType(String sendPaperType) {
        this.sendPaperType = sendPaperType;
    }

    public String getSendPaperStatus() {
        return sendPaperStatus;
    }

    public void setSendPaperStatus(String sendPaperStatus) {
        this.sendPaperStatus = sendPaperStatus;
    }

    public String getSendPaperTime() {
        return sendPaperTime;
    }

    public void setSendPaperTime(String sendPaperTime) {
        this.sendPaperTime = sendPaperTime;
    }
}
