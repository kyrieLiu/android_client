package com.chinasoft.ctams.util;

import android.content.Context;

import com.chinasoft.ctams.view.CustomProgressDialog;

/**
 * Created by Kyrie on 2016/6/30.
 * Email:kyrie_liu@sina.com
 */
public class ProgressDialogUtils {
    private static ProgressDialogUtils utils;
    CustomProgressDialog dialog;
    public static ProgressDialogUtils getInstance(){
        if (utils==null){
            utils=new ProgressDialogUtils();
        }
        return utils;
    }
    private ProgressDialogUtils(){}

    /**
     * 获取加载动画
     * @param context
     * @return
     */
    public CustomProgressDialog getProgressDialog(Context context){
        if (dialog==null){
            dialog=CustomProgressDialog.createDialog(context);
            dialog.setMessage("正在加载...");
        }
        return dialog;
    }
}
