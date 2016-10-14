package com.chinasoft.ctams.activity.patrol.video;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinasoft.ctams.R;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TranscribeVideoActivity extends Activity {
    @Bind(R.id.camera_preview)
    SurfaceView mCameraPreview;
    @Bind(R.id.timestamp_minute_prefix)
    TextView mMinutePrefix;
    @Bind(R.id.timestamp_minute_text)
    TextView mMinuteText;
    @Bind(R.id.timestamp_second_prefix)
    TextView mSecondPrefix;
    @Bind(R.id.timestamp_second_text)
    TextView mSecondText;
    @Bind(R.id.record_shutter)
    ImageButton iv_start_stop;
    @Bind(R.id.ll_video_controller)
     LinearLayout ll_controller;
    @Bind(R.id.iv_video_return)
    ImageView iv_return;


    private SurfaceHolder mSurfaceHolder;


    private Camera mCamera;
    private MediaRecorder mRecorder;

    private final static int CAMERA_ID = 0;

    private boolean mIsRecording = false;
    private boolean mIsSufaceCreated = false;

    private static final String TAG = "Jackie";

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transcribe);
        ButterKnife.bind(this);
        initPage();
    }


    public void initPage() {

        mSurfaceHolder = mCameraPreview.getHolder();
        mSurfaceHolder.addCallback(mSurfaceCallback);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

    }

    @OnClick({R.id.record_shutter,R.id.iv_transcribe_cancel,R.id.iv_transcribe_confirm,R.id.iv_video_return})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.record_shutter:
                if (mIsRecording) {
                    stopRecording();
                    ll_controller.setVisibility(View.VISIBLE);
                    iv_start_stop.setVisibility(View.GONE);
                } else {
                    initMediaRecorder();
                    startRecording();
                    //开始录像后,每隔1秒去更新录像的时间戳
                    mHandler.postDelayed(mTimestampRunnable, 1000);
                }
                break;
            case R.id.iv_transcribe_cancel:
                finish();
                break;
            case R.id.iv_transcribe_confirm:
                Intent intent_confirm=new Intent();
                setResult(6,intent_confirm);
                finish();
                break;
            case R.id.iv_video_return:
                finish();
                break;

        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (mIsRecording) {
            stopRecording();
        }
        stopPreview();
    }

    private SurfaceHolder.Callback mSurfaceCallback = new SurfaceHolder.Callback() {

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            mIsSufaceCreated = false;
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            mIsSufaceCreated = true;
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            startPreview();
        }
    };

    //启动预览
    private void startPreview() {
        //保证只有一个Camera对象
        if (mCamera != null || !mIsSufaceCreated) {
            return;
        }

        mCamera = Camera.open(CAMERA_ID);

        Parameters parameters = mCamera.getParameters();
        Size size = getBestPreviewSize(640, 480, parameters);
        if (size != null) {
            parameters.setPreviewSize(size.width, size.height);
        }

        parameters.setFocusMode(Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
        parameters.setPreviewFrameRate(20);

        //设置相机预览方向
        mCamera.setDisplayOrientation(0);

        mCamera.setParameters(parameters);

        try {
            mCamera.setPreviewDisplay(mSurfaceHolder);
//         mCamera.setPreviewCallback(new Camera.PreviewCallback() {
//             @Override
//             public void onPreviewFrame(byte[] data, Camera camera) {
//
//             }
//         });
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }

        mCamera.startPreview();
    }

    private void stopPreview() {
        //释放cameara对象
        if (mCamera != null) {
            try {
                mCamera.setPreviewDisplay(null);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }

            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    private Size getBestPreviewSize(int width, int height, Parameters parameters) {
        Size result = null;

        for (Size size : parameters.getSupportedPreviewSizes()) {
            if (size.width <= width && size.height <= height) {
                if (result == null) {
                    result = size;
                } else {
                    int resultArea = result.width * result.height;
                    int newArea = size.width * size.height;

                    if (newArea > resultArea) {
                        result = size;
                    }
                }
            }
        }

        return result;
    }


    private void initMediaRecorder() {
        mRecorder = new MediaRecorder();//实例化
        mCamera.unlock();
        //给Recoder设置cameara对象,保证录像跟预览的方向保持一致
        mRecorder.setCamera(mCamera);
        mRecorder.setOrientationHint(0);

        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC); // 设置从麦克风采集的声音
        mRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA); // 设置从摄像头采集图像
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);  // 设置视频的输出格式为MP4
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT); // 设置音频的编码格式
        mRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264); // 设置视频的 编码格式
        //mRecorder.setVideoSize(176, 144);  // 设置视频大小
        mRecorder.setVideoSize(640, 480);
        mRecorder.setVideoFrameRate(20); // 这只帧率
//        mRecorder.setMaxDuration(10000); //设置最大录像时间
        mRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());

        //设置视频的 存储路径
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES) + File.separator + "反恐维稳");
        if (!file.exists()) {
            //多级文件夹的创建
            file.mkdirs();
        }
        mRecorder.setOutputFile(file.getPath() + File.separator + "ctams.mp4");
    }

    private void startRecording() {
        if (mRecorder != null) {
            try {
                mRecorder.prepare();
                mRecorder.start();
            } catch (Exception e) {
                mIsRecording = false;
            }
        }

        iv_start_stop.setImageDrawable(getResources().getDrawable(R.mipmap.video_stop));
        mIsRecording = true;
    }

    private void stopRecording() {
        if (mCamera != null) {
            mCamera.lock();
        }

        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        }
        iv_start_stop.setVisibility(View.GONE);
        ll_controller.setVisibility(View.VISIBLE);
        mIsRecording = false;

        mHandler.removeCallbacks(mTimestampRunnable);


        //重启预览
        startPreview();
    }

    private Runnable mTimestampRunnable = new Runnable() {
        @Override
        public void run() {
            updateTimestamp();
            mHandler.postDelayed(this, 1000);
        }
    };

    private void updateTimestamp() {
        int second = Integer.parseInt(mSecondText.getText().toString());
        int minute = Integer.parseInt(mMinuteText.getText().toString());
        second++;
        Log.d(TAG, "second: " + second);

        if (second < 10) {
            mSecondText.setText(String.valueOf(second));
        } else if (second >= 10 && second < 60) {
            mSecondPrefix.setVisibility(View.GONE);
            mSecondText.setText(String.valueOf(second));
        } else if (second >= 60) {
            mSecondPrefix.setVisibility(View.VISIBLE);
            mSecondText.setText("0");

            minute++;
            mMinuteText.setText(String.valueOf(minute));
        } else if (minute >= 60) {
            mMinutePrefix.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
