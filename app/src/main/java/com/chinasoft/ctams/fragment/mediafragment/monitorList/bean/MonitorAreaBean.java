package com.chinasoft.ctams.fragment.mediafragment.monitorList.bean;

import com.chinasoft.ctams.util.myTree.annotation.TreeNodeId;
import com.chinasoft.ctams.util.myTree.annotation.TreeNodeLabel;
import com.chinasoft.ctams.util.myTree.annotation.TreeNodePid;

/**
 * Created by ls1213 on 2016/8/12.
 */

public class MonitorAreaBean {
    @TreeNodeId
    private String mAId;
    @TreeNodeLabel
    private String mAName;
    @TreeNodePid
    private String mAPid;

    public MonitorAreaBean(String mAId, String mAName, String mAPid) {
        this.mAId = mAId;
        this.mAName = mAName;
        this.mAPid = mAPid;
    }

    public String getmAId() {
        return mAId;
    }

    public void setmAId(String mAId) {
        this.mAId = mAId;
    }

    public String getmAName() {
        return mAName;
    }

    public void setmAName(String mAName) {
        this.mAName = mAName;
    }

    public String getmAPid() {
        return mAPid;
    }

    public void setmAPid(String mAPid) {
        this.mAPid = mAPid;
    }
}
