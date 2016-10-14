package com.chinasoft.ctams.model;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.chinasoft.ctams.util.SharedPreferencesHelper;

/**
 * 该类只能有一个实例  单例
 * 如果打开多个定位耗电  而且同时只能有一个在使用
 * @author Administrator
 *
 */
public class GPSInfoService {

    private static GPSInfoService mInstance;
    private LocationManager locationManager;// 定位服务
    private MyLocationListener listener;
    private Context context;
    public static boolean isPatrol=false;
    public static int patrolNumber=1;
    private String loginName;



    private GPSInfoService(Context context) {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        this.context=context;

    }

    public static GPSInfoService getInstance(Context context){
        if(mInstance == null){
            mInstance = new GPSInfoService(context);
        }
        return mInstance;
    }

    /************注册定位监听**************************/
    public void registenerLocationChangeListener(){

        // 查询条件
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE); // 定位的精准度
        criteria.setAltitudeRequired(false);          // 海拔信息是否关注
        criteria.setBearingRequired(false); // 对周围的事物是否关心
        criteria.setCostAllowed(true);  // 是否支持收费查询
        criteria.setPowerRequirement(Criteria.POWER_LOW); // 是否耗电
        criteria.setSpeedRequired(false); // 对速度是否关注

        // 获取最好的定位方式
        String provider = locationManager.getBestProvider(criteria, true); // true 代表从打开的设备中查找

        // 注册监听
        /**
         * provider:定位方式
         * minTime:定位时间   最少不能小于60000ms  (定位需要时间)
         * minDistance:最小距离位置更新  0代表不更新   按定位时间更新
         *
         */
        SharedPreferences preferences =context.getSharedPreferences("ip", Context.MODE_PRIVATE);
        String gps=preferences.getString("gps","50");
        int distance=Integer.parseInt(gps);
        loginName = SharedPreferencesHelper.getInstance().getUserName(context);
        isPatrol=SharedPreferencesHelper.getInstance().getPatrolFlag(context);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //gps已打开
            Log.i("info","GPS开始定位");
            locationManager.requestLocationUpdates(provider, 1000, distance, getListener());
        } else {
            toggleGPS();
        }

    }
    private void toggleGPS() {
        Intent gpsIntent = new Intent();
        gpsIntent.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
        gpsIntent.addCategory("android.intent.category.ALTERNATIVE");
        gpsIntent.setData(Uri.parse("custom:3"));
        try {
            PendingIntent.getBroadcast(context, 0, gpsIntent, 0).send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }

    }



    /*****通过单例来得到 定位的监听器*****************/
    private MyLocationListener getListener(){
        if(listener == null){
            listener = new MyLocationListener();
        }
        return listener;
    }


    private final class MyLocationListener implements LocationListener{

        @Override
        /*************当手机发生位置改变***************/
        public void onLocationChanged(Location location) {
            if (location != null) {
                String longitude=String.valueOf(location.getLongitude());
                String latitude=String.valueOf(location.getLatitude());
               // Toast.makeText(context,"GPSInfoService重新定位longitude  "+longitude+" latitude "+latitude+"  是否在巡逻中  "+isPatrol, Toast.LENGTH_SHORT).show();
                Log.i("info","GPSInfoService重新定位longitude  "+longitude+" latitude "+latitude+"  是否在巡逻中  "+isPatrol);
                SharedPreferencesHelper.getInstance().saveLocation(context,longitude,latitude);
                MyServerModel.getInstance(context).saveGPS(loginName,longitude+","+latitude,isPatrol,patrolNumber,context);
            }
        }

        @Override
        /**********当手机的某个位置提供者的状态发生改变的时候调用***************/
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        /*******某个位置的提供者可用了************/
        public void onProviderEnabled(String provider) {

        }

        @Override
        /*******某个位置的提供者被禁用掉******************/
        public void onProviderDisabled(String provider) {

        }
    }

    /**********取消监听********************/
    public void unRegisterLocationChangeListener(){
        locationManager.removeUpdates(getListener());
    }
}