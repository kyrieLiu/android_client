package com.chinasoft.ctams.activity.sendPaper;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.chinasoft.ctams.R;
import com.chinasoft.ctams.base.BaseActivity;
import com.chinasoft.ctams.model.MyServerModel;
import com.chinasoft.ctams.util.ConstantCode;
import com.chinasoft.ctams.util.SharedPreferencesHelper;
import com.chinasoft.ctams.view.CustomProgressDialog;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by liuyin on 2016/9/19.
 * 上传事件信息
 */
public class SendThingActivity extends BaseActivity {
    @Bind(R.id.iv_send_thing_back)
    ImageView iv_back;
    @Bind(R.id.et_send_thing_content)
    EditText et_content;
    @Bind(R.id.bt_patrol_feedBack_commit)
    Button bt_commit;
    @Bind(R.id.bt_patrol_feedBack_cancel)
    Button bt_cancel;

    private CustomProgressDialog dialog;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (dialog!=null)
                dialog.dismiss();
            switch (msg.what){
                case ConstantCode.POST_FAILED:
                    Toast.makeText(SendThingActivity.this,"上传失败,请检查网络!",Toast.LENGTH_SHORT).show();
                    break;
                case ConstantCode.POST_SUCCESS:
                    Toast.makeText(SendThingActivity.this,"上传成功",Toast.LENGTH_SHORT).show();
                    SendThingActivity.this.finish();
                    break;
            }
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_thing;
    }

    @Override
    public void initPage(Bundle savedInstanceState) {

        if (dialog==null){
            dialog=CustomProgressDialog.createDialog(this);
        }
        dialog.setMessage("正在上传...");
    }


    @OnClick({R.id.iv_send_thing_back, R.id.bt_patrol_feedBack_commit, R.id.bt_patrol_feedBack_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_send_thing_back:
                finish();
                break;
            case R.id.bt_patrol_feedBack_commit:
                String content=et_content.getText().toString();
                String userId=SharedPreferencesHelper.getInstance().getPeopleId(this);
                MyServerModel.getInstance(this).sendThings(handler,this,userId,content, Build.DISPLAY);
                dialog.show();
                break;
            case R.id.bt_patrol_feedBack_cancel:
                finish();
                break;
        }
    }
}
