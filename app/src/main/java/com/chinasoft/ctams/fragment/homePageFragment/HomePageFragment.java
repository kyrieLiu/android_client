package com.chinasoft.ctams.fragment.homePageFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.chinasoft.ctams.R;
import com.chinasoft.ctams.activity.addresssBook.AllAddressBookActivity;
import com.chinasoft.ctams.activity.chat.FriendsListActivity;
import com.chinasoft.ctams.activity.map.MapActivity;
import com.chinasoft.ctams.activity.patrol.PatrolMainActivity;
import com.chinasoft.ctams.activity.sendPaper.SendPaperMainActivity;
import com.chinasoft.ctams.activity.sendPaper.SendThingActivity;
import com.chinasoft.ctams.activity.task.activity.TaskThingsListActivity;
import com.chinasoft.ctams.adapter.GvFuntionsAdapter;
import com.chinasoft.ctams.base.BaseFragment;
import com.chinasoft.ctams.bean.DailyPlan;
import com.chinasoft.ctams.view.MyGirdView;
import com.daniulive.smartpublisher.CameraPublishActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Administrator on 2016/6/16.
 */
public class HomePageFragment extends BaseFragment {

    private static HomePageFragment fragment;
    @Bind(R.id.gv_home_page_funtions)
    MyGirdView gvHomePageFuntions;
    @Bind(R.id.ll_daily_homepage)
    LinearLayout dailyFragmentContainer;

    private GvFuntionsAdapter gvFuntionsAdapter;
    private int[] functionIcons = {R.mipmap.icon_sechedule,R.mipmap.sendpaper_gv_home_icon,R.mipmap.map_gv_home_page_icon,
           R.mipmap.all_select_gv_home_page_icon,R.mipmap.video_update_gv_home_page_icon,R.mipmap.patrol_gv_home_page_icon,R.mipmap.icon_address, R.mipmap.attention_push_gv_home_page_icon,};
    private String[] functionNames = {"任务事件", "信息报送", "电子地图", "实时上报", "视频回传", "巡逻计划", "通讯录", "好友列表"};
    private List<DailyPlan> dailyPlanList;

    public static HomePageFragment getInstance() {
        if (fragment == null) {
            fragment = new HomePageFragment();
        }
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_homepage_main;
    }

    @Override
    public void initPage(Bundle savedInstanceState) {
        init();
        initData();
    }


    private void init() {
        gvFuntionsAdapter = new GvFuntionsAdapter(getContext(), functionIcons, functionNames);
        gvHomePageFuntions.setAdapter(gvFuntionsAdapter);
        dailyPlanList = new ArrayList<>();
        setDailyPlanList();
    }
    private void initData(){
        gvHomePageFuntions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            Intent intent;
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        openActivity(TaskThingsListActivity.class);
                        break;
                    case 1:
                        openActivity(SendPaperMainActivity.class);
                        break;
                    case 2:
                       openActivity(MapActivity.class);
                        break;
                    case 3:
                       openActivity(SendThingActivity.class);
                        break;
                    case 4:
                        openActivity(CameraPublishActivity.class);
                        break;
                    case 5:
                        openActivity(PatrolMainActivity.class);
                        break;
                    case 6:
                        intent=new Intent(getActivity(),AllAddressBookActivity.class);
                        intent.putExtra("flag","normal");
                        startActivity(intent);
                        break;
                    case 7:
                        gvFuntionsAdapter.mark=1;
                        gvFuntionsAdapter.notifyDataSetChanged();
                        openActivity(FriendsListActivity.class);

                        break;

                }
            }
        });
    }
    private void openActivity(Class clz){
        startActivity(new Intent(getActivity(), clz));
    }


    private void setDailyPlanList() {
        for (int i = 0; i <= 20; i++) {
            DailyPlan dailyPlan = new DailyPlan("这是第" + i + "个标题");
            dailyPlanList.add(dailyPlan);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==0){
            gvFuntionsAdapter = new GvFuntionsAdapter(getContext(), functionIcons, functionNames);
            gvHomePageFuntions.setAdapter(gvFuntionsAdapter);
        }
    }
}
