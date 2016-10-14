package com.chinasoft.ctams.bean.bean_json;

import java.util.List;

/**
 * Created by Kyrie on 2016/6/29.
 * Email:kyrie_liu@sina.com
 */
public class MonitorVideoListBean {

    /**
     * placeId : 65312503002160390886
     * placeName : 8村3组清真寺
     * resultMap : {"autoMapping":false,"id":"com.icss.videoaccess.model.VideoAccessMQ","mappedColumns":[],"propertyResultMappings":[],"resultMappings":[],"type":"com.icss.videoaccess.model.VideoAccessMQ"}
     * videoDescribe : 街道1
     * videoId : 65312503001310010298
     * videoName : 摄像头1
     * videoNumber : 001
     * videoPass : pingtech123456

     * videoPort : 80
     * videoUrl : 192.168.0.167
     * videoUser : admin
     */

    private String placeId;
    private String placeName;
    /**
     * autoMapping : false
     * id : com.icss.videoaccess.model.VideoAccessMQ
     * mappedColumns : []
     * propertyResultMappings : []
     * resultMappings : []
     * type : com.icss.videoaccess.model.VideoAccessMQ
     */

    private ResultMapBean resultMap;
    private String videoDescribe;
    private String videoId;
    private String videoName;
    private String videoNumber;
    private String videoPass;
    private String videoPort;
    private String videoUrl;
    private String videoUser;

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public ResultMapBean getResultMap() {
        return resultMap;
    }

    public void setResultMap(ResultMapBean resultMap) {
        this.resultMap = resultMap;
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

    public static class ResultMapBean {
        private boolean autoMapping;
        private String id;
        private String type;
        private List<?> mappedColumns;
        private List<?> propertyResultMappings;
        private List<?> resultMappings;

        public boolean isAutoMapping() {
            return autoMapping;
        }

        public void setAutoMapping(boolean autoMapping) {
            this.autoMapping = autoMapping;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<?> getMappedColumns() {
            return mappedColumns;
        }

        public void setMappedColumns(List<?> mappedColumns) {
            this.mappedColumns = mappedColumns;
        }

        public List<?> getPropertyResultMappings() {
            return propertyResultMappings;
        }

        public void setPropertyResultMappings(List<?> propertyResultMappings) {
            this.propertyResultMappings = propertyResultMappings;
        }

        public List<?> getResultMappings() {
            return resultMappings;
        }

        public void setResultMappings(List<?> resultMappings) {
            this.resultMappings = resultMappings;
        }
    }
}
