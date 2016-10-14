package com.chinasoft.ctams.fragment.mediafragment.monitorList.bean;

import java.util.List;

/**
 * Created by Kyrie on 2016/7/19.
 * Email:kyrie_liu@sina.com
 */
public class MonitorBean {
    /**
     * page : 1
     * total : 1
     * records : 9
     * rows : [{"placeId":"达木斯乡小学","videoBlank":"设备正常","videoDescribe":"达木斯小学摄像头2","videoId":"402880fc55e391ab0155e391ab360000","videoName":"达木斯小学摄像头","videoNumber":"mp32","videoPass":"pingtech123456","videoPort":"8000","videoStatus":"true","videoUrl":"192.168.0.167","videoUser":"admin"},{"placeId":"拍克其乡","videoBlank":"设备正常","videoDescribe":"拍克其乡摄像头","videoId":"4028800b55df656d0155df6aa8190003","videoName":"拍克其乡摄像头","videoNumber":"MP1","videoPass":"pingtech123456","videoPort":"8000","videoStatus":"true","videoUrl":"192.168.0.167","videoUser":"admin"},{"placeId":"莎车镇派出所","videoBlank":"设备正常","videoDescribe":"112222","videoId":"4028800b55df656d0155df6b838c0004","videoName":"111","videoNumber":"111","videoPass":"pingtech123456","videoPort":"8000","videoStatus":"true","videoUrl":"192.168.0.167","videoUser":"admin"},{"placeId":"米夏乡清真寺","videoBlank":"设备正常","videoDescribe":"米夏乡清真寺摄像头","videoId":"402880fc55e1fe490155e2005b700002","videoName":"米夏乡清真寺摄像头","videoNumber":"mp4","videoPass":"pingtech123456","videoPort":"8000","videoStatus":"true","videoUrl":"192.168.0.167","videoUser":"admin"},{"placeId":"莎车镇_清真寺","videoBlank":"设备正常","videoDescribe":"2121","videoId":"4028800b55df656d0155df744f3e0008","videoName":"2121","videoNumber":"1221","videoPass":"pingtech123456","videoPort":"8000","videoStatus":"true","videoUrl":"192.168.0.167","videoUser":"admin"},{"placeId":"莎车镇_清真寺","videoBlank":"设备正常","videoDescribe":"测试","videoId":"402880fc55e1fe490155e1fe49eb0000","videoName":"测试1","videoNumber":"mp3","videoPass":"pingtech123456","videoPort":"8000","videoStatus":"true","videoUrl":"192.168.0.167","videoUser":"admin"},{"placeId":"122","videoBlank":"设备正常","videoDescribe":"123","videoId":"402880fc55e391ab0155e39c48430004","videoName":"123","videoNumber":"123","videoPass":"pingtech123456","videoPort":"8000","videoStatus":"true","videoUrl":"192.168.0.167","videoUser":"admin"},{"placeId":"122","videoBlank":"设备正常","videoDescribe":"asd","videoId":"402880fc55e391ab0155e394d88d0001","videoName":"asd","videoNumber":"asd","videoPass":"pingtech123456","videoPort":"8000","videoStatus":"true","videoUrl":"192.168.0.167","videoUser":"admin"},{"placeId":"莎车镇_清真寺","videoBlank":"设备正常","videoDescribe":"1221","videoId":"402880fc55e391ab0155e39ae41e0002","videoName":"1212","videoNumber":"1212","videoPass":"pingtech123456","videoPort":"8000","videoStatus":"true","videoUrl":"192.168.0.167","videoUser":"admin"}]
     */

    private String page;
    private String total;
    private String records;
    /**
     * placeId : 达木斯乡小学
     * videoBlank : 设备正常
     * videoDescribe : 达木斯小学摄像头2
     * videoId : 402880fc55e391ab0155e391ab360000
     * videoName : 达木斯小学摄像头
     * videoNumber : mp32
     * videoPass : pingtech123456
     * videoPort : 8000
     * videoStatus : true
     * videoUrl : 192.168.0.167
     * videoUser : admin
     */

    private List<MonitorRowsBean> rows;

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getRecords() {
        return records;
    }

    public void setRecords(String records) {
        this.records = records;
    }

    public List<MonitorRowsBean> getRows() {
        return rows;
    }

    public void setRows(List<MonitorRowsBean> rows) {
        this.rows = rows;
    }

    public static class MonitorRowsBean {
        private String placeId;
        private String videoBlank;
        private String videoDescribe;
        private String videoId;
        private String videoName;
        private String videoNumber;
        private String videoPass;
        private String videoPort;
        private String videoStatus;
        private String videoUrl;
        private String videoUser;

        public String getPlaceId() {
            return placeId;
        }

        public void setPlaceId(String placeId) {
            this.placeId = placeId;
        }

        public String getVideoBlank() {
            return videoBlank;
        }

        public void setVideoBlank(String videoBlank) {
            this.videoBlank = videoBlank;
        }

        public String getVideoDescribe() {
            return videoDescribe;
        }

        public void setVideoDescribe(String videoDescribe) {
            this.videoDescribe = videoDescribe;
        }

        public String getVideoId() {
            return videoId;
        }

        public void setVideoId(String videoId) {
            this.videoId = videoId;
        }

        public String getVideoName() {
            return videoName;
        }

        public void setVideoName(String videoName) {
            this.videoName = videoName;
        }

        public String getVideoNumber() {
            return videoNumber;
        }

        public void setVideoNumber(String videoNumber) {
            this.videoNumber = videoNumber;
        }

        public String getVideoPass() {
            return videoPass;
        }

        public void setVideoPass(String videoPass) {
            this.videoPass = videoPass;
        }

        public String getVideoPort() {
            return videoPort;
        }

        public void setVideoPort(String videoPort) {
            this.videoPort = videoPort;
        }

        public String getVideoStatus() {
            return videoStatus;
        }

        public void setVideoStatus(String videoStatus) {
            this.videoStatus = videoStatus;
        }

        public String getVideoUrl() {
            return videoUrl;
        }

        public void setVideoUrl(String videoUrl) {
            this.videoUrl = videoUrl;
        }

        public String getVideoUser() {
            return videoUser;
        }

        public void setVideoUser(String videoUser) {
            this.videoUser = videoUser;
        }
    }
}
