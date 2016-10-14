package com.chinasoft.ctams.activity.map;

import android.app.Dialog;
import android.content.Context;

import com.chinasoft.ctams.R;
import com.chinasoft.ctams.fragment.mediafragment.monitorPlay.MonitorCameraInfo;
import com.chinasoft.ctams.fragment.mediafragment.monitorPlay.Sjrs08SurfaceView;

/**
 * Created by Administrator on 2016/6/24.
 */
public class MonitorVideoDialog extends Dialog {
    private Context context = null;
    private static MonitorVideoDialog customProgressDialog = null;
    private Sjrs08SurfaceView nowSjrs08SurfaceView;
    private String videoPort;
    private String videoUrl;
    private String username;
    private String password;
    public MonitorVideoDialog(Context context, String videoPort, String videoUrl, String username, String password, String videoName){
        super(context);
        this.context = context;
        setContentView(R.layout.activity_monitor_play);
        setTitle(""+videoName);
        nowSjrs08SurfaceView=(Sjrs08SurfaceView)findViewById(R.id.video_0);
        this.videoPort=videoPort;
        this.videoUrl=videoUrl;
        this.username=username;
        this.password=password;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!nowSjrs08SurfaceView.playFlag) {   //
            MonitorCameraInfo cameraInfo = new MonitorCameraInfo();

            cameraInfo.serverip =videoUrl;
            cameraInfo.serverport = 8000;
            cameraInfo.username = username;
            cameraInfo.userpwd = password;
            cameraInfo.channel = 1;
            cameraInfo.describe = "摄像头1";

            nowSjrs08SurfaceView.setMonitorInfo(cameraInfo);
            nowSjrs08SurfaceView.startPlay();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (nowSjrs08SurfaceView.playFlag) {
            nowSjrs08SurfaceView.stopPlay();   //ֹͣʵʱԤ��
        }
    }
}

