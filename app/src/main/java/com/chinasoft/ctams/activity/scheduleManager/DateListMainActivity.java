package com.chinasoft.ctams.activity.scheduleManager;

import android.content.Intent;
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
import com.chinasoft.ctams.model.ServerModel;
import com.chinasoft.ctams.base.BaseActivity;
import com.chinasoft.ctams.activity.scheduleManager.adapter.LvDateListAdapter;
import com.chinasoft.ctams.activity.scheduleManager.bean.DateListBean;
import com.chinasoft.ctams.activity.scheduleManager.fragment.DateExcelActivity;
import com.chinasoft.ctams.util.ConstantCode;
import com.chinasoft.ctams.view.CustomProgressDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/6/23.
 */
public class DateListMainActivity extends BaseActivity {
    private final String DATE_LIST = "schedul/listSchedulMobile";

    @Bind(R.id.lv_date_list_fragment_main)
    ListView lvDateListFragmentMain;
    @Bind(R.id.iv_title_back)
    ImageView iv_back;
    @Bind(R.id.tv_title)
    TextView tv_title;
    private List<DateListBean> dateListBeanList;
    private LvDateListAdapter lvDateListAdapter;

    private CustomProgressDialog dialog;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (dialog!=null){
                dialog.dismiss();
            }
            if (msg.what == ConstantCode.POST_SUCCESS) {
                lvDateListAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(DateListMainActivity.this, R.string.getDataFailed, Toast.LENGTH_SHORT).show();
            }
        }
    };


    @Override
    public int getLayoutId() {
        return R.layout.fragment_date_list_main;
    }

    @Override
    public void initPage(Bundle savedInstanceState) {
        tv_title.setText("排班列表");
        init();
        initData();
    }

    private void init() {
        dateListBeanList = new ArrayList<>();
        lvDateListAdapter = new LvDateListAdapter(this, dateListBeanList);
        lvDateListFragmentMain.setAdapter(lvDateListAdapter);
        if (dialog==null){
            dialog=CustomProgressDialog.createDialog(this);
            dialog.setMessage("正在加载...");
        }
        dialog.show();
        String page="1";
        ServerModel.getInstance(this).getDateListData(handler, DATE_LIST, dateListBeanList,page,this);
    }

    private void initData() {

        lvDateListFragmentMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(DateListMainActivity.this, DateExcelActivity.class);
                intent.putExtra("id", dateListBeanList.get(position).getDateListId());
                startActivity(intent);
            }
        });
    }

    @OnClick(R.id.iv_title_back)
    public void onClick() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ServerModel.getInstance(this).cancelTag(this);
    }
}
