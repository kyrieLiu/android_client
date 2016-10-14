package com.chinasoft.ctams.fragment.homePageFragment.schedule.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chinasoft.ctams.R;
import com.chinasoft.ctams.base.BaseActivity;
import com.chinasoft.ctams.fragment.homePageFragment.schedule.adapter.LeaderWorkScheduleAdapter;
import com.chinasoft.ctams.fragment.homePageFragment.schedule.bean.LeaderDailyBean;
import com.chinasoft.ctams.model.MyServerModel;
import com.chinasoft.ctams.util.ConstantCode;
import com.chinasoft.ctams.view.XListView;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class LeaderWorkScheduleListActivity extends BaseActivity implements XListView.IXListViewListener{

    @Bind(R.id.iv_title_back)
    ImageView iv_back;
    @Bind(R.id.tv_title)
    TextView tv_title;
    @Bind(R.id.lv_work_schedule)
    XListView listView;

    private LeaderWorkScheduleAdapter adapter;
    private List<LeaderDailyBean.ResultBean.ListBean> list;
    private LeaderDailyBean leaderDailyBean;

    private int pageNo=2;


    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            listView.stopLoadMore();
            switch (msg.what){
                case ConstantCode.POST_FAILED:
                    Toast.makeText(LeaderWorkScheduleListActivity.this,"加载失败,请检查网络!",Toast.LENGTH_SHORT).show();
                    break;
                case ConstantCode.POST_SUCCESS:
                    pageNo++;
                    LeaderDailyBean bean= (LeaderDailyBean) msg.obj;
                    List<LeaderDailyBean.ResultBean.ListBean> newList=bean.getResult().getList();
                    list.addAll(newList);
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    };


    @Override
    public int getLayoutId() {
        return R.layout.activity_work_schedule_details;
    }

    @Override
    public void initPage(Bundle savedInstanceState) {
        listView.setPullLoadEnable(true);
        listView.setPullRefreshEnable(false);
        listView.setXListViewListener(this);
        Bundle bundle=getIntent().getExtras();
        String title=bundle.getString("title");
        tv_title.setText(title);
        leaderDailyBean = (LeaderDailyBean) bundle.getSerializable("bean");
        list = leaderDailyBean.getResult().getList();
        adapter=new LeaderWorkScheduleAdapter(this, list);
        listView.setAdapter(adapter);
        listenListView();
    }
    @OnClick(R.id.iv_title_back)
    public void onClick() {
        finish();
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {
        MyServerModel.getInstance(this).getLeaderWorkSchedule(handler,pageNo,this);
    }

    private void listenListView(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LeaderDailyBean.ResultBean.ListBean bean= (LeaderDailyBean.ResultBean.ListBean) adapter.getItem(position-1);
                Intent intent=new Intent(LeaderWorkScheduleListActivity.this,WorkArrangeDetailsActivity.class);
                Bundle bundle=new Bundle();
                bundle.putInt("flag",1);
                bundle.putSerializable("bean",bean);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyServerModel.getInstance(this).cancelTag(this);
    }
}
