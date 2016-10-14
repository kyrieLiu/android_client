package com.chinasoft.ctams.fragment.homePageFragment.schedule.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chinasoft.ctams.R;
import com.chinasoft.ctams.base.BaseFragment;
import com.chinasoft.ctams.fragment.homePageFragment.schedule.activity.LeaderWorkScheduleListActivity;
import com.chinasoft.ctams.fragment.homePageFragment.schedule.activity.WorkArrangeDetailsActivity;
import com.chinasoft.ctams.fragment.homePageFragment.schedule.adapter.LvLeaderDailyHomePageAdapter;
import com.chinasoft.ctams.fragment.homePageFragment.schedule.bean.LeaderDailyBean;
import com.chinasoft.ctams.model.MyServerModel;
import com.chinasoft.ctams.util.ConstantCode;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/6/17.
 */
public class DailyLeaderFragment extends BaseFragment {

    @Bind(R.id.lv_leader_daily_home_page)
    ListView listView;
    @Bind(R.id.tv_leader_plan_click_to_see_more)
    TextView tv_see_more;
    @Bind(R.id.fl_leaderSchedule_load)
    FrameLayout frameLayout;
    private LvLeaderDailyHomePageAdapter adapter;
    private List<LeaderDailyBean.ResultBean.ListBean> list;

    private LeaderDailyBean leaderDailyBean;
    private boolean isDestroy=false;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            frameLayout.setVisibility(View.GONE);

            switch (msg.what){
                case ConstantCode.POST_FAILED:
                    Toast.makeText(getActivity(),"加载失败,请检查网络!",Toast.LENGTH_SHORT).show();
                    break;
                case ConstantCode.POST_SUCCESS:
                    leaderDailyBean= (LeaderDailyBean) msg.obj;
                    list=leaderDailyBean.getResult().getList();
                    if (!isDestroy){
                        adapter = new LvLeaderDailyHomePageAdapter(getActivity(), list);
                        listView.setAdapter(adapter);
                    }
                    break;
            }
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.fragment_leader_daily_homepage;
    }

    @Override
    public void initPage(Bundle savedInstanceState) {

            MyServerModel.getInstance(getActivity()).getLeaderWorkSchedule(handler,1,getActivity());
            listenListView();
    }

    @OnClick(R.id.tv_leader_plan_click_to_see_more)
    public void onClick() {
        Intent intent=new Intent(getActivity(), LeaderWorkScheduleListActivity.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("bean",leaderDailyBean);
        bundle.putString("title","领导工作安排");
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * 监听ListView
     */
    private void listenListView(){

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LeaderDailyBean.ResultBean.ListBean bean= (LeaderDailyBean.ResultBean.ListBean) adapter.getItem(position);
                Intent intent=new Intent(getActivity(),WorkArrangeDetailsActivity.class);
                Bundle bundle=new Bundle();
                bundle.putInt("flag",1);
                bundle.putSerializable("bean",bean);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }


    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isDestroy=true;
        MyServerModel.getInstance(getActivity()).cancelTag(getActivity());

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
