package com.chinasoft.ctams.application;

import android.app.Activity;

import org.acra.annotation.ReportsCrashes;
import org.litepal.LitePalApplication;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Kyrie on 2016/6/21.
 * Email:kyrie_liu@sina.com
 */
public class MyApplication extends LitePalApplication {






    @Override
    public void onCreate() {

        super.onCreate();

    }

    private List<Activity> activities = new ArrayList<Activity>();
    public List<Activity> getActivities(){
        return activities;
    }

}
