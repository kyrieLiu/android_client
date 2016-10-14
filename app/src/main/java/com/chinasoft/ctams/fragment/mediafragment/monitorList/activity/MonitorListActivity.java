package com.chinasoft.ctams.fragment.mediafragment.monitorList.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.chinasoft.ctams.R;
import com.chinasoft.ctams.base.BaseActivity;
import com.chinasoft.ctams.fragment.mediafragment.monitorList.adapter.LvMonitorVideoAdapter;
import com.chinasoft.ctams.fragment.mediafragment.monitorList.bean.MonitorBean;
import com.chinasoft.ctams.model.MyServerModel;
import com.chinasoft.ctams.util.ConstantCode;
import com.chinasoft.ctams.view.CustomProgressDialog;
import com.chinasoft.ctams.view.XListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by ls1213 on 2016/8/13.
 */

public class MonitorListActivity extends BaseActivity implements XListView.IXListViewListener {
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.lv_monitor_list_activity)
    XListView lv_monitorVideo;
    private LvMonitorVideoAdapter lvMonitorVideoAdapter;
    private List<MonitorBean.MonitorRowsBean> list;
    private CustomProgressDialog dialog;
    private int pageNo = 1;
    private String placeId="";

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            dialog.dismiss();
            lv_monitorVideo.stopLoadMore();
             if (msg.what == ConstantCode.POST_SUCCESS) {
                 pageNo++;
                List<MonitorBean.MonitorRowsBean> newList= (List<MonitorBean.MonitorRowsBean>) msg.obj;
                list.addAll(newList);
                 lvMonitorVideoAdapter.notifyDataSetChanged();
            } else if (msg.what == ConstantCode.DATA_IS_NULL) {
                Toast.makeText(MonitorListActivity.this, R.string.data_is_null, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MonitorListActivity.this, R.string.getDataFailed, Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.monitor_list_activity;
    }

    @Override
    public void initPage(Bundle savedInstanceState) {
        init();
        initData();
    }

    private void init() {
        tvTitle.setText("请选择监控");
        list = new ArrayList<>();
        placeId=getIntent().getStringExtra("placeId");
        lv_monitorVideo.setPullLoadEnable(true);
        lv_monitorVideo.setPullRefreshEnable(false);
        lv_monitorVideo.setXListViewListener(this);
        lvMonitorVideoAdapter = new LvMonitorVideoAdapter(MonitorListActivity.this, list);
        lv_monitorVideo.setAdapter(lvMonitorVideoAdapter);
        MyServerModel.getInstance(MonitorListActivity.this).loadMonitorList(handler,placeId,pageNo, MonitorListActivity.this);
        if (dialog == null) {
            dialog = CustomProgressDialog.createDialog(MonitorListActivity.this);
            dialog.setMessage("正在加载...");
        }
        dialog.show();
        listenListView();
    }

    private void initData() {
    }


    @OnClick({R.id.iv_title_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
        }
    }

    private void listenListView() {
        lv_monitorVideo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MonitorListActivity.this,"暂无监控可观看",Toast.LENGTH_SHORT).show();
//                MonitorBean.MonitorRowsBean monitorVideoListBean = (MonitorBean.MonitorRowsBean) lvMonitorVideoAdapter.getItem(position - 1);
//                String ip = monitorVideoListBean.getVideoUrl();
//                String username = monitorVideoListBean.getVideoUser();
//                String password = monitorVideoListBean.getVideoPass();
//                Intent intent = new Intent(MonitorListActivity.this, MonitorVideoPlayActivity.class);
//                intent.putExtra("ip", ip);
//                intent.putExtra("username", username);
//                intent.putExtra("password", password);
//                startActivity(intent);
            }
        });
    }


    @Override
    public void onRefresh() {
//        MyServerModel.getInstance(MonitorListActivity.this).loadMonitorList(handler,placeId, 1, MonitorListActivity.this);
    }

    @Override
    public void onLoadMore() {
//        pageNo++;
        MyServerModel.getInstance(MonitorListActivity.this).loadMonitorList(handler, placeId,pageNo, MonitorListActivity.this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dialog = null;
        MyServerModel.getInstance(MonitorListActivity.this).cancelTag(MonitorListActivity.this);
    }

}
