package com.chinasoft.ctams.fragment.homePageFragment.schedule.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinasoft.ctams.R;
import com.chinasoft.ctams.base.BaseActivity;
import com.chinasoft.ctams.fragment.homePageFragment.schedule.bean.LeaderDailyBean;
import com.chinasoft.ctams.fragment.homePageFragment.schedule.bean.OfficeDailyBean;

import butterknife.Bind;
import butterknife.OnClick;

public class WorkArrangeDetailsActivity extends BaseActivity {

    @Bind(R.id.iv_title_back)
    ImageView iv_back;
    @Bind(R.id.tv_title)
    TextView tv_title;
    @Bind(R.id.tv_leaderSchedule_createTime)
    TextView tv_createTime;
    @Bind(R.id.tv_leaderPatrol_schedule_time)
    TextView tv_scheduleTime;
    @Bind(R.id.tv_leaderSchedule_founder)
    TextView tv_founder;
    @Bind(R.id.tv_leaderSchedule_jobType)
    TextView tv_jobType;
    @Bind(R.id.tv_leaderSchedule_leaderName)
    TextView tv_leaderName;
    @Bind(R.id.tv_leaderSchedule_organizaion)
    TextView tv_organizaion;
    @Bind(R.id.tv_leaderSchedule_arrange)
    TextView tv_arrange;
    @Bind(R.id.tv_leaderSchedule_modifierName)
    TextView tv_modifireName;

    @Override
    public int getLayoutId() {
        return R.layout.activity_work_arrange_details;
    }

    @Override
    public void initPage(Bundle savedInstanceState) {
        tv_title.setText("工作安排详情");
        Bundle bundle=getIntent().getExtras();
        int flag=bundle.getInt("flag");
        if (flag==1){
            LeaderDailyBean.ResultBean.ListBean bean= (LeaderDailyBean.ResultBean.ListBean) bundle.getSerializable("bean");
            tv_arrange.setText("工作安排: "+bean.getLeaderscheduleArrangement());
            tv_createTime.setText("创建时间:　"+bean.getLeaderscheduleCreatetimePage());
            tv_founder.setText("创建人："+bean.getLeaderscheduleFounder());
            tv_jobType.setText("职位类型："+bean.getLeaderscheduleJobtype());
            tv_leaderName.setText("领导姓名："+bean.getLeaderscheduleLeadername());
            tv_modifireName.setText("修改人："+bean.getLeaderscheduleModifiername());
            tv_organizaion.setText("行政区划："+bean.getOrganization());
            tv_scheduleTime.setText("时间："+bean.getLeaderscheduleDatePage());
        }else if (flag==2){
            OfficeDailyBean.ResultBean.ListBean bean= (OfficeDailyBean.ResultBean.ListBean) bundle.getSerializable("bean");
            tv_arrange.setText("工作安排: "+bean.getWorkscheduleArrangement());
            tv_createTime.setText("创建时间:　"+bean.getWorkscheduleCreatetimeTo());
            tv_founder.setText("创建人："+bean.getWorkscheduleFounder());
            tv_jobType.setText("职位类型："+bean.getWorkscheduleJobtype());
            tv_leaderName.setText("领导姓名："+bean.getWorkscheduleLeadername());
            tv_modifireName.setText("修改人："+bean.getWorkscheduleModifiernaem());
            tv_organizaion.setText("行政区划："+bean.getOrganization());
            tv_scheduleTime.setText("时间："+bean.getWorkscheduleDateTo());
        }

    }
    @OnClick(R.id.iv_title_back)
    public void onClick() {
        finish();
    }

}
