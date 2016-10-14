package com.chinasoft.ctams.activity.scheduleManager.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.chinasoft.ctams.R;
import com.chinasoft.ctams.base.BaseActivity;
import com.chinasoft.ctams.bean.bean_json.ScheduleDetailsBean;
import com.chinasoft.ctams.bean.bean_json.ScheduleInformationBean;
import com.chinasoft.ctams.activity.scheduleManager.adapter.LvDateWeekExcelAdapter;
import com.chinasoft.ctams.model.MyServerModel;
import com.chinasoft.ctams.util.ConstantCode;
import com.chinasoft.ctams.view.CustomProgressDialog;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/6/18.
 */
public class DateExcelActivity extends BaseActivity {
    @Bind(R.id.tv_fragment_date_excel_start)
    TextView tv_start;
    @Bind(R.id.tv_fragment_date_excel_end)
    TextView tv_end;
    @Bind(R.id.tv_fragment_date_excel_title)
    TextView tv_excelTitle;
    @Bind(R.id.tv_fragment_date_excel_title_phone)
    TextView tv_phone;
    @Bind(R.id.tv_fragment_date_excel_leaderName)
    TextView tv_leaderName;
    @Bind(R.id.tv_fragment_date_excel_type)
    TextView tv_type;
    @Bind(R.id.lv_fragment_date_excel)
    ListView lv_dateExcel;
    @Bind(R.id.iv_title_back)
    ImageView iv_back;
    @Bind(R.id.tv_title)
    TextView tv_title;
    @Bind(R.id.tv_fragment_date_excel_leaderJob)
    TextView tvFragmentDateExcelLeaderJob;

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
                    ScheduleInformationBean bean = (ScheduleInformationBean) msg.obj;
                    initData(bean);
                    break;
            }
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_date_excel;
    }

    @Override
    public void initPage(Bundle savedInstanceState) {
        tv_title.setText("排班详情");
        String id = getIntent().getStringExtra("id");
        MyServerModel.getInstance(this).loadScheduleDetails(handler, id,this);
        if (progressDialog == null) {
            progressDialog = CustomProgressDialog.createDialog(this);
            progressDialog.setMessage("正在加载...");
        }
        progressDialog.show();
    }

    /**
     * 加载数据
     *
     * @param informationBean
     */
    private void initData(ScheduleInformationBean informationBean) {

        tv_start.setText(informationBean.getSchedulStarttime());
        tv_end.setText(informationBean.getSchedulEndtime());
        tv_excelTitle.setText(informationBean.getSchedulIndicate());
        tv_leaderName.setText(informationBean.getSchedulLeader());
        tv_phone.setText(informationBean.getSchedulPhone());
        tv_type.setText(informationBean.getSchedulType());

        List<ScheduleDetailsBean> list = informationBean.getList();
        LvDateWeekExcelAdapter lvDateWeekExcelAdapter = new LvDateWeekExcelAdapter(this, list);
        lv_dateExcel.setAdapter(lvDateWeekExcelAdapter);


    }


    @OnClick(R.id.iv_title_back)
    public void onClick() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        progressDialog=null;
        MyServerModel.getInstance(this).cancelTag(this);
    }
}
