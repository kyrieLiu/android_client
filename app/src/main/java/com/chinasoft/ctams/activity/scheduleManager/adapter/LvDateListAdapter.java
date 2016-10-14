package com.chinasoft.ctams.activity.scheduleManager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinasoft.ctams.R;
import com.chinasoft.ctams.activity.scheduleManager.bean.DateListBean;

import java.util.List;

/**
 * Created by Administrator on 2016/6/23.
 */
public class LvDateListAdapter extends BaseAdapter {
    private Context context;
    private List<DateListBean> list;
    private LayoutInflater layoutInflater;

    public LvDateListAdapter(Context context, List<DateListBean> list) {
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
        LvDateListHolder lvDateListHolder=null;
        if (convertView==null){
            lvDateListHolder=new LvDateListHolder();
            convertView=layoutInflater.inflate(R.layout.item_lv_date_list,null);
            lvDateListHolder.title= (TextView) convertView.findViewById(R.id.tv_item_title_date_list);
            lvDateListHolder.startTime= (TextView) convertView.findViewById(R.id.tv_item_start_time_date_list);
            lvDateListHolder.endTime= (TextView) convertView.findViewById(R.id.tv_item_end_time_date_list);
            lvDateListHolder.organization= (TextView) convertView.findViewById(R.id.tv_item_duty_organize);
            convertView.setTag(lvDateListHolder);
        }else {
            lvDateListHolder= (LvDateListHolder) convertView.getTag();
        }
        lvDateListHolder.title.setText(list.get(position).getDateListName());
        lvDateListHolder.startTime.setText(list.get(position).getStartTime());
        lvDateListHolder.endTime.setText(list.get(position).getEndTime());
        lvDateListHolder.organization.setText(list.get(position).getOrganization());
        return convertView;
    }
    private class LvDateListHolder{
        TextView title,startTime,endTime,organization;
    }
}
