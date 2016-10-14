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
import com.chinasoft.ctams.fragment.homePageFragment.schedule.adapter.OfficeWorkScheduleAdapter;
import com.chinasoft.ctams.fragment.homePageFragment.schedule.bean.OfficeDailyBean;
import com.chinasoft.ctams.model.MyServerModel;
import com.chinasoft.ctams.util.ConstantCode;
import com.chinasoft.ctams.view.XListView;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class OfficeWorkScheduleListActivity extends BaseActivity implements XListView.IXListViewListener{

    @Bind(R.id.iv_title_back)
    ImageView iv_back;
    @Bind(R.id.tv_title)
    TextView tv_title;
    @Bind(R.id.lv_work_schedule)
    XListView listView;

    private OfficeWorkScheduleAdapter adapter;
    private List<OfficeDailyBean.ResultBean.ListBean> list;
    private OfficeDailyBean officeDailyBean;

    private int pageNo=2;


    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            listView.stopLoadMore();
            switch (msg.what){
                case ConstantCode.POST_FAILED:
                    Toast.makeText(OfficeWorkScheduleListActivity.this,"加载失败,请检查网络!",Toast.LENGTH_SHORT).show();
                    break;
                case ConstantCode.POST_SUCCESS:
                    pageNo++;
                    OfficeDailyBean bean= (OfficeDailyBean) msg.obj;
                    List<OfficeDailyBean.ResultBean.ListBean> newList=bean.getResult().getList();
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
        officeDailyBean = (OfficeDailyBean) bundle.getSerializable("bean");
        list = officeDailyBean.getResult().getList();
        adapter=new OfficeWorkScheduleAdapter(this, list);
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
        MyServerModel.getInstance(this).getOfficeWorkData(handler,pageNo,this);
    }

    private void listenListView(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OfficeDailyBean.ResultBean.ListBean bean= (OfficeDailyBean.ResultBean.ListBean) adapter.getItem(position-1);
                Intent intent=new Intent(OfficeWorkScheduleListActivity.this,WorkArrangeDetailsActivity.class);
                Bundle bundle=new Bundle();
                bundle.putInt("flag",2);
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
