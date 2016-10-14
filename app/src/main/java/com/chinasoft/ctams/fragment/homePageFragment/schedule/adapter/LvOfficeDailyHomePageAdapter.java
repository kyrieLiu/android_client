package com.chinasoft.ctams.fragment.homePageFragment.schedule.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinasoft.ctams.R;
import com.chinasoft.ctams.fragment.homePageFragment.schedule.bean.OfficeDailyBean;

import java.util.List;

/**
 * Created by Administrator on 2016/6/17.
 */
public class LvOfficeDailyHomePageAdapter extends BaseAdapter {
    private Context context;
    private List<OfficeDailyBean.ResultBean.ListBean> list;
    private LayoutInflater layoutInflater;

    public LvOfficeDailyHomePageAdapter(Context context, List<OfficeDailyBean.ResultBean.ListBean> list) {
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
        LvOfficeDailyHomePageHolder lvOfficeDailyHomePageHolder=null;
        OfficeDailyBean.ResultBean.ListBean bean=list.get(position);
        if (convertView==null){
            lvOfficeDailyHomePageHolder=new LvOfficeDailyHomePageHolder();
            convertView=layoutInflater.inflate(R.layout.lv_item_work_schedule_home_page,null);
            lvOfficeDailyHomePageHolder.tv_officeTime= (TextView) convertView.findViewById(R.id.tv_work_schedule_date);
            lvOfficeDailyHomePageHolder.tv_officeName= (TextView) convertView.findViewById(R.id.tv_work_schedule_name);
            lvOfficeDailyHomePageHolder.tv_officeDetail= (TextView) convertView.findViewById(R.id.tv_work_schedule_content);
            convertView.setTag(lvOfficeDailyHomePageHolder);
        }else {
            lvOfficeDailyHomePageHolder= (LvOfficeDailyHomePageHolder) convertView.getTag();
        }
        lvOfficeDailyHomePageHolder.tv_officeTime.setText(bean.getWorkscheduleDateTo());
        lvOfficeDailyHomePageHolder.tv_officeName.setText(bean.getWorkscheduleLeadername());
        lvOfficeDailyHomePageHolder.tv_officeDetail.setText(bean.getWorkscheduleArrangement());
        return convertView;
    }
    private class LvOfficeDailyHomePageHolder{
        TextView tv_officeTime,tv_officeName,tv_officeDetail;
    }
}
