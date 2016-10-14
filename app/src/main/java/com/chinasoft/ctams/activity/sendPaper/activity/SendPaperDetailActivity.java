package com.chinasoft.ctams.activity.sendPaper.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chinasoft.ctams.R;
import com.chinasoft.ctams.activity.sendPaper.SendPaperMainActivity;
import com.chinasoft.ctams.activity.task.bean.SendPaperBean;
import com.chinasoft.ctams.base.BaseActivity;
import com.chinasoft.ctams.model.ServerModel;
import com.chinasoft.ctams.util.ConstantCode;

import butterknife.Bind;

/**
 * Created by 李硕 on 2016/6/22.
 * 信息详情
 */
public class SendPaperDetailActivity extends BaseActivity {
    private final String URL_SEND_PAPER_DETAIL = "bizSubmittedInfo/showBizFrequencyinfo";
    private final String URL_SEND_PAPER_DELETE = "bizSubmittedInfo/deleteBiz";
    @Bind(R.id.iv_left_titlebar_main_icon)
    ImageView ivLeftTitlebarMainIcon;
    @Bind(R.id.ll_titlebar_main_left_layout)
    LinearLayout llTitlebarMainLeftLayout;
    @Bind(R.id.tv_title_titlebar_main)
    TextView tvTitleTitlebarMain;
    @Bind(R.id.tv_title_detail_send_paper)
    TextView tvTitleDetailSendPaper;
    @Bind(R.id.tv_detail_message_type_send_paper)
    TextView tvDetailMessageTypeSendPaper;
    @Bind(R.id.tv_detail_status_send_paper)
    TextView tvDetailStatusSendPaper;
    @Bind(R.id.tv_send_time_detail_send_paper)
    TextView tvSendTimeDetailSendPaper;
    @Bind(R.id.tv_detail_send_unit_send_paper)
    TextView tvDetailSendUnitSendPaper;
    @Bind(R.id.tv_detail_send_man_send_paper)
    TextView tvDetailSendManSendPaper;
    @Bind(R.id.tv_detail_receive_company_send_paper)
    TextView tvDetailReceiveCompanySendPaper;
    @Bind(R.id.tv_detail_content_send_paper)
    TextView tvDetailContentSendPaper;

    private SendPaperBean sendPaperBean;
    private String paperId = "";
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == ConstantCode.DATA_DELETED_SUCCESS) {
                Toast.makeText(SendPaperDetailActivity.this, "删除成功！", Toast.LENGTH_SHORT).show();
                SendPaperMainActivity.isChange = true;
                finish();
            } else if (msg.what == ConstantCode.DATA_DELETED_FAILED) {
                Toast.makeText(SendPaperDetailActivity.this, "删除失败！请重试！", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(SendPaperDetailActivity.this, R.string.getDataFailed, Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_detail_send_paper;
    }

    @Override
    public void initPage(Bundle savedInstanceState) {
        init();
        initData();

    }

    private void init() {
        sendPaperBean = (SendPaperBean) getIntent().getSerializableExtra("detail");
        paperId = sendPaperBean.getSendPaperId();
        ivLeftTitlebarMainIcon.setImageResource(R.mipmap.title_back_icon);
        tvTitleTitlebarMain.setText("报送详情");
        initDetail(sendPaperBean);
    }

    private void initData() {
        llTitlebarMainLeftLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendPaperMainActivity.isChange = false;
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        Log.i("tag", "" + SendPaperMainActivity.isChange);
        SendPaperMainActivity.isChange = false;
        super.onBackPressed();
    }

    private void initDetail(SendPaperBean sendPaperBean) {
        tvTitleDetailSendPaper.setText(sendPaperBean.getSendPaperTitle());
        tvDetailMessageTypeSendPaper.setText(sendPaperBean.getSendPaperMessageType());
        tvDetailStatusSendPaper.setText(sendPaperBean.getSendPaperStatus());
        if ("审核通过".equals(sendPaperBean.getSendPaperStatus())) {
            tvDetailStatusSendPaper.setTextColor(Color.rgb(188, 230, 114));
        }
        tvSendTimeDetailSendPaper.setText(sendPaperBean.getSendPaperDate());
        tvDetailSendUnitSendPaper.setText(sendPaperBean.getSendPaperSendUnit());
        tvDetailSendManSendPaper.setText(sendPaperBean.getSendPaperPerson());
        tvDetailReceiveCompanySendPaper.setText(sendPaperBean.getSendPaperReceivedUnit());
        tvDetailContentSendPaper.setText(sendPaperBean.getSendPaperContent());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ServerModel.getInstance(this).cancelTag(this);
    }

}
