package com.chinasoft.ctams.activity.main;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.chinasoft.ctams.R;
import com.chinasoft.ctams.base.BaseActivity;
import com.chinasoft.ctams.model.MyServerModel;
import com.chinasoft.ctams.util.ConstantCode;

import java.io.File;
import java.util.HashMap;

import butterknife.Bind;

public class CheckVersionActivity extends BaseActivity {

    @Bind(R.id.tv_version_number)
    TextView tv_version;

    private int progress=0;
    private ProgressDialog downloadProgressDialog;
    private String url;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case ConstantCode.POST_FAILED:
                    Toast.makeText(CheckVersionActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
                    break;
                case ConstantCode.POST_SUCCESS:
                    HashMap<String,String> map= (HashMap<String, String>) msg.obj;
                    String version=map.get("version");
                    url = map.get("appUrl");
                    showVersion(version);
                    break;
            }
        }
    };

    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case ConstantCode.GET_DOWNLOAD:
                    progress=msg.arg1;
                    downloadProgressDialog.setProgress(progress);
                    break;
                case ConstantCode.POST_SUCCESS:
                    downloadProgressDialog.setTitle("下载完成");
                    downloadProgressDialog.setMessage("");
                    CheckVersionActivity.this.finish();
                    downloadProgressDialog.dismiss();
                    File file= (File) msg.obj;
                    installAPK(file);
                    break;
                case ConstantCode.POST_FAILED:
                    Toast.makeText(CheckVersionActivity.this,"文件下载失败,请检查网络!",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };



    @Override
    public int getLayoutId() {
        return R.layout.activity_check_version;
    }

    @Override
    public void initPage(Bundle savedInstanceState) {
        MyServerModel.getInstance(this).getPackageVersion(handler,this);

    }
    private void showVersion(String version){
        try {
            PackageInfo info=this.getPackageManager().getPackageInfo(this.getPackageName(),0);
            String appVersion=info.versionName;
            tv_version.setText("当前版本号 "+appVersion);
            Log.i("info","获取的version=="+version+"  appVersion==  "+appVersion);
            if (!version.equals(appVersion)){
                    showAlertDialog();
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
    /**
     * 初始化版本更新对话框
     */
    private void showAlertDialog(){
        AlertDialog dialog = new AlertDialog.Builder(CheckVersionActivity.this)
                .setTitle("软件升级")
                .setMessage("messsage")
                .setCancelable(false)
                .setPositiveButton("现在下载",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                                MyServerModel.getInstance(CheckVersionActivity.this).downLoadAPK(mHandler,url,CheckVersionActivity.this);
                                showDownLoadDialog();

                            }
                        })
                .setNegativeButton("以后再说",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                            }
                        }).create();
        dialog.show();
    }
    /**
     * 弹出下载框
     */
    private void showDownLoadDialog() {

        downloadProgressDialog = new ProgressDialog(this);
        downloadProgressDialog.setIcon(R.mipmap.ic_launcher);
        downloadProgressDialog.setTitle("正在下载...");
        downloadProgressDialog.setMessage("请稍后");
        downloadProgressDialog.setMax(100);
        downloadProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        downloadProgressDialog.setButton("关闭", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mHandler.removeMessages(ConstantCode.GET_DOWNLOAD);
                progress=0;
                downloadProgressDialog.setProgress(progress);
                MyServerModel.getInstance(CheckVersionActivity.this).cancelTag(CheckVersionActivity.this);

            }
        });
        downloadProgressDialog.show();

        progress=(progress>0)?progress:0;
        downloadProgressDialog.setProgress(progress);
    }
    /**
     * 安装APK
     */
    private void installAPK(File file){
        if (!file.exists()) {
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        // 安装，如果签名不一致，可能出现程序未安装提示
        i.setDataAndType(Uri.parse("file://" + file.toString()),
                "application/vnd.android.package-archive");
        this.startActivity(i);
        openApk(file);
    }
    private void openApk(File file) {
        PackageManager manager = this.getPackageManager();

        // 这里的是你下载好的文件路径
        Log.i("info","file文件是"+file.getAbsolutePath());
        PackageInfo info = manager.getPackageArchiveInfo(file.getAbsolutePath(), PackageManager.GET_ACTIVITIES);
        if (info != null) {
            Intent intent = manager.getLaunchIntentForPackage(info.applicationInfo.packageName);
            this.startActivity(intent);
        }
    }
}
