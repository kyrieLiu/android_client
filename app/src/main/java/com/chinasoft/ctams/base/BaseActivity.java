package com.chinasoft.ctams.base;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.chinasoft.ctams.application.MyApplication;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/6/16.
 */
public abstract class BaseActivity extends FragmentActivity implements UIOperation {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        ((MyApplication)getApplication()).getActivities().add(this);
        initPage(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(getRequestedOrientation()!= ActivityInfo.SCREEN_ORIENTATION_PORTRAIT){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        ((MyApplication)getApplication()).getActivities().remove(this);
    }
}
