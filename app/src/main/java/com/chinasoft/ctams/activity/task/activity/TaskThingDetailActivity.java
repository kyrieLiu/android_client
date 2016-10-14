package com.chinasoft.ctams.activity.task.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chinasoft.ctams.R;
import com.chinasoft.ctams.activity.task.adapter.LvItemTaskThingDetailAdapter;
import com.chinasoft.ctams.activity.task.bean.TaskItemBean;
import com.chinasoft.ctams.activity.task.bean.TaskListBean;
import com.chinasoft.ctams.base.BaseActivity;
import com.chinasoft.ctams.model.ServerModel;
import com.chinasoft.ctams.util.ConstantCode;
import com.chinasoft.ctams.view.MyListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by ls1213 on 2016/8/10.
 */

public class TaskThingDetailActivity extends BaseActivity {

    private final String GET_TASK_DETAIL_ITEM_LIST="infomerge/showInforMationevent";

    @Bind(R.id.iv_title_back)
    ImageView ivTitleBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_title_task_thing_parent_detail)
    TextView tvTitleTaskThingParentDetail;
    @Bind(R.id.tv_type_task_thing_parent_detail)
    TextView tvTypeTaskThingParentDetail;
    @Bind(R.id.tv_organization_task_thing_parent_detail)
    TextView tvOrganizationTaskThingParentDetail;
    @Bind(R.id.tv_time_task_thing_parent_detail)
    TextView tvTimeTaskThingParentDetail;
    @Bind(R.id.tv_introduction_task_thing_parent_detail)
    TextView tvIntroductionTaskThingParentDetail;
    @Bind(R.id.lv_son_task_thing_detail)
    MyListView lvSonTaskThingDetail;
    private TaskListBean taskListBean;
    private List<TaskItemBean> taskItemBeanList;
    private LvItemTaskThingDetailAdapter lvItemTaskThingDetailAdapter;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what== ConstantCode.POST_SUCCESS){
                taskItemBeanList.addAll((List<TaskItemBean>) msg.obj);
                lvItemTaskThingDetailAdapter=new LvItemTaskThingDetailAdapter(TaskThingDetailActivity.this,taskItemBeanList);
                lvSonTaskThingDetail.setAdapter(lvItemTaskThingDetailAdapter);
            }else {
                Toast.makeText(TaskThingDetailActivity.this,R.string.getDataFailed,Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_task_thing_detail;
    }

    @Override
    public void initPage(Bundle savedInstanceState) {
        init();
    }

    private void init(){
        tvTitle.setText("任务事件详情");
        taskListBean= (TaskListBean) getIntent().getSerializableExtra("parentBean");
        setParentData(taskListBean);
        taskItemBeanList=new ArrayList<>();
        ServerModel.getInstance(this).getTaskDetailItemList(handler,GET_TASK_DETAIL_ITEM_LIST,taskListBean.getThingId(),this);
    }

    private void setParentData(TaskListBean taskListBean){
        tvIntroductionTaskThingParentDetail.setText(taskListBean.getThingIntroduction());
        tvOrganizationTaskThingParentDetail.setText(taskListBean.getThingMechanism());
        tvTimeTaskThingParentDetail.setText(taskListBean.getThingDate());
        tvTitleTaskThingParentDetail.setText(taskListBean.getThingName());
        tvTypeTaskThingParentDetail.setText(taskListBean.getThingType());
    }
    @OnClick(R.id.iv_title_back)
    public void onClick() {
        finish();
    }

    @Override
    protected void onDestroy() {
        ServerModel.getInstance(this).cancelTag(this);
        super.onDestroy();
    }
}
