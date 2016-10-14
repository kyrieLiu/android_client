package com.chinasoft.ctams.fragment.mediafragment.videoUpdate;

import android.content.Intent;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.chinasoft.ctams.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by Administrator on 2016/6/24.
 */
public class UpDateVideoActivity extends AppCompatActivity {
    SurfaceView sView;
    SurfaceHolder surfaceHolder;
    int screenWidth, screenHeight;
    Camera camera;                    // 定义系统所用的照相机
    boolean isPreview = false;        //是否在浏览中
    private String ipname;
    private StreamIt streamIt;
    ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor_video_play);

        // 获取IP地址
        Intent intent = getIntent();
        Bundle data = intent.getExtras();
        ipname = data.getString("ipname");
        Log.i("tag","ipname"+ipname);
        screenWidth = 640;
        screenHeight = 480;
        sView = (SurfaceView) findViewById(R.id.sView);                  // 获取界面中SurfaceView组件
        surfaceHolder = sView.getHolder();                               // 获得SurfaceView的SurfaceHolder

        // 为surfaceHolder添加一个回调监听器
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width,int height) {
            }
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                initCamera();                                            // 打开摄像头
            }
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                // 如果camera不为null ,释放摄像头
                if (camera != null) {
                    if (isPreview)
                        camera.stopPreview();
                    camera.release();
                    camera = null;
                }
                finish();
            }
        });
        // 设置该SurfaceView自己不维护缓冲
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

    }



    @Override
    public void onBackPressed() {
        camera.setPreviewCallback(null);
        camera.stopPreview();
        camera.release();
        camera=null;
        finish();
    }


    private void initCamera() {
        if (!isPreview) {
            camera = Camera.open();
        }
        if (camera != null && !isPreview) {
            try{
                Camera.Parameters parameters = camera.getParameters();
                parameters.setPreviewSize(screenWidth, screenHeight);    // 设置预览照片的大小
                parameters.setPreviewFpsRange(20,30);                    // 每秒显示20~30帧
                parameters.setPictureFormat(ImageFormat.NV21);           // 设置图片格式
                parameters.setPictureSize(screenWidth, screenHeight);    // 设置照片的大小
                //camera.setParameters(parameters);                      // android2.3.3以后不需要此行代码
                camera.setPreviewDisplay(surfaceHolder);                 // 通过SurfaceView显示取景画面
                streamIt=new StreamIt(ipname);
                camera.setPreviewCallback(streamIt);         // 设置回调的类
                camera.startPreview();                                   // 开始预览
                camera.autoFocus(null);                                  // 自动对焦
            } catch (Exception e) {
                e.printStackTrace();
            }
            isPreview = true;
        }
    }
    class StreamIt implements Camera.PreviewCallback {
        private String ipname;
        public StreamIt(String ipname){
            this.ipname = ipname;
        }

        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
            Camera.Size size = camera.getParameters().getPreviewSize();
            try{
                //调用image.compressToJpeg（）将YUV格式图像数据data转为jpg格式
                YuvImage image = new YuvImage(data, ImageFormat.NV21, size.width, size.height, null);
                if(image!=null ){
                    ByteArrayOutputStream outstream = new ByteArrayOutputStream();
                    image.compressToJpeg(new Rect(0, 0, size.width, size.height), 80, outstream);
                    outstream.flush();
                    //启用线程将图像数据发送出去
                    Thread  th = new MyThread(outstream,ipname);
                    th.start();
                    Thread.sleep(700);
                }
            }catch(Exception ex){
                Log.e("Sys","Error:"+ex.getMessage());
            }
        }
    }

    class MyThread extends Thread {
        private byte byteBuffer[] = new byte[1024];

        private ByteArrayOutputStream myoutputstream;
        private String ipname;

        public MyThread(ByteArrayOutputStream myoutputstream, String ipname) {
            this.myoutputstream = myoutputstream;
            this.ipname = ipname;

            try {
                myoutputstream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {
            try {
                //将图像数据通过Socket发送出去
                Socket  tempSocket = new Socket(ipname, 5999);
                OutputStream outsocket= tempSocket.getOutputStream();
                ByteArrayInputStream inputstream = new ByteArrayInputStream(myoutputstream.toByteArray());
                int amount;
                while ((amount = inputstream.read(byteBuffer)) != -1) {
                    outsocket.write(byteBuffer, 0, amount);
                }
                myoutputstream.flush();
                myoutputstream.close();
                tempSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

