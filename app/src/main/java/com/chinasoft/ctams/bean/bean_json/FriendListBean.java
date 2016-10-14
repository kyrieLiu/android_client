package com.chinasoft.ctams.bean.bean_json;

/**
 * Created by Kyrie on 2016/6/22.
 * Email:kyrie_liu@sina.com
 */
public class FriendListBean {


    /**
     * id : 30
     * username : 欧文
     * password : f4a8fa58181f4f57aa5f6926dc05e11e
     * createdDate : Jun 22, 2016 9:56:25 AM
     * online : false
     */

    private int id;
    private String accountName;
    private String peopleName;
    private String password;
    private String createdDate;
    private String headPictureUrl;
    private boolean online;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public String getPeopleName() {
        return peopleName;
    }

    public void setPeopleName(String peopleName) {
        this.peopleName = peopleName;
    }

    public String getHeadPictureUrl() {
        return headPictureUrl;
    }

    public void setHeadPictureUrl(String headPictureUrl) {
        this.headPictureUrl = headPictureUrl;
    }
}
