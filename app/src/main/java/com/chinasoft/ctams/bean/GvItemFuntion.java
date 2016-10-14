package com.chinasoft.ctams.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/6/16.
 */
public class GvItemFuntion implements Serializable {
    private int icon;
    private String itemFunctionName;

    public GvItemFuntion(int icon, String itemFunctionName) {
        this.icon = icon;
        this.itemFunctionName = itemFunctionName;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getItemFunctionName() {
        return itemFunctionName;
    }

    public void setItemFunctionName(String itemFunctionName) {
        this.itemFunctionName = itemFunctionName;
    }
}
