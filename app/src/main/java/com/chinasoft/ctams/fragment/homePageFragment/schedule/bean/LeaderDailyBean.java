package com.chinasoft.ctams.fragment.homePageFragment.schedule.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Kyrie on 2016/8/8.
 * Email:kyrie_liu@sina.com
 */
public class LeaderDailyBean implements Serializable{

    /**
     * pageNo : 1
     * list : [{"area":"菜其力克村","deleflag":"N","leaderscheduleArrangement":"234","leaderscheduleCreatetime":null,"leaderscheduleCreatetimePage":"2016-08-04","leaderscheduleDate":null,"leaderscheduleDatePage":"2016-08-01","leaderscheduleDistrict":"","leaderscheduleFounder":"超管管理员","leaderscheduleId":"4028807e56555b930156555b93db0000","leaderscheduleJobtype":"领导","leaderscheduleLeadername":"晓雾","leaderscheduleMechanism":"","leaderscheduleModifiername":"超管管理员","leaderscheduleModifytime":null,"leaderscheduleModifytimePage":"2016-08-04","leaderscheduleRecord":"234234234234","organization":"迎宾路社区(2014年建)"},{"area":"艾里西湖镇","deleflag":"N","leaderscheduleArrangement":"222","leaderscheduleCreatetime":null,"leaderscheduleCreatetimePage":"2016-08-04","leaderscheduleDate":null,"leaderscheduleDatePage":"2016-08-25","leaderscheduleDistrict":"402881e95586e089015586eff5730003","leaderscheduleFounder":"超管管理员","leaderscheduleId":"4028807e56553539015655376dda0001","leaderscheduleJobtype":"领导","leaderscheduleLeadername":"222","leaderscheduleMechanism":"49A8D58D849B4543BE23E4F6220788A6","leaderscheduleModifiername":"超管管理员","leaderscheduleModifytime":null,"leaderscheduleModifytimePage":"2016-08-05","leaderscheduleRecord":"222","organization":"墩巴格乡"},{"area":"莎车县","deleflag":"N","leaderscheduleArrangement":"123","leaderscheduleCreatetime":null,"leaderscheduleCreatetimePage":"2016-08-04","leaderscheduleDate":null,"leaderscheduleDatePage":"2016-09-14","leaderscheduleDistrict":"1","leaderscheduleFounder":"超管管理员","leaderscheduleId":"4028807e56556a9f0156556a9f320000","leaderscheduleJobtype":"领导","leaderscheduleLeadername":"张丹","leaderscheduleMechanism":"932125E0AC14403F902F46BE08EC061E","leaderscheduleModifiername":"","leaderscheduleModifytime":null,"leaderscheduleModifytimePage":"","leaderscheduleRecord":"123","organization":"巴格阿瓦提乡"}]
     */

    private ResultBean result;
    /**
     * result : {"pageNo":1,"list":[{"area":"菜其力克村","deleflag":"N","leaderscheduleArrangement":"234","leaderscheduleCreatetime":null,"leaderscheduleCreatetimePage":"2016-08-04","leaderscheduleDate":null,"leaderscheduleDatePage":"2016-08-01","leaderscheduleDistrict":"","leaderscheduleFounder":"超管管理员","leaderscheduleId":"4028807e56555b930156555b93db0000","leaderscheduleJobtype":"领导","leaderscheduleLeadername":"晓雾","leaderscheduleMechanism":"","leaderscheduleModifiername":"超管管理员","leaderscheduleModifytime":null,"leaderscheduleModifytimePage":"2016-08-04","leaderscheduleRecord":"234234234234","organization":"迎宾路社区(2014年建)"},{"area":"艾里西湖镇","deleflag":"N","leaderscheduleArrangement":"222","leaderscheduleCreatetime":null,"leaderscheduleCreatetimePage":"2016-08-04","leaderscheduleDate":null,"leaderscheduleDatePage":"2016-08-25","leaderscheduleDistrict":"402881e95586e089015586eff5730003","leaderscheduleFounder":"超管管理员","leaderscheduleId":"4028807e56553539015655376dda0001","leaderscheduleJobtype":"领导","leaderscheduleLeadername":"222","leaderscheduleMechanism":"49A8D58D849B4543BE23E4F6220788A6","leaderscheduleModifiername":"超管管理员","leaderscheduleModifytime":null,"leaderscheduleModifytimePage":"2016-08-05","leaderscheduleRecord":"222","organization":"墩巴格乡"},{"area":"莎车县","deleflag":"N","leaderscheduleArrangement":"123","leaderscheduleCreatetime":null,"leaderscheduleCreatetimePage":"2016-08-04","leaderscheduleDate":null,"leaderscheduleDatePage":"2016-09-14","leaderscheduleDistrict":"1","leaderscheduleFounder":"超管管理员","leaderscheduleId":"4028807e56556a9f0156556a9f320000","leaderscheduleJobtype":"领导","leaderscheduleLeadername":"张丹","leaderscheduleMechanism":"932125E0AC14403F902F46BE08EC061E","leaderscheduleModifiername":"","leaderscheduleModifytime":null,"leaderscheduleModifytimePage":"","leaderscheduleRecord":"123","organization":"巴格阿瓦提乡"}]}
     * reason : 成功！
     * resultCode : 0
     */

    private String reason;
    private int resultCode;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public static class ResultBean implements Serializable{
        private int pageNo;
        /**
         * area : 菜其力克村
         * deleflag : N
         * leaderscheduleArrangement : 234
         * leaderscheduleCreatetime : null
         * leaderscheduleCreatetimePage : 2016-08-04
         * leaderscheduleDate : null
         * leaderscheduleDatePage : 2016-08-01
         * leaderscheduleDistrict :
         * leaderscheduleFounder : 超管管理员
         * leaderscheduleId : 4028807e56555b930156555b93db0000
         * leaderscheduleJobtype : 领导
         * leaderscheduleLeadername : 晓雾
         * leaderscheduleMechanism :
         * leaderscheduleModifiername : 超管管理员
         * leaderscheduleModifytime : null
         * leaderscheduleModifytimePage : 2016-08-04
         * leaderscheduleRecord : 234234234234
         * organization : 迎宾路社区(2014年建)
         */

        private List<ListBean> list;

        public int getPageNo() {
            return pageNo;
        }

        public void setPageNo(int pageNo) {
            this.pageNo = pageNo;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean implements Serializable{
            private String area;
            private String deleflag;
            private String leaderscheduleArrangement;
            private Object leaderscheduleCreatetime;
            private String leaderscheduleCreatetimePage;
            private Object leaderscheduleDate;
            private String leaderscheduleDatePage;
            private String leaderscheduleDistrict;
            private String leaderscheduleFounder;
            private String leaderscheduleId;
            private String leaderscheduleJobtype;
            private String leaderscheduleLeadername;
            private String leaderscheduleMechanism;
            private String leaderscheduleModifiername;
            private Object leaderscheduleModifytime;
            private String leaderscheduleModifytimePage;
            private String leaderscheduleRecord;
            private String organization;

            public String getArea() {
                return area;
            }

            public void setArea(String area) {
                this.area = area;
            }

            public String getDeleflag() {
                return deleflag;
            }

            public void setDeleflag(String deleflag) {
                this.deleflag = deleflag;
            }

            public String getLeaderscheduleArrangement() {
                return leaderscheduleArrangement;
            }

            public void setLeaderscheduleArrangement(String leaderscheduleArrangement) {
                this.leaderscheduleArrangement = leaderscheduleArrangement;
            }

            public Object getLeaderscheduleCreatetime() {
                return leaderscheduleCreatetime;
            }

            public void setLeaderscheduleCreatetime(Object leaderscheduleCreatetime) {
                this.leaderscheduleCreatetime = leaderscheduleCreatetime;
            }

            public String getLeaderscheduleCreatetimePage() {
                return leaderscheduleCreatetimePage;
            }

            public void setLeaderscheduleCreatetimePage(String leaderscheduleCreatetimePage) {
                this.leaderscheduleCreatetimePage = leaderscheduleCreatetimePage;
            }

            public Object getLeaderscheduleDate() {
                return leaderscheduleDate;
            }

            public void setLeaderscheduleDate(Object leaderscheduleDate) {
                this.leaderscheduleDate = leaderscheduleDate;
            }

            public String getLeaderscheduleDatePage() {
                return leaderscheduleDatePage;
            }

            public void setLeaderscheduleDatePage(String leaderscheduleDatePage) {
                this.leaderscheduleDatePage = leaderscheduleDatePage;
            }

            public String getLeaderscheduleDistrict() {
                return leaderscheduleDistrict;
            }

            public void setLeaderscheduleDistrict(String leaderscheduleDistrict) {
                this.leaderscheduleDistrict = leaderscheduleDistrict;
            }

            public String getLeaderscheduleFounder() {
                return leaderscheduleFounder;
            }

            public void setLeaderscheduleFounder(String leaderscheduleFounder) {
                this.leaderscheduleFounder = leaderscheduleFounder;
            }

            public String getLeaderscheduleId() {
                return leaderscheduleId;
            }

            public void setLeaderscheduleId(String leaderscheduleId) {
                this.leaderscheduleId = leaderscheduleId;
            }

            public String getLeaderscheduleJobtype() {
                return leaderscheduleJobtype;
            }

            public void setLeaderscheduleJobtype(String leaderscheduleJobtype) {
                this.leaderscheduleJobtype = leaderscheduleJobtype;
            }

            public String getLeaderscheduleLeadername() {
                return leaderscheduleLeadername;
            }

            public void setLeaderscheduleLeadername(String leaderscheduleLeadername) {
                this.leaderscheduleLeadername = leaderscheduleLeadername;
            }

            public String getLeaderscheduleMechanism() {
                return leaderscheduleMechanism;
            }

            public void setLeaderscheduleMechanism(String leaderscheduleMechanism) {
                this.leaderscheduleMechanism = leaderscheduleMechanism;
            }

            public String getLeaderscheduleModifiername() {
                return leaderscheduleModifiername;
            }

            public void setLeaderscheduleModifiername(String leaderscheduleModifiername) {
                this.leaderscheduleModifiername = leaderscheduleModifiername;
            }

            public Object getLeaderscheduleModifytime() {
                return leaderscheduleModifytime;
            }

            public void setLeaderscheduleModifytime(Object leaderscheduleModifytime) {
                this.leaderscheduleModifytime = leaderscheduleModifytime;
            }

            public String getLeaderscheduleModifytimePage() {
                return leaderscheduleModifytimePage;
            }

            public void setLeaderscheduleModifytimePage(String leaderscheduleModifytimePage) {
                this.leaderscheduleModifytimePage = leaderscheduleModifytimePage;
            }

            public String getLeaderscheduleRecord() {
                return leaderscheduleRecord;
            }

            public void setLeaderscheduleRecord(String leaderscheduleRecord) {
                this.leaderscheduleRecord = leaderscheduleRecord;
            }

            public String getOrganization() {
                return organization;
            }

            public void setOrganization(String organization) {
                this.organization = organization;
            }
        }
    }
}
