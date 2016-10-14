package com.chinasoft.ctams.activity.main;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.chinasoft.ctams.R;
import com.chinasoft.ctams.base.BaseActivity;
import com.chinasoft.ctams.model.ServerModel;
import com.chinasoft.ctams.util.ConstantCode;
import com.chinasoft.ctams.util.SharedPreferencesHelper;
import com.chinasoft.ctams.view.ClearableEditTextWithIcon;
import com.chinasoft.ctams.view.CustomProgressDialog;

import butterknife.Bind;

public class LoginActivity extends BaseActivity {
    //登陆接口
    private final String Login_URL="login";

    @Bind(R.id.et_user_name_login)
    ClearableEditTextWithIcon etUserNameLogin;
    @Bind(R.id.et_user_pwd_login)
    ClearableEditTextWithIcon etUserPwdLogin;
    @Bind(R.id.bt_to_login)
    Button btToLogin;
    private String username,userpwd;
    private CustomProgressDialog progressDialog = null;

    private static final String TAG = "LoginActivity";


    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            stopProgressDialog();
            switch (msg.what){
                case ConstantCode.POST_SUCCESS:
                    SharedPreferencesHelper.getInstance().saveLoginFlag(true,LoginActivity.this);
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    finish();
                    break;
                case ConstantCode.POST_FAILED:
                    Toast.makeText(getApplicationContext(), R.string.login_wrong,Toast.LENGTH_SHORT).show();
                    break;
                case ConstantCode.DEV_EXCEPTION:
                    Toast.makeText(getApplicationContext(), R.string.dev_exception,Toast.LENGTH_SHORT).show();
                    break;
            }

        }
    };


    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initPage(Bundle savedInstanceState) {
        initData();
    }

    private void initData() {

        String loginName= SharedPreferencesHelper.getInstance().getUserName(this);
        String password= SharedPreferencesHelper.getInstance().getPassword(this);
        etUserNameLogin.setText(loginName);
        etUserPwdLogin.setText(password);

        etUserNameLogin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                changeBtnLogin();}

            @Override
            public void afterTextChanged(Editable s) {}
        });
        etUserPwdLogin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                changeBtnLogin();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        btToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username=etUserNameLogin.getText().toString();
                userpwd=etUserPwdLogin.getText().toString();
                if (!isNullAccount(username,userpwd)){
                    //SharedPreferencesHelper.getInstance().saveUserInfo(LoginActivity.this,username,userpwd,"","","","");
                    login();
                }
            }
        });
    }

    private void login(){
        PackageManager manager=getPackageManager();
        PackageInfo info= null;
        String version="";
        try {
            info = manager.getPackageInfo(getPackageName(),0);
            version = info.versionName;//app版本号
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String display=Build.DISPLAY;//手机系统版本号
        String model=Build.MODEL;//手机型号
        ServerModel.getInstance(this).login(handler,username,userpwd,Login_URL,display, model,version,this);
        startProgressDialog();
    }

    private void changeBtnLogin(){
        if (etUserNameLogin.isNull==false && etUserPwdLogin.isNull==false){
            btToLogin.setBackgroundColor(Color.rgb(198,209,99));
        }else {
            btToLogin.setBackgroundColor(Color.WHITE);
        }
        if (etUserNameLogin.isNull && !etUserPwdLogin.isNull){
            etUserPwdLogin.setText("");
        }
    }
    private boolean isNullAccount(String username,String userpwd){
        if (username==null || username.equals("")){
            Toast.makeText(getApplicationContext(), R.string.miss_username,Toast.LENGTH_SHORT).show();
            return true;
        }else {
            if (userpwd==null || userpwd.equals("")){
                Toast.makeText(getApplicationContext(), R.string.miss_password,Toast.LENGTH_SHORT).show();
                return true;
            }else {
                return false;
            }
        }
    }

    private void startProgressDialog(){
        if (progressDialog == null){
            progressDialog = CustomProgressDialog.createDialog(this);
            progressDialog.setMessage("正在登录...");
        }
        progressDialog.show();
    }

    private void stopProgressDialog(){
        if (progressDialog != null){
            progressDialog.hide();
            progressDialog.dismiss();
            progressDialog = null;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ServerModel.getInstance(this).cancelTag(this);
        progressDialog=null;
    }
}
