package com.chinasoft.ctams.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/6/23.
 */
public class SharedPreferencesHelper {
   public static SharedPreferencesHelper sharedPerferencesHelper=new SharedPreferencesHelper();
    private SharedPreferencesHelper() {
    }
    public static SharedPreferencesHelper getInstance(){

        return sharedPerferencesHelper;
    }
    private SharedPreferences personShareprefernce;//个人信息标识
    private SharedPreferences locationPerferences;//GPS定位坐标
    private SharedPreferences patrolPreferences;//巡逻状态标识
    private SharedPreferences ipPreferences;//服务器ip
    private SharedPreferences loginPreferences;//登录标识
    private SharedPreferences pushTimePreferences;//推送时间标识
    private SharedPreferences resolutionPreference;//实时视频分辨率

    /**
     *保存用户信息
     */
    public void saveUserInfo(Context context,String username,String password,String peopleId,String peopleName,String organization,String organizationId){
        personShareprefernce =context.getSharedPreferences("userinfo",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= personShareprefernce.edit();
        editor.putString("username",username);
        editor.putString("userpassword",password);
        editor.putString("peopleId",peopleId);
        editor.putString("peopleName",peopleName);
        editor.putString("peopleOrganization",organization);
        editor.putString("organizationId",organizationId);
        editor.commit();
    }
    public String getUserName(Context context){
        if (personShareprefernce ==null){
            personShareprefernce =context.getSharedPreferences("userinfo",Context.MODE_PRIVATE);
        }
        String username= personShareprefernce.getString("username","");
        return username;
    }
    public String getPassword(Context context){
        if (personShareprefernce ==null){
            personShareprefernce =context.getSharedPreferences("userinfo",Context.MODE_PRIVATE);
        }
        String password= personShareprefernce.getString("userpassword","");
        return password;
    }

    public String getPeopleId(Context context){
        if (personShareprefernce ==null){
            personShareprefernce =context.getSharedPreferences("userinfo",Context.MODE_PRIVATE);
        }
        String peopleId= personShareprefernce.getString("peopleId","");
        return peopleId;
    }

    public String getPeopleName(Context context){
        if (personShareprefernce ==null){
            personShareprefernce =context.getSharedPreferences("userinfo",Context.MODE_PRIVATE);
        }
        String password= personShareprefernce.getString("peopleName","");
        return password;
    }

    public String getOrganization(Context context){
        if (personShareprefernce ==null){
            personShareprefernce =context.getSharedPreferences("userinfo",Context.MODE_PRIVATE);
        }
        String password= personShareprefernce.getString("peopleOrganization","");
        return password;
    }

    public String getOrganizationId(Context context){
        if (personShareprefernce ==null){
            personShareprefernce =context.getSharedPreferences("userinfo",Context.MODE_PRIVATE);
        }
        String password= personShareprefernce.getString("organizationId","");
        return password;
    }

    /**
     * 保存GPS定位的经纬度
     * @param longitude
     * @param latitude
     */
    public void saveLocation(Context context,String longitude,String latitude){
        if (locationPerferences==null){
            locationPerferences=context.getSharedPreferences("location",Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor=locationPerferences.edit();
        editor.putString("longitude",longitude);
        editor.putString("latitude",latitude);
        editor.commit();
    }
    public Map<String,String> getLocation(Context context){
        if (locationPerferences==null){
            locationPerferences=context.getSharedPreferences("location",Context.MODE_PRIVATE);
        }
        String longitude=locationPerferences.getString("longitude","");
        String latitude=locationPerferences.getString("latitude","");
        Map<String,String> map=new HashMap<>();
        map.put("longitude",longitude);
        map.put("latitude",latitude);
        return map;
    }
    /**
     * 存储是否为巡逻状态
     */
    public void savePatrolFlag(boolean flag,Context context){
            if (patrolPreferences==null){
                patrolPreferences=context.getSharedPreferences("patrol",Context.MODE_PRIVATE);
            }
        SharedPreferences.Editor editor=patrolPreferences.edit();
        editor.putBoolean("flag",flag);
        editor.commit();
    }

    /**
     *判断是否在巡逻状态
     */

    public boolean getPatrolFlag(Context context){
        if (patrolPreferences==null){
            patrolPreferences=context.getSharedPreferences("patrol",Context.MODE_PRIVATE);
        }
        boolean flag=patrolPreferences.getBoolean("flag",false);
        return flag;
    }
    /**
     * 获取端口号
     */
    public String getPort(Context context){
        if (ipPreferences==null){
            ipPreferences=context.getSharedPreferences("ip",Context.MODE_PRIVATE);
        }
        String port=ipPreferences.getString("port","8080");
        return port;
    }

    /**
     * 保存和获取是否已经登录过标识
     * @param flag
     */
    public void saveLoginFlag(boolean flag,Context context){
        loginPreferences=context.getSharedPreferences("loginFlag",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=loginPreferences.edit();
        editor.putBoolean("flag",flag);
        editor.commit();
    }

    /**
     * 获取登录标识
     * @param context
     * @return
     */
    public boolean getLoginFlag(Context context){
        if(loginPreferences==null){
            loginPreferences=context.getSharedPreferences("loginFlag",Context.MODE_PRIVATE);
        }
        boolean flag=loginPreferences.getBoolean("flag",false);
        return flag;
    }
    /**
     * 存储任务推送时间
     */
    public void saveTaskPushTime(Context context){
        Date date=new Date();
        if (pushTimePreferences==null){
            pushTimePreferences=context.getSharedPreferences("pushTime",Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor=pushTimePreferences.edit();
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        String time=format.format(date);
        editor.putString("time",time);
        editor.commit();
    }
    /**
     * 获取任务推送时间
     */
    public String getTaskPushTime(Context context){
        if (pushTimePreferences==null){
            pushTimePreferences=context.getSharedPreferences("pushTime",Context.MODE_PRIVATE);
        }
        String time=pushTimePreferences.getString("time","");
        return time;
    }
    /**
     * 存储实时视频分辨率
     */
    public void saveResolution(int width,int height,Context context){
        if (resolutionPreference==null){
            resolutionPreference=context.getSharedPreferences("resolution",Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor=resolutionPreference.edit();
        editor.putInt("width",width);
        editor.putInt("height",height);
        editor.commit();
    }
    /**
     * 获取实时视频分辨率
     */
    public Map<String,Integer> getResolution(Context context){
        if (resolutionPreference==null){
            resolutionPreference=context.getSharedPreferences("resolution",Context.MODE_PRIVATE);
        }
        int width=resolutionPreference.getInt("width",320);
        int height=resolutionPreference.getInt("height",240);
        Map<String,Integer> map=new HashMap<>();
        map.put("width",width);
        map.put("height",height);
        return map;
    }
}
