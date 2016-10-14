package com.chinasoft.ctams.fragment.mineMainFragment.bean;

import com.chinasoft.ctams.util.myTree.annotation.TreeNodeId;
import com.chinasoft.ctams.util.myTree.annotation.TreeNodeLabel;
import com.chinasoft.ctams.util.myTree.annotation.TreeNodePid;

/**
 * Created by ls1213 on 2016/8/5.
 */

public class   OrgBean {
    @TreeNodeId
    private String orgId;
    @TreeNodeLabel
    private String orgName;
    @TreeNodePid
    private String orgPid;

    public OrgBean(String orgId, String orgName, String orgPid) {
        this.orgId = orgId;
        this.orgName = orgName;
        this.orgPid = orgPid;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOrgPid() {
        return orgPid;
    }

    public void setOrgPid(String orgPid) {
        this.orgPid = orgPid;
    }
}
