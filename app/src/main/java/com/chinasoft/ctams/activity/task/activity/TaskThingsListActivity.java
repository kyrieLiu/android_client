package com.chinasoft.ctams.activity.task.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.chinasoft.ctams.R;
import com.chinasoft.ctams.activity.task.adapter.LvTaskThingListAdapter;
import com.chinasoft.ctams.activity.task.bean.TaskListBean;
import com.chinasoft.ctams.base.BaseActivity;
import com.chinasoft.ctams.model.ServerModel;
import com.chinasoft.ctams.util.ConstantCode;
import com.chinasoft.ctams.util.SharedPreferencesHelper;
import com.chinasoft.ctams.view.CustomProgressDialog;
import com.chinasoft.ctams.view.XListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by ls1213 on 2016/8/10.
 */

public class TaskThingsListActivity extends BaseActivity {

    private final String GET_TASK_LIST = "infomerge/listInformationevent";

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.lv_task_thing_list_activity)
    XListView lvTaskThingLv;
    private List<TaskListBean> taskListBeanList;
    private LvTaskThingListAdapter lvTaskThingListAdapter;
    private int pageNo = 1;
    private CustomProgressDialog dialog;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (lvTaskThingLv != null)
                lvTaskThingLv.stopLoadMore();
            if (dialog != null) {
                dialog.dismiss();
            }
            if (msg.what == ConstantCode.POST_SUCCESS) {
                pageNo++;
                taskListBeanList.addAll((List<TaskListBean>) msg.obj);
                lvTaskThingListAdapter.notifyDataSetChanged();
                Toast.makeText(TaskThingsListActivity.this, "加载成功！", Toast.LENGTH_SHORT).show();
            }else if (msg.what==ConstantCode.POST_FAILED){
                Toast.makeText(TaskThingsListActivity.this, "加载失败,请检查网络!", Toast.LENGTH_SHORT).show();
            }
        }
    };
    private String organizationId;

    @Override
    public int getLayoutId() {
        return R.layout.activity_task_thing;
    }

    @Override
    public void initPage(Bundle savedInstanceState) {
        init();
        initData();
    }

    private void init() {
        tvTitle.setText("任务事件列表");
        taskListBeanList = new ArrayList<>();
        lvTaskThingListAdapter = new LvTaskThingListAdapter(this, taskListBeanList);
        lvTaskThingLv.setAdapter(lvTaskThingListAdapter);
        lvTaskThingLv.setPullRefreshEnable(false);
        lvTaskThingLv.setPullLoadEnable(true);
        organizationId = SharedPreferencesHelper.getInstance().getOrganizationId(this);

        ServerModel.getInstance(this).getTaskThingList(handler, GET_TASK_LIST, 1, organizationId, this);
        if (dialog==null){
            dialog=CustomProgressDialog.createDialog(this);
            dialog.setMessage("请稍后...");
        }
        dialog.show();
    }

    private void initData() {
        lvTaskThingLv.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
            }

            @Override
            public void onLoadMore() {

                ServerModel.getInstance(TaskThingsListActivity.this).getTaskThingList(handler, GET_TASK_LIST, pageNo,organizationId, TaskThingsListActivity.this);
            }
        });
        lvTaskThingLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(TaskThingsListActivity.this, TaskThingDetailActivity.class);
                intent.putExtra("parentBean",taskListBeanList.get(position-1));
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onDestroy() {
        ServerModel.getInstance(this).cancelTag(this);
        handler.removeMessages(0);
        dialog=null;
        super.onDestroy();
    }


    @OnClick({R.id.iv_title_back, R.id.tv_head_menu})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;

        }
    }
}
