package com.chinasoft.ctams.activity.addresssBook;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinasoft.ctams.R;
import com.chinasoft.ctams.base.BaseActivity;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 说明:
 */
public class ContactPhoneActivity extends BaseActivity {
    @Bind(R.id.tv_contact_phone_name)
    TextView tv_name;
    @Bind(R.id.tv_contact_phone_number)
    TextView tv_phoneNumber;
    @Bind(R.id.tv_contact_phone_call)
    TextView tv_phoneCall;
    @Bind(R.id.tv_contact_phone_sendMessage)
    TextView tv_sendMessage;
    @Bind(R.id.iv_contact_phone_back)
    ImageView iv_back;


    private String phoneNumber;
    private String name;

    @Override
    public int getLayoutId() {
        return R.layout.activity_contact_phone_information;
    }

    @Override
    public void initPage(Bundle savedInstanceState) {
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        phoneNumber = intent.getStringExtra("phoneNumber");
        tv_name.setText(name);
        tv_phoneNumber.setText(phoneNumber);
    }
//
    @OnClick({R.id.rl_call_one_pressed,R.id.rl_send_message_one_pressed, R.id.iv_contact_phone_back})
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.rl_call_one_pressed:
                intent.setAction(Intent.ACTION_CALL);
                Log.i("tag","ph"+phoneNumber);
                intent.setData(Uri.parse("tel:" + phoneNumber));
                ContactPhoneActivity.this.startActivity(intent);
                break;
            case R.id.rl_send_message_one_pressed:
                intent.setAction(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("smsto:" + phoneNumber));
                ContactPhoneActivity.this.startActivity(intent);
                break;
            case R.id.iv_contact_phone_back:
                finish();
                break;
//            case R.id.rl_contact_phone_add:
//                addToOftenContact();
//                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}