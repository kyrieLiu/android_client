package com.chinasoft.ctams.activity.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.chinasoft.ctams.R;
import com.chinasoft.ctams.base.BaseActivity;
import com.chinasoft.ctams.okhttp.CacheType;
import com.chinasoft.ctams.okhttp.Callback;
import com.chinasoft.ctams.okhttp.OKHttpUtils;

import java.io.IOException;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SplashActivity extends BaseActivity {

    @Bind(R.id.et_flash_ip)
    EditText et_ip;
    @Bind(R.id.bt_confirm_ip)
    Button bt_confirm;
    @Bind(R.id.et_push_ip)
    EditText etPushIp;
    @Bind(R.id.et_gps)
    EditText et_gps;
    @Bind(R.id.et_server_port)
    EditText et_port;




    @OnClick(R.id.bt_confirm_ip)
    public void onClick() {
        String ip = et_ip.getText().toString();
        String pushIp=etPushIp.getText().toString();
        String gps=et_gps.getText().toString();
        String port=et_port.getText().toString();
        SharedPreferences preferences = getSharedPreferences("ip", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        if (!ip.equals("")) {
            editor.putString("ipNumber", ip);
            editor.putString("ipPush",pushIp);
            editor.putString("gps",gps);
            editor.putString("port",port);
            editor.commit();
        }
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
        finish();

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void initPage(Bundle savedInstanceState) {
        testHttp();
        SharedPreferences preferences=getSharedPreferences("ip", Context.MODE_PRIVATE);
        String ip=preferences.getString("ipNumber","172.23.247.51");
        String ipPush=preferences.getString("ipPush","172.23.247.53");
        String gps=preferences.getString("gps","50");
        String port=preferences.getString("port","8080");
        et_ip.setText(ip);
        etPushIp.setText(ipPush);
        et_gps.setText(gps);
        et_port.setText(port);
    }
    private void testHttp(){
        OKHttpUtils okHttpUtils=new OKHttpUtils.Builder(this).build();
        RequestBody requestBody = new FormBody.Builder().add("KEYDATA",  "admin,admin" ).build();
        final Request request=new  Request.Builder().url("http://192.168.191.1:8080/GBEMSSystemMGR/applogin/login_login").post(requestBody).build();

        okHttpUtils.request(request, CacheType.ONLY_NETWORK, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("info","加载失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("info","加载成功"+response.body().string());
            }
        });


    }

}
