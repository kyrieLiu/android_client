package com.chinasoft.ctams.activity.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chinasoft.ctams.R;
import com.chinasoft.ctams.activity.scheduleManager.DateListMainActivity;
import com.chinasoft.ctams.activity.search.activity.ComprehensiveSearchActivity;
import com.chinasoft.ctams.activity.statistic.StatisticActivity;
import com.chinasoft.ctams.adapter.LvDrawerAdapter;
import com.chinasoft.ctams.application.MyApplication;
import com.chinasoft.ctams.base.BaseActivity;
import com.chinasoft.ctams.fragment.MainFragment;
import com.chinasoft.ctams.model.GPSInfoService;
import com.chinasoft.ctams.util.SharedPreferencesHelper;
import com.chinasoft.ctams.view.CircleImageView;

import org.androidpn.client.Constants;
import org.androidpn.client.ServiceManager;

import java.io.File;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/6/16.
 */
public class MainActivity extends BaseActivity {

    @Bind(R.id.ll_titlebar_main_left_layout)
    LinearLayout llTitlebarMainLeftLayout;
    @Bind(R.id.drawer_main_layout)
    DrawerLayout drawerMainLayout;
    @Bind(R.id.lv_drawer_left_menu_item)
    ListView lvDrawerLeftMenuItem;
    @Bind(R.id.iv_drawer_left_user_headicon)
    CircleImageView iv_head_photo;
    @Bind(R.id.tv_drawer_left_username)
    TextView tv_username;
    @Bind(R.id.rl_exit_drawer)
    RelativeLayout rl_exit;

    private LvDrawerAdapter lvDrawerAdapter;
    private int drawerBg[] = {R.drawable.drawer_item_important_message_selector, R.drawable.drawer_item_daily_intelligence_selector,
             R.drawable.drawer_item_session_manager_selector,R.drawable.drawer_item_about_selector};
    private String[] items = {"排班管理", "综合查询","接报统计","检查更新"};

    private long firstTime = 0; // 退出间隔
    private MyReceiver receiver;//广播接收器,接受开始巡逻和结束巡逻发送来的广播
    private int receiveFlag;



    private boolean alreadyLogin=true;
    private ServiceManager serviceManager;
    private String userName;


    @Override
    public int getLayoutId() {
        judgeIsLogin();//判断是否已经登录
        return R.layout.activity_main;
    }

    @Override
    public void initPage(Bundle savedInstanceState) {

        if (alreadyLogin){
            initView();//初始化界面
            setListener();//监听按钮和listView
            registerBroadcast();//注册广播
            connectXmppServer();//连接推送服务
            GPSInfoService.getInstance(this).registenerLocationChangeListener();//开启GPS定位
        }
    }

    private void judgeIsLogin(){
        alreadyLogin=SharedPreferencesHelper.getInstance().getLoginFlag(this);
        if (!alreadyLogin){
            Intent intent=new Intent(this,LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void initView() {
        lvDrawerAdapter = new LvDrawerAdapter(this, items, drawerBg);
        lvDrawerLeftMenuItem.setAdapter(lvDrawerAdapter);
        showMainFragment();
        File file = new File(Environment.getExternalStorageDirectory() + "/avater.png");
        Glide.with(this).load(file).placeholder(R.mipmap.app_logo).into(iv_head_photo);

        userName = SharedPreferencesHelper.getInstance().getUserName(this);
        tv_username.setText(userName);
    }

    /**
     * 注册广播
     */
    private void registerBroadcast() {
        receiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.chinasoft.ctams.activity.main");
        registerReceiver(receiver, intentFilter);
    }

    private void setListener() {
        lvDrawerLeftMenuItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent();
                switch (position) {
                    case 0:
                        intent.setClass(MainActivity.this,DateListMainActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        intent.setClass(MainActivity.this,ComprehensiveSearchActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent.setClass(MainActivity.this,StatisticActivity.class);
                        startActivity(intent);
                        break;
                    case 3:
                        intent.setClass(MainActivity.this,CheckVersionActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });
    }

    /**
     * 加载主视图区域的Fragment
     */
    private void showMainFragment() {
        MainFragment mainFragment = MainFragment.getInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fl_drawer_main_container, mainFragment).commit();
    }

    @OnClick({R.id.rl_exit_drawer,R.id.ll_titlebar_main_left_layout,R.id.tv_drawer_switch_count})
    public void onClick(View view) {
        switch (view.getId()){
            //退出
            case R.id.rl_exit_drawer:
                //取消GPS定位
                GPSInfoService.getInstance(this).unRegisterLocationChangeListener();

                List<Activity> activities = ((MyApplication)getApplication()).getActivities();
                for(Activity activity:activities){
                    activity.finish();
                }
                break;
            //控制侧滑菜单弹出隐藏
            case R.id.ll_titlebar_main_left_layout:
                drawerMainLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.tv_drawer_switch_count:
                switchAcount();//切换账号
                break;
        }
    }
    /**
     * 连接推送服务
     */
    private void connectXmppServer(){
        //登陆成功后建立socket长连接
        if (serviceManager==null){
            serviceManager=new ServiceManager(this);
        }

        SharedPreferences preferences = getSharedPreferences(Constants.SHARED_PREFERENCE_NAME,
                Context.MODE_PRIVATE);
        String oldName=preferences.getString(Constants.XMPP_USERNAME, "");
        String username=SharedPreferencesHelper.getInstance().getUserName(this);
        //如果登陆名未更改,普通长连接,如果更改,清除原先数据,重新注册长连接
        if (oldName.equals(username)){
            serviceManager.startService(username,false);
        }else{
            serviceManager.startService(username,true);
        }
    }
    /**
     * 切换账号
     */
    private  void switchAcount(){
        GPSInfoService.getInstance(this).unRegisterLocationChangeListener();
        AlertDialog dialog=new AlertDialog.Builder(this).setTitle("系统提示")
                .setMessage("您的系统已经与"+userName+"账户绑定,切换账号之前请确定该系统在指挥中心已经与"+userName+"账户解除绑定!")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferencesHelper.getInstance().saveLoginFlag(false,MainActivity.this);

                        Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        MainActivity.this.finish();
                        serviceManager.stopService();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    /**
     *两次按键退出
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                long secondTime = System.currentTimeMillis();
                if (secondTime - firstTime > 2000) { // 如果两次按键时间间隔大于2秒，则不退出
                    Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    firstTime = secondTime;// 更新firstTime
                    return true;
                } else { // 两次按键小于2秒时，退出应用
                    finish();
                }
                break;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (alreadyLogin){
            unregisterReceiver(receiver);
        }
    }

    class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            receiveFlag = intent.getIntExtra("flag", 0);
            if (receiveFlag == 1) {
                //由开始巡逻发送来的广播
                GPSInfoService.isPatrol = true;
                GPSInfoService.patrolNumber = intent.getIntExtra("number", 1);
            } else if (receiveFlag == 5) {
                //巡逻结束
                GPSInfoService.isPatrol = false;
                GPSInfoService.patrolNumber = intent.getIntExtra("number", 1);
            }
        }
    }
}
