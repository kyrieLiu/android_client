package com.chinasoft.ctams.fragment.mineMainFragment.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/6/18.
 */
public class UserInfoBean implements Serializable {
    private String peopleName;
    private String peopleSex;
    private String peopleDate;
    private String peopleNational;
    private String peopleDivision;
    private String peopleOrganization;
    private String peoplePhone;
    private String peoplePhotourl;
    private String peopleLoginname;

    public String getPeoplePhotourl() {
        return peoplePhotourl;
    }

    public void setPeoplePhotourl(String peoplePhotourl) {
        this.peoplePhotourl = peoplePhotourl;
    }

    public UserInfoBean() {
    }

    public String getPeopleName() {
        return peopleName;
    }

    public void setPeopleName(String peopleName) {
        this.peopleName = peopleName;
    }

    public String getPeopleSex() {
        return peopleSex;
    }

    public void setPeopleSex(String peopleSex) {
        this.peopleSex = peopleSex;
    }

    public String getPeopleDate() {
        return peopleDate;
    }

    public void setPeopleDate(String peopleDate) {
        this.peopleDate = peopleDate;
    }

    public String getPeopleNational() {
        return peopleNational;
    }

    public void setPeopleNational(String peopleNational) {
        this.peopleNational = peopleNational;
    }

    public String getPeopleDivision() {
        return peopleDivision;
    }

    public void setPeopleDivision(String peopleDivision) {
        this.peopleDivision = peopleDivision;
    }

    public String getPeopleOrganization() {
        return peopleOrganization;
    }

    public void setPeopleOrganization(String peopleOrganization) {
        this.peopleOrganization = peopleOrganization;
    }

    public String getPeoplePhone() {
        return peoplePhone;
    }

    public void setPeoplePhone(String peoplePhone) {
        this.peoplePhone = peoplePhone;
    }

    public String getPeopleLoginname() {
        return peopleLoginname;
    }

    public void setPeopleLoginname(String peopleLoginname) {
        this.peopleLoginname = peopleLoginname;
    }
}
