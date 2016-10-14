package com.chinasoft.ctams.fragment.homePageFragment.schedule.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Kyrie on 2016/8/8.
 * Email:kyrie_liu@sina.com
 */
public class OfficeDailyBean implements Serializable{
    /**
     * pageNo : 1
     * list : [{"area":"","deleflag":"N","organization":"","workschedule":"完成","workscheduleArrangement":"购买防爆盾","workscheduleCreatetime":null,"workscheduleCreatetimeTo":"2016-07-26","workscheduleDate":null,"workscheduleDateTo":"2016-07-29","workscheduleDistrict":"莎车县","workscheduleFounder":"超管管理员","workscheduleId":"4028807e5625f2570156260d02860001","workscheduleJobtype":"办内","workscheduleLeadername":"刘丽","workscheduleMechanism":"喀群乡","workscheduleModifiernaem":"超管管理员","workscheduleModifytime":null,"workscheduleModifytimeTo":"2016-07-28"},{"area":"","deleflag":"N","organization":"","workschedule":"","workscheduleArrangement":"下午三点反恐演习","workscheduleCreatetime":null,"workscheduleCreatetimeTo":"2016-07-26","workscheduleDate":null,"workscheduleDateTo":"2016-07-21","workscheduleDistrict":"莎车县","workscheduleFounder":"超管管理员","workscheduleId":"4028807e5625f257015625f257270000","workscheduleJobtype":"办内","workscheduleLeadername":"壬午","workscheduleMechanism":"达木斯乡","workscheduleModifiernaem":"","workscheduleModifytime":null,"workscheduleModifytimeTo":""},{"area":"","deleflag":"N","organization":"","workschedule":"","workscheduleArrangement":"温热","workscheduleCreatetime":null,"workscheduleCreatetimeTo":"2016-07-29","workscheduleDate":null,"workscheduleDateTo":"2016-07-30","workscheduleDistrict":"莎车县","workscheduleFounder":"超管管理员","workscheduleId":"402880075635560a015635560aa60000","workscheduleJobtype":"办内","workscheduleLeadername":"李","workscheduleMechanism":"莎车县","workscheduleModifiernaem":"","workscheduleModifytime":null,"workscheduleModifytimeTo":""}]
     */

    private ResultBean result;
    /**
     * result : {"pageNo":1,"list":[{"area":"","deleflag":"N","organization":"","workschedule":"完成","workscheduleArrangement":"购买防爆盾","workscheduleCreatetime":null,"workscheduleCreatetimeTo":"2016-07-26","workscheduleDate":null,"workscheduleDateTo":"2016-07-29","workscheduleDistrict":"莎车县","workscheduleFounder":"超管管理员","workscheduleId":"4028807e5625f2570156260d02860001","workscheduleJobtype":"办内","workscheduleLeadername":"刘丽","workscheduleMechanism":"喀群乡","workscheduleModifiernaem":"超管管理员","workscheduleModifytime":null,"workscheduleModifytimeTo":"2016-07-28"},{"area":"","deleflag":"N","organization":"","workschedule":"","workscheduleArrangement":"下午三点反恐演习","workscheduleCreatetime":null,"workscheduleCreatetimeTo":"2016-07-26","workscheduleDate":null,"workscheduleDateTo":"2016-07-21","workscheduleDistrict":"莎车县","workscheduleFounder":"超管管理员","workscheduleId":"4028807e5625f257015625f257270000","workscheduleJobtype":"办内","workscheduleLeadername":"壬午","workscheduleMechanism":"达木斯乡","workscheduleModifiernaem":"","workscheduleModifytime":null,"workscheduleModifytimeTo":""},{"area":"","deleflag":"N","organization":"","workschedule":"","workscheduleArrangement":"温热","workscheduleCreatetime":null,"workscheduleCreatetimeTo":"2016-07-29","workscheduleDate":null,"workscheduleDateTo":"2016-07-30","workscheduleDistrict":"莎车县","workscheduleFounder":"超管管理员","workscheduleId":"402880075635560a015635560aa60000","workscheduleJobtype":"办内","workscheduleLeadername":"李","workscheduleMechanism":"莎车县","workscheduleModifiernaem":"","workscheduleModifytime":null,"workscheduleModifytimeTo":""}]}
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
         * area :
         * deleflag : N
         * organization :
         * workschedule : 完成
         * workscheduleArrangement : 购买防爆盾
         * workscheduleCreatetime : null
         * workscheduleCreatetimeTo : 2016-07-26
         * workscheduleDate : null
         * workscheduleDateTo : 2016-07-29
         * workscheduleDistrict : 莎车县
         * workscheduleFounder : 超管管理员
         * workscheduleId : 4028807e5625f2570156260d02860001
         * workscheduleJobtype : 办内
         * workscheduleLeadername : 刘丽
         * workscheduleMechanism : 喀群乡
         * workscheduleModifiernaem : 超管管理员
         * workscheduleModifytime : null
         * workscheduleModifytimeTo : 2016-07-28
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
            private String organization;
            private String workschedule;
            private String workscheduleArrangement;
            private Object workscheduleCreatetime;
            private String workscheduleCreatetimeTo;
            private Object workscheduleDate;
            private String workscheduleDateTo;
            private String workscheduleDistrict;
            private String workscheduleFounder;
            private String workscheduleId;
            private String workscheduleJobtype;
            private String workscheduleLeadername;
            private String workscheduleMechanism;
            private String workscheduleModifiernaem;
            private Object workscheduleModifytime;
            private String workscheduleModifytimeTo;

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

            public String getOrganization() {
                return organization;
            }

            public void setOrganization(String organization) {
                this.organization = organization;
            }

            public String getWorkschedule() {
                return workschedule;
            }

            public void setWorkschedule(String workschedule) {
                this.workschedule = workschedule;
            }

            public String getWorkscheduleArrangement() {
                return workscheduleArrangement;
            }

            public void setWorkscheduleArrangement(String workscheduleArrangement) {
                this.workscheduleArrangement = workscheduleArrangement;
            }

            public Object getWorkscheduleCreatetime() {
                return workscheduleCreatetime;
            }

            public void setWorkscheduleCreatetime(Object workscheduleCreatetime) {
                this.workscheduleCreatetime = workscheduleCreatetime;
            }

            public String getWorkscheduleCreatetimeTo() {
                return workscheduleCreatetimeTo;
            }

            public void setWorkscheduleCreatetimeTo(String workscheduleCreatetimeTo) {
                this.workscheduleCreatetimeTo = workscheduleCreatetimeTo;
            }

            public Object getWorkscheduleDate() {
                return workscheduleDate;
            }

            public void setWorkscheduleDate(Object workscheduleDate) {
                this.workscheduleDate = workscheduleDate;
            }

            public String getWorkscheduleDateTo() {
                return workscheduleDateTo;
            }

            public void setWorkscheduleDateTo(String workscheduleDateTo) {
                this.workscheduleDateTo = workscheduleDateTo;
            }

            public String getWorkscheduleDistrict() {
                return workscheduleDistrict;
            }

            public void setWorkscheduleDistrict(String workscheduleDistrict) {
                this.workscheduleDistrict = workscheduleDistrict;
            }

            public String getWorkscheduleFounder() {
                return workscheduleFounder;
            }

            public void setWorkscheduleFounder(String workscheduleFounder) {
                this.workscheduleFounder = workscheduleFounder;
            }

            public String getWorkscheduleId() {
                return workscheduleId;
            }

            public void setWorkscheduleId(String workscheduleId) {
                this.workscheduleId = workscheduleId;
            }

            public String getWorkscheduleJobtype() {
                return workscheduleJobtype;
            }

            public void setWorkscheduleJobtype(String workscheduleJobtype) {
                this.workscheduleJobtype = workscheduleJobtype;
            }

            public String getWorkscheduleLeadername() {
                return workscheduleLeadername;
            }

            public void setWorkscheduleLeadername(String workscheduleLeadername) {
                this.workscheduleLeadername = workscheduleLeadername;
            }

            public String getWorkscheduleMechanism() {
                return workscheduleMechanism;
            }

            public void setWorkscheduleMechanism(String workscheduleMechanism) {
                this.workscheduleMechanism = workscheduleMechanism;
            }

            public String getWorkscheduleModifiernaem() {
                return workscheduleModifiernaem;
            }

            public void setWorkscheduleModifiernaem(String workscheduleModifiernaem) {
                this.workscheduleModifiernaem = workscheduleModifiernaem;
            }

            public Object getWorkscheduleModifytime() {
                return workscheduleModifytime;
            }

            public void setWorkscheduleModifytime(Object workscheduleModifytime) {
                this.workscheduleModifytime = workscheduleModifytime;
            }

            public String getWorkscheduleModifytimeTo() {
                return workscheduleModifytimeTo;
            }

            public void setWorkscheduleModifytimeTo(String workscheduleModifytimeTo) {
                this.workscheduleModifytimeTo = workscheduleModifytimeTo;
            }
        }
    }
}
