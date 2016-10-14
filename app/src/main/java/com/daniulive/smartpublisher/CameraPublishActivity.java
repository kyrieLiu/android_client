/*
 * CameraPublishActivity.java
 * CameraPublishActivity
 * 
 * Github: https://github.com/daniulive/SmarterStreaming
 * 
 * Created by DaniuLive on 2015/09/20.
 * Copyright © 2014~2016 DaniuLive. All rights reserved.
 */

package com.daniulive.smartpublisher;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.ImageFormat;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chinasoft.ctams.R;
import com.chinasoft.ctams.base.BaseActivity;
import com.chinasoft.ctams.model.MyServerModel;
import com.chinasoft.ctams.util.SharedPreferencesHelper;
import com.eventhandle.SmartEventCallback;
import com.voiceengine.NTAudioRecord;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;


@SuppressWarnings("deprecation")
public class CameraPublishActivity extends BaseActivity implements Callback, PreviewCallback, View.OnClickListener {
    private static String TAG = "SmartPublisher";

    NTAudioRecord audioRecord_ = null;    //for audio capture
    @Bind(R.id.surface)
    SurfaceView mSurfaceView;
    @Bind(R.id.iv_camera_switch)
    ImageView imgSwitchCamera;
    @Bind(R.id.txtCurURL)
    TextView textCurURL;
    @Bind(R.id.iv_camera_resolution)
    ImageView iv_resolution;
    @Bind(R.id.iv_camera_return)
    ImageView iv_back;
    @Bind(R.id.tv_camera_title)
    TextView tv_title;
    private PopupWindow popupWindow;
    @Bind(R.id.rl_camera_main)
    RelativeLayout relativeLayout;
    @Bind(R.id.iv_start_stop)
    ImageView bt_start_stop;
    @Bind(R.id.tv_camera_flag)
    TextView tv_flag;


    private SmartPublisherJni libPublisher = null;

    private SurfaceHolder mSurfaceHolder = null;

    private Camera mCamera = null;
    private AutoFocusCallback myAutoFocusCallback = null;

    private boolean mPreviewRunning = false;

    private boolean isStart = false;

    final private String logoPath = "/sdcard/daniulivelogo.png";
    private boolean isWritelogoFileSuccess = false;

    private String publishURL;
    //final private String baseURL = "rtmp://192.168.1.186:1935/hls/stream";

     private String baseURL;

    private String printText = "URL:";
    private String txt = "当前状态";

    private static final int FRONT = 1;        //前置摄像头标记
    private static final int BACK = 2;        //后置摄像头标记
    private int currentCameraType = BACK;    //当前打开的摄像头标记
    private static final int PORTRAIT = 1;    //竖屏
    private static final int LANDSCAPE = 2;    //横屏
    private int currentOrigentation = PORTRAIT;
    private int curCameraIndex = -1;

    private int videoWidth = 320;
    private int videoHight = 240;

    private int frameCount = 0;

    private String recDir = "/sdcard/daniulive/rec";    //for recorder path

    private boolean is_need_local_recorder = false;        // do not enable recorder in default

    private Context myContext;


    static {
        System.load("libSmartPublisher.so");
    }

    private String username;


    @Override
    public int getLayoutId() {
        return R.layout.activity_smart_main;
    }

    @Override
    public void initPage(Bundle savedInstanceState) {
        myContext = this.getApplicationContext();

        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        SharedPreferences preferences=this.getSharedPreferences("ip",Context.MODE_PRIVATE);
         String ip=  preferences.getString("ipPush","");
        baseURL= "rtmp://"+ip+":1935/hls/stream";

        mSurfaceView.getHolder().setKeepScreenOn(true); // 保持屏幕高亮

        //自动聚焦变量回调
        myAutoFocusCallback = new AutoFocusCallback() {

            public void onAutoFocus(boolean success, Camera camera) {
                if (success)//success表示对焦成功
                {
                    Log.i(TAG, "onAutoFocus succeed...");
                } else {
                    Log.i(TAG, "onAutoFocus failed...");
                }
            }
        };
        libPublisher = new SmartPublisherJni();
        initPopupWindow();//初始化调节屏幕分辨率的窗口
        initData();//初始化屏幕分辨率
        Toast.makeText(this, "点击播放按钮开始上传", Toast.LENGTH_LONG).show();
        tv_title.setText("实时视频");


    }

    private void initData() {
        Map<String, Integer> map = SharedPreferencesHelper.getInstance().getResolution(this);
        videoWidth = map.get("width");
        videoHight = map.get("height");
    }

    void CheckInitAudioRecorder() {
        if (audioRecord_ == null) {
            audioRecord_ = new NTAudioRecord(this, 1);
        }

        if (audioRecord_ != null) {
            Log.i(TAG, "onCreate, call executeAudioRecordMethod..");
            audioRecord_.executeAudioRecordMethod();
        }
    }

    //Configure recorder related function.
    void ConfigRecorderFuntion() {
        if (libPublisher != null) {
            if (is_need_local_recorder) {
                if (recDir != null && !recDir.isEmpty()) {
                    int ret = libPublisher.SmartPublisherCreateFileDirectory(recDir);
                    if (0 == ret) {
                        if (0 != libPublisher.SmartPublisherSetRecorderDirectory(recDir)) {
                            return;
                        }

                        if (0 != libPublisher.SmartPublisherSetRecorder(1)) {
                            return;
                        }

                        if (0 != libPublisher.SmartPublisherSetRecorderFileMaxSize(200)) {
                            return;
                        }

                    } else {
                    }
                }
            } else {
                if (0 != libPublisher.SmartPublisherSetRecorder(0)) {
                    return;
                }
            }
        }
    }

    @OnClick({R.id.iv_camera_resolution, R.id.iv_start_stop, R.id.iv_camera_switch,R.id.iv_camera_return})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_camera_resolution:
                int[] location = new int[2];
                relativeLayout.getLocationInWindow(location);
                int x = location[0];
                int y = location[1];
                popupWindow.showAtLocation(relativeLayout, Gravity.RIGHT | Gravity.TOP, 0, y);
                break;
            case R.id.iv_start_stop:
                startStreaming();//开始推流

                break;
            case R.id.iv_camera_switch:
                try {
                    switchCamera();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.tv_popup_item1:
                switchResolution(640, 480);
                break;
            case R.id.tv_popup_item2:
                switchResolution(320, 240);
                break;
            case R.id.tv_popup_item3:
                switchResolution(176, 144);
                break;
            case R.id.iv_camera_return:
                finish();
                break;

        }
    }

    /**
     * 切换摄像头分辨率
     *
     * @param width
     * @param height
     */
    private void switchResolution(int width, int height) {
        videoWidth = width;
        videoHight = height;
        popupWindow.dismiss();
        mCamera.stopPreview();
        if (audioRecord_ != null) {
            audioRecord_.StopRecording();
            audioRecord_ = null;
        }
        if (libPublisher != null) {
            libPublisher.SmartPublisherStop();
        }
        //重新推流
        initCamera(mSurfaceHolder);
        startStreaming();
    }



    class EventHande implements SmartEventCallback {
        @Override
        public void onCallback(int code, long param1, long param2, String param3, String param4, Object param5) {
            switch (code) {
                case EVENTID.EVENT_DANIULIVE_ERC_PUBLISHER_STARTED:
                    txt = "开始。。";
                    break;
                case EVENTID.EVENT_DANIULIVE_ERC_PUBLISHER_CONNECTING:
                    txt = "连接中。。";
                    break;
                case EVENTID.EVENT_DANIULIVE_ERC_PUBLISHER_CONNECTION_FAILED:
                    txt = "连接失败。。";
                    break;
                case EVENTID.EVENT_DANIULIVE_ERC_PUBLISHER_CONNECTED:
                    txt = "连接成功。。";
                    break;
                case EVENTID.EVENT_DANIULIVE_ERC_PUBLISHER_DISCONNECTED:
                    txt = "连接断开。。";
                    break;
                case EVENTID.EVENT_DANIULIVE_ERC_PUBLISHER_STOP:
                    txt = "关闭。。";
                    break;
                case EVENTID.EVENT_DANIULIVE_ERC_PUBLISHER_RECORDER_START_NEW_FILE:
                    Log.i(TAG, "开始一个新的录像文件 : " + param3);
                    txt = "开始一个新的录像文件。。";
                    break;
                case EVENTID.EVENT_DANIULIVE_ERC_PUBLISHER_ONE_RECORDER_FILE_FINISHED:
                    Log.i(TAG, "已生成一个录像文件 : " + param3);
                    txt = "已生成一个录像文件。。";
                    break;
            }

            String str = "当前回调状态：" + txt;

            Log.i(TAG, str);

        }
    }

    /**
     * 开始推流
     */
    private void startStreaming() {
        if (isStart) {
            stop();
            tv_flag.setText("");
            bt_start_stop.setImageResource(R.mipmap.btn_play);
            return;
        }
        isStart = true;
        tv_flag.setText("推流中...");
        bt_start_stop.setImageResource(R.mipmap.btn_pause);

        if (libPublisher != null) {
            username = SharedPreferencesHelper.getInstance().getUserName(this);
            publishURL = baseURL + username;
            MyServerModel.getInstance(this).updateVideoState(this, username, publishURL, "online");

            printText = "URL:" + publishURL;

            textCurURL.setText(printText);

            ConfigRecorderFuntion();


            int audio_opt = 1;
            int video_opt = 1;


            libPublisher.SmartPublisherInit(myContext, audio_opt, video_opt, videoWidth, videoHight);
            libPublisher.SetSmartPublisherEventCallback(new EventHande());

            String path = logoPath;

            if (isWritelogoFileSuccess) {
                libPublisher.SmartPublisherSetPictureWatermark(path, SmartPublisherJni.WATERMARK.WATERMARK_POSITION_TOPRIGHT, 160, 160, 10, 10);
            }

            // IF not set url or url is empty, it will not publish stream
            // if ( libPublisher.SmartPublisherSetURL("") != 0 )
            if (libPublisher.SmartPublisherSetURL(publishURL) != 0) {
                Log.e(TAG, "Failed to set publish stream URL..");
            }

            int isStarted = libPublisher.SmartPublisherStart();
            if (isStarted != 0) {
            }
        }
        CheckInitAudioRecorder();    //enable pure video publisher..
    }

    ;

    private void stop() {
        StopPublish();
        isStart = false;
    }


    private void SetCameraFPS(Parameters parameters) {
        if (parameters == null)
            return;

        int[] findRange = null;

        int defFPS = 20 * 1000;

        List<int[]> fpsList = parameters.getSupportedPreviewFpsRange();
        if (fpsList != null && fpsList.size() > 0) {
            for (int i = 0; i < fpsList.size(); ++i) {
                int[] range = fpsList.get(i);
                if (range != null
                        && Parameters.PREVIEW_FPS_MIN_INDEX < range.length
                        && Parameters.PREVIEW_FPS_MAX_INDEX < range.length) {

                    if (findRange == null) {
                        if (defFPS <= range[Parameters.PREVIEW_FPS_MAX_INDEX]) {
                            findRange = range;
                        }
                    }
                }
            }
        }

        if (findRange != null) {
            parameters.setPreviewFpsRange(findRange[Parameters.PREVIEW_FPS_MIN_INDEX], findRange[Parameters.PREVIEW_FPS_MAX_INDEX]);
        }

    }

    private void initCamera(SurfaceHolder holder) {

        if (mPreviewRunning)
            mCamera.stopPreview();

        Parameters parameters;
        try {
            parameters = mCamera.getParameters();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        Log.i(TAG, "initCamera: videoWidth"+videoWidth+"  videoHeight  "+videoHight);
        parameters.setPreviewSize(videoWidth, videoHight);
        parameters.setPictureFormat(PixelFormat.JPEG);
        parameters.setPreviewFormat(PixelFormat.YCbCr_420_SP);

        SetCameraFPS(parameters);

        setCameraDisplayOrientation(this, curCameraIndex, mCamera);

        mCamera.setParameters(parameters);

        int bufferSize = (((videoWidth | 0xf) + 1) * videoHight * ImageFormat.getBitsPerPixel(parameters.getPreviewFormat())) / 8;

        mCamera.addCallbackBuffer(new byte[bufferSize]);

        mCamera.setPreviewCallbackWithBuffer(this);
        try {
            mCamera.setPreviewDisplay(holder);
        } catch (Exception ex) {
            if (null != mCamera) {
                mCamera.release();
                mCamera = null;
            }
            ex.printStackTrace();
        }
        mCamera.startPreview();
        mCamera.autoFocus(myAutoFocusCallback);
        mPreviewRunning = true;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {

            int CammeraIndex = findBackCamera();

            if (CammeraIndex == -1) {
                CammeraIndex = findFrontCamera();
                currentCameraType = FRONT;
                imgSwitchCamera.setEnabled(false);
                if (CammeraIndex == -1) {
                    return;
                }
            } else {
                currentCameraType = BACK;
            }

            if (mCamera == null) {
                mCamera = openCamera(currentCameraType);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        initCamera(holder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    public void onConfigurationChanged(Configuration newConfig) {
        try {
            super.onConfigurationChanged(newConfig);
            if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                if (!isStart) {
                    currentOrigentation = LANDSCAPE;
                }
            } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                if (!isStart) {
                    currentOrigentation = PORTRAIT;
                }
            }
        } catch (Exception ex) {
        }
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        frameCount++;
        if (frameCount % 3000 == 0) {
            System.gc();
        }

        if (data == null) {
            Parameters params = camera.getParameters();
            Size size = params.getPreviewSize();
            int bufferSize = (((size.width | 0x1f) + 1) * size.height * ImageFormat.getBitsPerPixel(params.getPreviewFormat())) / 8;
            camera.addCallbackBuffer(new byte[bufferSize]);
        } else {
            if (isStart) {
                libPublisher.SmartPublisherOnCaptureVideoData(data, data.length, currentCameraType, currentOrigentation);
            }
            camera.addCallbackBuffer(data);
        }
    }

    @SuppressLint("NewApi")
    private Camera openCamera(int type) {
        int frontIndex = -1;
        int backIndex = -1;
        int cameraCount = Camera.getNumberOfCameras();

        CameraInfo info = new CameraInfo();
        for (int cameraIndex = 0; cameraIndex < cameraCount; cameraIndex++) {
            Camera.getCameraInfo(cameraIndex, info);
            if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
                frontIndex = cameraIndex;
            } else if (info.facing == CameraInfo.CAMERA_FACING_BACK) {
                backIndex = cameraIndex;
            }
        }

        currentCameraType = type;
        if (type == FRONT && frontIndex != -1) {
            curCameraIndex = frontIndex;
            return Camera.open(frontIndex);
        } else if (type == BACK && backIndex != -1) {
            curCameraIndex = backIndex;
            return Camera.open(backIndex);
        }
        return null;
    }

    private void switchCamera() throws IOException {
        mCamera.stopPreview();
        mCamera.release();
        if (currentCameraType == FRONT) {
            mCamera = openCamera(BACK);
        } else if (currentCameraType == BACK) {
            mCamera = openCamera(FRONT);
        }
        initCamera(mSurfaceHolder);

    }

    private void StopPublish() {
        if (audioRecord_ != null) {
            audioRecord_.StopRecording();
            audioRecord_ = null;
        }

        if (libPublisher != null) {
            libPublisher.SmartPublisherStop();
        }
        MyServerModel.getInstance(this).updateVideoState(this, username, publishURL, "offline");
    }

    //Check if it has front camera
    private int findFrontCamera() {
        int cameraCount = 0;
        CameraInfo cameraInfo = new CameraInfo();
        cameraCount = Camera.getNumberOfCameras();

        for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
            Camera.getCameraInfo(camIdx, cameraInfo);
            if (cameraInfo.facing == CameraInfo.CAMERA_FACING_FRONT) {
                return camIdx;
            }
        }
        return -1;
    }

    //Check if it has back camera
    private int findBackCamera() {
        int cameraCount = 0;
        CameraInfo cameraInfo = new CameraInfo();
        cameraCount = Camera.getNumberOfCameras();

        for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
            Camera.getCameraInfo(camIdx, cameraInfo);
            if (cameraInfo.facing == CameraInfo.CAMERA_FACING_BACK) {
                return camIdx;
            }
        }
        return -1;
    }

    private void setCameraDisplayOrientation(Activity activity, int cameraId, Camera camera) {
        CameraInfo info = new CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        int result;
        if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;
        } else {
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }

    /**
     * 初始化分辨率选择窗口
     */
    private void initPopupWindow() {
        View popupView = LayoutInflater.from(CameraPublishActivity.this).inflate(R.layout.popup_view, null);
        popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.bg_black));
        TextView tv_high = (TextView) popupView.findViewById(R.id.tv_popup_item1);
        TextView tv_middle = (TextView) popupView.findViewById(R.id.tv_popup_item2);
        TextView tv_low = (TextView) popupView.findViewById(R.id.tv_popup_item3);
        tv_low.setVisibility(View.VISIBLE);
        tv_high.setText("高分辨率");
        tv_middle.setText("中分辨率");
        tv_low.setText("低分辨率");
        tv_high.setOnClickListener(this);
        tv_middle.setOnClickListener(this);
        tv_low.setOnClickListener(this);
        LinearLayout linearLayout = (LinearLayout) popupView.findViewById(R.id.ll_popup);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
        params.height = 600;
        linearLayout.setLayoutParams(params);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isStart) {
            isStart = false;
            StopPublish();
        }

    }
}