package com.chinasoft.ctams.activity.chat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chinasoft.ctams.R;
import com.chinasoft.ctams.adapter.FriendsAdapter;
import com.chinasoft.ctams.base.BaseActivity;
import com.chinasoft.ctams.bean.bean_json.FriendListBean;
import com.chinasoft.ctams.model.MyServerModel;
import com.chinasoft.ctams.util.ConstantCode;
import com.chinasoft.ctams.view.CustomProgressDialog;

import org.androidpn.client.Constants;
import org.litepal.crud.DataSupport;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import sql.AlreadyReadMessage;
import sql.ChatInformation;

/**
 * Created by Kyrie on 2016/6/21.
 * Email:kyrie_liu@sina.com
 * 好友列表界面
 */
public class FriendsListActivity extends BaseActivity {

    @Bind(R.id.iv_title_back)
    ImageView iv_back;
    @Bind(R.id.tv_title)
    TextView tv_title;
    @Bind(R.id.tv_head_menu)
    TextView tvHeadMenu;
    @Bind(R.id.lv_friend_main)
    ListView lv_friends;

    private List<ChatInformation> chatInformationList;
    private FriendsAdapter friendsAdapter;
    private List<FriendListBean> friendList;
    private MyReceiver myReceiver;
    private IntentFilter intentFilter;

    private CustomProgressDialog progressDialog;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (progressDialog!=null) {
                progressDialog.dismiss();
            }
            switch (msg.what) {
                case ConstantCode.POST_SUCCESS:
                    //加载所有好友列表
                    chatInformationList = DataSupport.findAll(ChatInformation.class);
                    SharedPreferences preferences = getSharedPreferences(Constants.SHARED_PREFERENCE_NAME,
                            Context.MODE_PRIVATE);
                    String userName = preferences.getString(Constants.XMPP_USERNAME, "暂无数据");
                    friendList = (List<FriendListBean>) msg.obj;
                    for (int i = 0; i < friendList.size(); i++) {
                        FriendListBean bean = friendList.get(i);
                        if (bean.getAccountName().equals(userName)) {
                            friendList.remove(bean);
                        }
                    }
                    friendsAdapter = new FriendsAdapter(FriendsListActivity.this, friendList, chatInformationList);
                    lv_friends.setAdapter(friendsAdapter);
                    break;
                case ConstantCode.POST_FAILED:
                    Toast.makeText(FriendsListActivity.this, "加载失败,请检查网络", Toast.LENGTH_SHORT).show();;
                    break;
            }
        }
    };


    @Override
    public int getLayoutId() {
        return R.layout.activity_user_list;
    }

    @Override
    public void initPage(Bundle savedInstanceState) {
        tv_title.setText("好友列表");


        //注册消息广播
        myReceiver = new MyReceiver();
        intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.ACTION_MESSAGE_RECEIVED);
        registerReceiver(myReceiver, intentFilter);

        initData();
        listenListView();
    }

    @OnClick({R.id.iv_title_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                FriendsListActivity.this.setResult(0);
                finish();
                break;
        }
    }

    /**
     * 监听好友列表
     */
    private void listenListView() {
        lv_friends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FriendListBean friendListBean = (FriendListBean) friendsAdapter.getItem(position);
                String peopleName = friendListBean.getPeopleName();
                String accountName = friendListBean.getAccountName();
                Intent intent = new Intent(FriendsListActivity.this, ChatActivity.class);
                intent.putExtra("peopleName", peopleName);
                intent.putExtra("accountName", accountName);

                List<ChatInformation> readList = DataSupport.where("targetAccountName=?", accountName).find(ChatInformation.class);
                for (ChatInformation information : readList) {
                    AlreadyReadMessage alreadyReadMessage = new AlreadyReadMessage();
                    alreadyReadMessage.setPhotoUrl(information.getPhotoUrl());
                    alreadyReadMessage.setTargetAccountName(information.getTargetAccountName());
                    alreadyReadMessage.setMessageContent(information.getMessageContent());
                    alreadyReadMessage.setReceive(true);
                    alreadyReadMessage.setSendTime(information.getSendTime());
                    alreadyReadMessage.save();
                }

                DataSupport.deleteAll(ChatInformation.class, "targetAccountName=?", accountName);
                startActivityForResult(intent, 0);
            }
        });
    }

    private void initData() {
        //加载好友列表信息
        MyServerModel.getInstance(this).loadUserList(handler,this);
        if (progressDialog == null) {
            progressDialog = CustomProgressDialog.createDialog(this);
            progressDialog.setMessage("正在加载...");
        }
        progressDialog.show();
    }

    //返回刷新界面
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0) {
            refreshFriendList();
        }
    }


    private class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Constants.ACTION_MESSAGE_RECEIVED)) {
//                Vibrator vibrator=(Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
//                vibrator.vibrate(700);
                refreshFriendList();
            }
        }
    }

    private void refreshFriendList() {
        chatInformationList = DataSupport.findAll(ChatInformation.class);

        friendsAdapter = new FriendsAdapter(FriendsListActivity.this, friendList, chatInformationList);
        lv_friends.setAdapter(friendsAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myReceiver);
        MyServerModel.getInstance(this).cancelTag(this);
        progressDialog=null;
    }
}
