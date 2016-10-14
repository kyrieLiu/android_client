package com.chinasoft.ctams.activity.sendPaper.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.chinasoft.ctams.R;
import com.chinasoft.ctams.base.BaseActivity;
import com.chinasoft.ctams.model.ServerModel;
import com.chinasoft.ctams.util.ConstantCode;
import com.chinasoft.ctams.util.SharedPreferencesHelper;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/6/20.
 */
public class AddSendPaperActivity extends BaseActivity {
    private final String URL_Message_Type = "bizSubmittedInfo/listMessageType";
    private final String URL_ADD_SEND_PAPER = "bizSubmittedInfo/saveBizFrequencyinfo";
    private final String URL_UPDATE_SEND_PAPER = "bizSubmittedInfo/update";
    @Bind(R.id.et_title_send_paper)
    EditText etTitleSendPaper;
    @Bind(R.id.sp_message_type_send_paper)
    Spinner spMessageTypeSendPaper;
    @Bind(R.id.tv_company_send_paper)
    TextView tvCompanySendPaper;
    @Bind(R.id.tv_send_man_send_paper)
    TextView tvSendManSendPaper;
    @Bind(R.id.tv_receive_company_send_paper)
    EditText tvReceiveCompanySendPaper;
    @Bind(R.id.et_content_send_paper)
    EditText etContentSendPaper;
    @Bind(R.id.bt_patrol_feedBack_commit)
    Button btCommitSendPaper;
    @Bind(R.id.bt_patrol_feedBack_cancel)
    Button btCancelSendPaper;
    @Bind(R.id.iv_title_back)
    ImageView ivTitleBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.et_send_address_send_paper)
    EditText et_address;
    private String[] types;
    private ArrayAdapter spinnerAdapter;
    private int UpdateSpPosition = 0;
    private String messageType = "";
    private String sendAddress;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            btCommitSendPaper.setClickable(true);
            if (msg.what == ConstantCode.POST_SUCCESS) {
                Toast.makeText(AddSendPaperActivity.this, "添加信息成功！", Toast.LENGTH_SHORT).show();
                finish();
            } else if (msg.what == ConstantCode.DATA_ADD_UPDATE_FAILED) {
                Toast.makeText(AddSendPaperActivity.this, "修改信息失败！", Toast.LENGTH_SHORT).show();
            } else if (msg.what == ConstantCode.DATA_ADD_UPDATE_SUCCESS) {
                Toast.makeText(AddSendPaperActivity.this, "修改信息成功！", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(AddSendPaperActivity.this, "添加信息失败！", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_send_paper;
    }

    @Override
    public void initPage(Bundle savedInstanceState) {
        init();
        initData();
    }

    private void init() {
        //isAdd = getIntent().getBooleanExtra("isAdd", true);
        types = getResources().getStringArray(R.array.sendPaperType);
        spinnerAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, types);
        spMessageTypeSendPaper.setAdapter(spinnerAdapter);
            tvTitle.setText("新增信息报送");
            spMessageTypeSendPaper.setSelection(0);
            tvCompanySendPaper.setText(SharedPreferencesHelper.getInstance().getOrganization(AddSendPaperActivity.this));
            tvSendManSendPaper.setText(SharedPreferencesHelper.getInstance().getPeopleName(AddSendPaperActivity.this));
    }

    private void initData() {
        spMessageTypeSendPaper.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        messageType = "";
                        break;
                    case 1:
                        messageType = "1000012";
                        break;
                    case 2:
                        messageType = "1000011";
                        break;
                    case 3:
                        messageType = "1000013";
                        break;
                    case 4:
                        messageType = "1000014";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    @OnClick({R.id.iv_title_back, R.id.bt_patrol_feedBack_commit, R.id.bt_patrol_feedBack_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.bt_patrol_feedBack_commit:
                String title = etTitleSendPaper.getText().toString();
                String submitUnit = tvCompanySendPaper.getText().toString();
                String sendMan = tvSendManSendPaper.getText().toString();
                String receivedUnit = tvReceiveCompanySendPaper.getText().toString();
                String content = etContentSendPaper.getText().toString();
                sendAddress=et_address.getText().toString();
                if (!YZ(title, messageType, submitUnit, sendMan, receivedUnit, content, sendAddress)) {
                    return;
                }
                ServerModel.getInstance(this).AddSendPaper(handler, URL_ADD_SEND_PAPER, title, messageType,  submitUnit, sendMan, receivedUnit, content, sendAddress, this);
                break;
            case R.id.bt_patrol_feedBack_cancel:
                finish();
                break;
        }
    }




    private boolean YZ(String title, String type,  String sendUnit, String sendMan, String receivedUnit, String content,String sendAddressBean) {
        if (title == null || "".equals(title)) {
            Toast.makeText(AddSendPaperActivity.this, "标题不能为空", Toast.LENGTH_SHORT).show();
            return false;
        } else if (type == null || "".equals(type)) {
            Toast.makeText(AddSendPaperActivity.this, "信息类型不能为空", Toast.LENGTH_SHORT).show();
            return false;
        } else if (sendUnit == null || "".equals(sendUnit)) {
            Toast.makeText(AddSendPaperActivity.this, "报送单位不能为空", Toast.LENGTH_SHORT).show();
            return false;
        } else if (sendMan == null || "".equals(sendMan)) {
            Toast.makeText(AddSendPaperActivity.this, "报送人员不能为空", Toast.LENGTH_SHORT).show();
            return false;
        } else if (receivedUnit == null || "".equals(receivedUnit)) {
            Toast.makeText(AddSendPaperActivity.this, "接收单位不能为空", Toast.LENGTH_SHORT).show();
            return false;
        } else if (content == null || "".equals(content)) {
            Toast.makeText(AddSendPaperActivity.this, "报送内容不能为空", Toast.LENGTH_SHORT).show();
            return false;
        } else if (sendAddressBean == null || "".equals(sendAddressBean)) {
            Toast.makeText(AddSendPaperActivity.this, "报送地点不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }else {
            return true;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ServerModel.getInstance(this).cancelTag(this);
    }
}
