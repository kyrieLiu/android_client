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
 * Created by Kyrie on 2016/8/8.
 * Email:kyrie_liu@sina.com
 */
public class OfficeWorkScheduleAdapter extends BaseAdapter {
    private List<OfficeDailyBean.ResultBean.ListBean> list;
    private Context context;
    public OfficeWorkScheduleAdapter(Context context, List<OfficeDailyBean.ResultBean.ListBean> list){
        this.context=context;
        this.list=list;
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
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        OfficeDailyBean.ResultBean.ListBean bean=list.get(position);
        if (convertView==null){
            viewHolder=new ViewHolder();
            convertView= LayoutInflater.from(context).inflate(R.layout.item_activity_work_schedule,null);
            viewHolder.tv_name=(TextView)convertView.findViewById(R.id.tv_item_work_schedlue_leader_name);
            viewHolder.tv_time=(TextView)convertView.findViewById(R.id.tv_item_work_schedlue_time);
            viewHolder.tv_arragement=(TextView)convertView.findViewById(R.id.tv_item_work_schedlue_arrangement);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_name.setText(bean.getWorkscheduleLeadername());
        viewHolder.tv_time.setText(bean.getWorkscheduleDateTo());
        viewHolder.tv_arragement.setText(bean.getWorkscheduleArrangement());
        return convertView;
    }
    private class ViewHolder{
        private TextView tv_name,tv_time,tv_arragement;
    }
}
