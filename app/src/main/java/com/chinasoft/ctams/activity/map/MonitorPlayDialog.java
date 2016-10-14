package com.chinasoft.ctams.activity.map;


import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;

import com.chinasoft.ctams.R;
import com.chinasoft.ctams.fragment.mediafragment.monitorPlay.MonitorCameraInfo;
import com.chinasoft.ctams.fragment.mediafragment.monitorPlay.Sjrs08SurfaceView;

/**
 * Created by Kyrie on 2016/7/15.
 * Email:kyrie_liu@sina.com
 */
public class MonitorPlayDialog extends Dialog {
    private static MonitorPlayDialog dialog;
   private Sjrs08SurfaceView nowSjrs08SurfaceView;
    private String ipUrl;
    private String videoPort;
    private String username;
    private String password;

    public MonitorPlayDialog(Context context) {
        super(context);
        //nowSjrs08SurfaceView=(Sjrs08SurfaceView)dialog.findViewById(R.id.video_0);
        //playVideo();

    }

    public MonitorPlayDialog(Context context, int themeResId) {
        super(context, themeResId);
    }
    public static MonitorPlayDialog createDialog(Context context){
        dialog=new MonitorPlayDialog(context, R.style.CustomProgressDialog);
        dialog.setContentView(R.layout.customprogressdialog);
        dialog.getWindow().getAttributes().gravity= Gravity.CENTER;
        return  dialog;
    }
    private void playVideo(){
        if (!nowSjrs08SurfaceView.playFlag) {   //
            MonitorCameraInfo cameraInfo = new MonitorCameraInfo();


            cameraInfo.serverip =ipUrl;
            cameraInfo.serverport = 8000;
            cameraInfo.username = username;
            cameraInfo.userpwd = password;
            cameraInfo.channel = 1;
            cameraInfo.describe = "摄像头1";

            nowSjrs08SurfaceView.setMonitorInfo(cameraInfo);
            nowSjrs08SurfaceView.startPlay();
        }

    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        if (nowSjrs08SurfaceView.playFlag) {
//            nowSjrs08SurfaceView.stopPlay();
//        }
//    }
}
