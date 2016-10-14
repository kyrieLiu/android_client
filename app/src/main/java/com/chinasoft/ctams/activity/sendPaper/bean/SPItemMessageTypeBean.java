package com.chinasoft.ctams.activity.sendPaper.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/6/29.
 */
public class SPItemMessageTypeBean implements Serializable {
    private String itemName;
    private String itemCode;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }
}
