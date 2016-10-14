package com.chinasoft.ctams.activity.task.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinasoft.ctams.R;
import com.chinasoft.ctams.activity.task.bean.TaskListBean;

import java.util.List;

/**
 * Created by ls1213 on 2016/8/10.
 */

public class LvTaskThingListAdapter extends BaseAdapter {
    private Context context;
    private List<TaskListBean> list;
    private LayoutInflater layoutInflater;

    public LvTaskThingListAdapter(Context context, List<TaskListBean> list) {
        this.context = context;
        this.list = list;
        layoutInflater=LayoutInflater.from(context);
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
        LvTaskThingListHolder lvTaskThingListHolder=null;
        if (convertView==null){
            lvTaskThingListHolder=new LvTaskThingListHolder();
            convertView=layoutInflater.inflate(R.layout.item_lv_task_thing_list,null);
            lvTaskThingListHolder.title= (TextView) convertView.findViewById(R.id.tv_item_task_title_task_list);
            lvTaskThingListHolder.introduce = (TextView) convertView.findViewById(R.id.tv_item_task_introduce_task_list);
            lvTaskThingListHolder.space= (TextView) convertView.findViewById(R.id.tv_item_task_space_task_list);
            lvTaskThingListHolder.date= (TextView) convertView.findViewById(R.id.tv_item_task_date_task_list);
            lvTaskThingListHolder.iv_type=(ImageView)convertView.findViewById(R.id.iv_item_task_type);
            convertView.setTag(lvTaskThingListHolder);
        }else {
            lvTaskThingListHolder= (LvTaskThingListHolder) convertView.getTag();
        }

        if (list.get(position).getThingType().equals("暴恐")){
            lvTaskThingListHolder.iv_type.setImageResource(R.mipmap.type_terror);
        }else if (list.get(position).getThingType().equals("火灾")){
            lvTaskThingListHolder.iv_type.setImageResource(R.mipmap.type_fire);
        }else if (list.get(position).getThingType().equals("交通事故")){
            lvTaskThingListHolder.iv_type.setImageResource(R.mipmap.type_traffic_logo);
        }else {
            lvTaskThingListHolder.iv_type.setImageResource(R.mipmap.type_unlawful_assembly);
        }
        lvTaskThingListHolder.title.setText(list.get(position).getThingName());
        lvTaskThingListHolder.introduce.setText(list.get(position).getThingIntroduction());
        lvTaskThingListHolder.space.setText(list.get(position).getThingMechanism());
        lvTaskThingListHolder.date.setText(list.get(position).getThingDate());
        return convertView;
    }

    private static class LvTaskThingListHolder{
        TextView title, introduce,space,date;
        ImageView iv_type;
    }
}
