package com.chinasoft.ctams.activity.patrol;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinasoft.ctams.R;
import com.chinasoft.ctams.activity.patrol.bean.PatrolPlanBean;
import com.chinasoft.ctams.activity.patrol.video.VideoUploadActivity;
import com.chinasoft.ctams.base.BaseActivity;
import com.chinasoft.ctams.model.MyServerModel;
import com.chinasoft.ctams.util.ConstantCode;
import com.chinasoft.ctams.util.SharedPreferencesHelper;
import com.chinasoft.ctams.view.CustomProgressDialog;

import java.io.Serializable;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Kyrie on 2016/6/28.
 * Email:kyrie_liu@sina.com
 */
public class PatrolMainActivity extends BaseActivity implements View.OnClickListener {


    @Bind(R.id.iv_title_back)
    ImageView iv_back;
    @Bind(R.id.tv_title)
    TextView tv_title;
    @Bind(R.id.tv_head_menu)
    TextView tv_menu;
    @Bind(R.id.tv_patrol_teamName)
    TextView tv_patrolTeamName;
    @Bind(R.id.tv_patrol_teamLeader)
    TextView tv_patrolTeamLeader;
    @Bind(R.id.tv_patrol_team_contactPhone)
    TextView tv_patrolTeamContactPhone;
    @Bind(R.id.tv_patrol_teamArea)
    TextView tv_patrolTeamArea;
    @Bind(R.id.tv_patrol_teamBuidtime)
    TextView tv_patrolTeamBuidtime;
    @Bind(R.id.tv_patrol_planName)
    TextView tv_patrolPlanName;
    @Bind(R.id.tv_patrol_routeName)
    TextView tv_patrolRouteName;
    @Bind(R.id.tv_patrol_periodic)
    TextView tv_patrolPeriodic;
    @Bind(R.id.tv_patrol_remark)
    TextView tv_patrolRemark;
    @Bind(R.id.rl_patrol_plan)
    RelativeLayout rlPatrolPlan;
    @Bind(R.id.tv_patrol_place)
    TextView tv_place;
    @Bind(R.id.ll_patrol_main)
    LinearLayout ll_patrol_main;
    private CustomProgressDialog dialog;
    private PopupWindow popupWindow;
    private int height;
    private PatrolPlanBean patrolPlanBean;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (dialog!=null){
                dialog.dismiss();
            }
            switch (msg.what) {
                case ConstantCode.POST_SUCCESS:
                    patrolPlanBean = (PatrolPlanBean) msg.obj;
                    tv_patrolTeamArea.setText("行政区域: " + patrolPlanBean.getPatteamArea());
                    tv_patrolTeamLeader.setText("队长: " + patrolPlanBean.getPatteamCaptain());
                    tv_patrolPlanName.setText("计划名称: " + patrolPlanBean.getPlanName());
                    tv_patrolTeamName.setText("队伍名称: " + patrolPlanBean.getPatteamName());
                    tv_patrolTeamContactPhone.setText(patrolPlanBean.getPatteamContact());
                    tv_patrolTeamBuidtime.setText("创建时间：" + patrolPlanBean.getPatteamBuildtime());
                    tv_patrolRemark.setText("备注:　" + patrolPlanBean.getPatteamRemarks());
                    tv_patrolRouteName.setText("路线名称:　" + patrolPlanBean.getRouteName());
                    tv_patrolPeriodic.setText("巡逻周期: " + patrolPlanBean.getPlanEstimate());
                    List<String> arrPlace= patrolPlanBean.getTrajpointList();
                    StringBuilder builder=new StringBuilder();
                    for (int i=0;i<arrPlace.size();i++){
                        if (i<arrPlace.size()-1){
                            builder.append(arrPlace.get(i)+" → ");
                        }else{
                            builder.append(arrPlace.get(i));
                        }
                    }
                    tv_place.setText(builder);
                    break;
            }
        }
    };


    @Override
    public int getLayoutId() {
        return R.layout.activity_patrol_plan;
    }

    @Override
    public void initPage(Bundle savedInstanceState) {
        tv_title.setText("巡逻详情");
        tv_menu.setVisibility(View.VISIBLE);
        tv_menu.setBackgroundResource(R.mipmap.icon_right_popup_menu);
        initPopupWindow();
        loadData();




    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @OnClick({R.id.iv_title_back, R.id.tv_head_menu})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.tv_head_menu:
                int[] location = new int[2];
                rlPatrolPlan  .getLocationInWindow(location);
                int x = location[0];
                int y = location[1];
                popupWindow.showAtLocation(rlPatrolPlan, Gravity.RIGHT | Gravity.TOP, 0,y);
                break;
            //开始巡逻
            case R.id.tv_popup_item1:
                turnPatrolProcess(1);
                break;
            //过程上报
            case R.id.tv_popup_item2:
              turnPatrolProcess(2);
                break;
            //巡逻反馈
            case R.id.tv_popup_item3:
                intent = new Intent(PatrolMainActivity.this, PatrolFeedBackActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("routeName", patrolPlanBean.getRouteName());
                bundle.putString("teamName", patrolPlanBean.getPatteamName());
                bundle.putString("planName", patrolPlanBean.getPlanName());
                bundle.putString("planId",patrolPlanBean.getPlanId());
                bundle.putString("processTeamId",patrolPlanBean.getPatteamId());
                bundle.putString("processRouteId",patrolPlanBean.getRolroteID());
                bundle.putInt("num",patrolPlanBean.getNum());
                intent.putExtras(bundle);
                startActivity(intent);
                popupWindow.dismiss();
                break;
            //路线查看
            case R.id.tv_popup_item4:
                intent = new Intent(PatrolMainActivity.this, RoutePatrolActivity.class);
                String routeUrl = patrolPlanBean.getRolroteID();
                String port= SharedPreferencesHelper.getInstance().getPort(this);
                intent.putExtra("routeUrl", ":"+port+"/ctams/mapJsp/map/drawPath.jsp?id=" + routeUrl);
                startActivity(intent);
                popupWindow.dismiss();
                break;
            //巡逻结束
            case R.id.tv_popup_item5:
                turnPatrolProcess(5);
                break;
            //视频上传
            case R.id.tv_popup_item6:
                intent=new Intent(PatrolMainActivity.this, VideoUploadActivity.class);
                startActivity(intent);
                popupWindow.dismiss();
                break;
        }
    }

    private void initPopupWindow() {
        View popupView = LayoutInflater.from(PatrolMainActivity.this).inflate(R.layout.popup_view, null);
        popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.bg_black));
        TextView tv_patrol_start = (TextView) popupView.findViewById(R.id.tv_popup_item1);
        TextView tv_process = (TextView) popupView.findViewById(R.id.tv_popup_item2);
        TextView tv_feedBack = (TextView) popupView.findViewById(R.id.tv_popup_item3);
        TextView tv_route=(TextView)popupView.findViewById(R.id.tv_popup_item4);
        TextView tv_patrol_end=(TextView)popupView.findViewById(R.id.tv_popup_item5);
        TextView tv_patrol_video=(TextView)popupView.findViewById(R.id.tv_popup_item6);
        tv_patrol_end.setVisibility(View.VISIBLE);
        tv_feedBack.setVisibility(View.VISIBLE);
        tv_route.setVisibility(View.VISIBLE);
        tv_patrol_video.setVisibility(View.VISIBLE);

        LinearLayout linearLayout = (LinearLayout) popupView.findViewById(R.id.ll_popup);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
        params.height = 940;
        linearLayout.setLayoutParams(params);
        tv_process.setOnClickListener(this);
        tv_feedBack.setOnClickListener(this);
        tv_route.setOnClickListener(this);
        tv_patrol_start.setOnClickListener(this);
        tv_patrol_end.setOnClickListener(this);
        tv_patrol_video.setOnClickListener(this);
    }
    private void turnPatrolProcess(int flag){
        Intent intent=new Intent(this,PatrolProcessSendUpActivity.class);
        Bundle bundle=new Bundle();
        bundle.putString("routeName", patrolPlanBean.getRouteName());
        bundle.putString("teamName", patrolPlanBean.getPatteamName());
        bundle.putString("planName", patrolPlanBean.getPlanName());
        bundle.putString("planId",patrolPlanBean.getPlanId());
        bundle.putString("processTeamId",patrolPlanBean.getPatteamId());
        bundle.putString("processRouteId",patrolPlanBean.getRolroteID());
        bundle.putInt("num",patrolPlanBean.getNum());
        bundle.putSerializable("list", (Serializable) patrolPlanBean.getTrajpointList());
        bundle.putInt("flag",flag);
        intent.putExtras(bundle);
        if(flag==1){
            startActivityForResult(intent,1);
        }else{
            startActivity(intent);
        }

        popupWindow.dismiss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1){
            if (resultCode==1){
                loadData();
            }
        }

    }
    private void loadData(){
        MyServerModel.getInstance(this).loadMainPatrol(handler, this);
        if (dialog == null) {
            dialog = CustomProgressDialog.createDialog(this);
            dialog.setMessage("正在加载..");
        }
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dialog=null;
        MyServerModel.getInstance(this).cancelTag(this);
    }
}

