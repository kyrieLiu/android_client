package com.chinasoft.ctams.activity.chat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.chinasoft.ctams.R;
import com.chinasoft.ctams.adapter.ChatAdatper;
import com.chinasoft.ctams.base.BaseActivity;

import org.androidpn.client.Constants;
import org.androidpn.client.ServiceManager;
import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import sql.AlreadyReadMessage;
import sql.ChatInformation;

/**
 * 聊天界面
 */
public class ChatActivity extends BaseActivity implements View.OnClickListener{

    @Bind(R.id.lv_chat_message_list)
    ListView lv_chatMessageList;
    @Bind(R.id.bt_chat_send)
    Button bt_send;
    @Bind(R.id.et_chat_content)
    EditText et_content;
    @Bind(R.id.iv_title_back)
    ImageView iv_back;
    @Bind(R.id.tv_title)
    TextView tv_title;
    @Bind(R.id.tv_head_menu)
    TextView tv_headMenu;
    @Bind(R.id.tv_chat_alert)
    TextView tv_alert;

    private ServiceManager serviceManager;
    private SimpleDateFormat format;
    private ChatAdatper chatAdatper;
    private List<AlreadyReadMessage> alreadyReadMessages;
    private List<AlreadyReadMessage> allMessage;
    private MyReceiver myReceiver;
    private IntentFilter intentFilter;
    private String peopleName;
    private String accountName;

    private PopupWindow popupWindow;
    @Override
    public int getLayoutId() {
        return R.layout.activity_chat;
    }

    @Override
    public void initPage(Bundle savedInstanceState) {
        initPopupWindow();
        tv_headMenu.setVisibility(View.VISIBLE);
        tv_headMenu.setBackgroundResource(R.mipmap.icon_right_popup_menu);
        Intent intent = getIntent();
        peopleName = intent.getStringExtra("peopleName");
        accountName = intent.getStringExtra("accountName");
        tv_title.setText(peopleName);
        format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //获取serviceManager实例
        serviceManager = new ServiceManager(this);
        //查询该联系人的聊天记录
        allMessage = DataSupport.where("targetAccountName=?", accountName).find(AlreadyReadMessage.class);
        alreadyReadMessages=new ArrayList<>();
        int listLength=allMessage.size();
        if (listLength>=30){
            tv_alert.setVisibility(View.VISIBLE);
            alreadyReadMessages=allMessage.subList(listLength-30,listLength);
        }
        else{
            alreadyReadMessages.addAll(allMessage);
        }

        chatAdatper = new ChatAdatper(alreadyReadMessages, this);
        lv_chatMessageList.setAdapter(chatAdatper);
        lv_chatMessageList.setSelection(chatAdatper.getCount());

        //注册聊天信息广播接收
        myReceiver = new MyReceiver();
        intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.ACTION_MESSAGE_RECEIVED);
        registerReceiver(myReceiver, intentFilter);
    }


    @OnClick({R.id.bt_chat_send, R.id.tv_head_menu,R.id.iv_title_back})
    public void onClick(View view) {
        switch (view.getId()) {
            //发送消息并更新界面
            case R.id.bt_chat_send:
                String content = et_content.getText().toString();
                if ("".equals(content)){
                    Toast.makeText(ChatActivity.this,"消息不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                String sendTime = format.format(new Date());
                AlreadyReadMessage messageBean = new AlreadyReadMessage();
                messageBean.setSendTime(sendTime);
                messageBean.setReceive(false);
                messageBean.setMessageContent(content);
                messageBean.setTargetAccountName(accountName);
                messageBean.setPeopleName(peopleName);
                messageBean.setPhotoUrl("url");
                messageBean.save();
                serviceManager.sendMessage(content, accountName,null);
                alreadyReadMessages.add(messageBean);
                chatAdatper.notifyDataSetChanged();
                et_content.setText("");
                break;

            case R.id.tv_head_menu:
                int[] location = new int[2];
                lv_chatMessageList  .getLocationInWindow(location);
                int x = location[0];
                int y = location[1];
                popupWindow.showAtLocation(lv_chatMessageList, Gravity.RIGHT | Gravity.TOP, 0,y);
                break;
            case R.id.tv_popup_chat_deleteLog:
                DataSupport.deleteAll(AlreadyReadMessage.class, "targetAccountName=?", accountName);
                alreadyReadMessages.clear();
                chatAdatper.notifyDataSetChanged();
                popupWindow.dismiss();
                tv_alert.setVisibility(View.GONE);
                break;
            case R.id.iv_title_back:
                finish();
                break;
        }

    }


    /**
     * 聊天信息接收
     */
    private class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (Constants.ACTION_MESSAGE_RECEIVED.equals(action)) {
                List<ChatInformation> informations = DataSupport.where("targetAccountName=?", accountName).find(ChatInformation.class);
                for (ChatInformation information : informations) {
                    AlreadyReadMessage alreadyReadMessage = new AlreadyReadMessage();
                    alreadyReadMessage.setPhotoUrl(information.getPhotoUrl());
                    alreadyReadMessage.setTargetAccountName(information.getTargetAccountName());
                    alreadyReadMessage.setPeopleName(information.getPeopleName());
                    alreadyReadMessage.setMessageContent(information.getMessageContent());
                    alreadyReadMessage.setReceive(true);
                    alreadyReadMessage.setSendTime(information.getSendTime());
                    alreadyReadMessage.save();
                    alreadyReadMessages.add(alreadyReadMessage);
                    chatAdatper.notifyDataSetChanged();
                }
                DataSupport.deleteAll(ChatInformation.class, "targetAccountName=?", accountName);
            }
        }
    }
    private void initPopupWindow() {
        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(this.LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.popup_chat_menu, null);
        popupWindow = new PopupWindow(popupView, 300, ActionBar.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.bg_black));
        TextView tv_deleteLog = (TextView) popupView.findViewById(R.id.tv_popup_chat_deleteLog);
        tv_deleteLog.setOnClickListener(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myReceiver);
    }
}
