package com.chinasoft.ctams.util;

/**
 * Created by Kyrie on 2016/6/22.
 * Email:kyrie_liu@sina.com
 * 存储项目常量
 */
public class ConstantCode {

    //public static final String URL_PSOT ="http://192.168.0.212:8080/ctams/";
//public static final String URL_PSOT ="http://192.168.0.120:8080/ctams/";
//public static final String URL_PSOT ="http://192.168.0.152:8080/ctams/";
    public static final String URL_PSOT ="http://192.168.0.135:8080/ctams/";
    public static final String URL_CAI ="http://192.168.0.135:8080/ctams/";
    public static final String URL_PUSH ="http://192.168.0.135:8080/androidpn/userlist.do";

    public static final int POST_SUCCESS=1;//网络请求成功

    public static final int POST_FAILED=2;//网络请求失败



    public static final int POST_LOGIN_FAULT=3;//用户名密码错误

    public static final int DATA_IS_NULL=4;

    public static final int DATA_DELETED_SUCCESS=5;
    public static final int DATA_DELETED_FAILED=6;
    public static final int DATA_ADD_SUCCESS=7;
    public static final int DATA_ADD_FAILED=8;
    public static final int DATA_DELETED_ITEM_SUCCESS=9;
    public static final int DATA_DELETED_ITEM_FAILED=10;
    public static final int DATA_ADD_UPDATE_SUCCESS=11;
    public static final int DATA_ADD_UPDATE_FAILED=12;
    public static final int DEV_EXCEPTION=0x3334;

    //通知栏跳转值班页面标识
    public static final int TURN_PAGE_NOTIFICATION_SCHEDULE =13;
    //正常跳转页面标识
    public static final int TURN_PAGE_NORMAL=14;
    //通知栏跳转个人通讯录标识
    public  static final int TURN_PAGE_NOTIFICATION_ADDRESSBOOK_PERSONAL=15;

    /* 照片请求码 */
    public static final int IMAGE_REQUEST_CODE = 16;
    public static final int CAMERA_REQUEST_CODE = 17;
    public static final int RESULT_REQUEST_CODE = 18;

    public static final int POST_NULL_RESPONSE=19;//服务器暂无数据

    public static final int GET_DOWNLOAD=20;//model正在下载文件

    public static final int VERSION_CODE_UPDATE=21;//版本更新


}
