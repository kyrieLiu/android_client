package com.chinasoft.ctams.fragment.mediafragment.monitorPlay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.chinasoft.ctams.R;

public class MonitorVideoPlayActivity extends Activity {

	Sjrs08SurfaceView nowSjrs08SurfaceView;   //SurfaceView
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_monitor_play);

		nowSjrs08SurfaceView = (Sjrs08SurfaceView) findViewById(R.id.video_0);

	}

	@Override
	protected void onResume() {

		super.onResume();

		if (!nowSjrs08SurfaceView.playFlag) {   //
			MonitorCameraInfo cameraInfo = new MonitorCameraInfo();

			Intent intent=getIntent();
			String ip=intent.getStringExtra("ip");
			String username=intent.getStringExtra("username");
			String password=intent.getStringExtra("password");
			Log.i("info","ip地址"+ip+"username"+username+"password"+password);
			cameraInfo.serverip =ip;
			cameraInfo.serverport = 8000;
			cameraInfo.username = username;
			cameraInfo.userpwd = password;
//			cameraInfo.serverip ="22.144.96.61";
//			cameraInfo.serverport = 8000;
//			cameraInfo.username = "admin";
//			cameraInfo.userpwd = "admin12345";
			cameraInfo.channel = 1;
			cameraInfo.describe = "摄像头1";

			nowSjrs08SurfaceView.setMonitorInfo(cameraInfo);
			nowSjrs08SurfaceView.startPlay();
		}

	}

	@Override
	protected void onPause() {
		super.onPause();
		if (nowSjrs08SurfaceView.playFlag) {
			nowSjrs08SurfaceView.stopPlay();   //
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (nowSjrs08SurfaceView.playFlag) {
			nowSjrs08SurfaceView.stopPlay();   //
		}
	}
}