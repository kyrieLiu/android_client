package com.chinasoft.ctams.fragment.homePageFragment.schedule.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinasoft.ctams.R;
import com.chinasoft.ctams.fragment.homePageFragment.schedule.bean.LeaderDailyBean;

import java.util.List;

/**
 * Created by Administrator on 2016/6/17.
 */
public class LvLeaderDailyHomePageAdapter extends BaseAdapter {
    private Context context;
    private List<LeaderDailyBean.ResultBean.ListBean> list;
    private LayoutInflater layoutInflater;

    public LvLeaderDailyHomePageAdapter(Context context, List<LeaderDailyBean.ResultBean.ListBean> list) {
        this.context = context;
        this.list = list;
        this.layoutInflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LvLeaderDailyHomePageHolder lvLeaderDailyHomePageHolder=null;
        LeaderDailyBean.ResultBean.ListBean bean=list.get(position);
        if (convertView==null){
            lvLeaderDailyHomePageHolder=new LvLeaderDailyHomePageHolder();
            convertView=layoutInflater.inflate(R.layout.lv_item_work_schedule_home_page,null);
            lvLeaderDailyHomePageHolder.tv_leaderTime= (TextView) convertView.findViewById(R.id.tv_work_schedule_date);
            lvLeaderDailyHomePageHolder.tv_leaderMan= (TextView) convertView.findViewById(R.id.tv_work_schedule_name);
            lvLeaderDailyHomePageHolder.tv_leaderDetail= (TextView) convertView.findViewById(R.id.tv_work_schedule_content);
            convertView.setTag(lvLeaderDailyHomePageHolder);
        }else {
            lvLeaderDailyHomePageHolder= (LvLeaderDailyHomePageHolder) convertView.getTag();
        }
        lvLeaderDailyHomePageHolder.tv_leaderTime.setText(bean.getLeaderscheduleDatePage());
        lvLeaderDailyHomePageHolder.tv_leaderMan.setText(bean.getLeaderscheduleLeadername());
        lvLeaderDailyHomePageHolder.tv_leaderDetail.setText(bean.getLeaderscheduleArrangement());
        return convertView;
    }
    private class LvLeaderDailyHomePageHolder{
        TextView tv_leaderTime,tv_leaderMan,tv_leaderDetail;
    }
}
