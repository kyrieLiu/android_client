package com.chinasoft.ctams.activity.task.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinasoft.ctams.R;
import com.chinasoft.ctams.activity.task.bean.TaskItemBean;
import com.chinasoft.ctams.activity.task.bean.TaskListBean;

import java.util.List;

/**
 * Created by ls1213 on 2016/8/10.
 */

public class LvItemTaskThingDetailAdapter extends BaseAdapter {
    private Context context;
    private List<TaskItemBean> list;
    private LayoutInflater layoutInflater;

    public LvItemTaskThingDetailAdapter(Context context, List<TaskItemBean> list) {
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
        LvTaskThingDetailHolder lvTaskThingDetailHolder=null;
        if (convertView==null){
            lvTaskThingDetailHolder=new LvTaskThingDetailHolder();
            convertView=layoutInflater.inflate(R.layout.item_lv_item_task_thing_detail_list,null);
            lvTaskThingDetailHolder.title= (TextView) convertView.findViewById(R.id.tv_item_task_detail_title_task_list);
            lvTaskThingDetailHolder.type= (TextView) convertView.findViewById(R.id.tv_item_task_detail_type_task_list);
            lvTaskThingDetailHolder.man= (TextView) convertView.findViewById(R.id.tv_item_task_detail_man_task_list);
            lvTaskThingDetailHolder.date= (TextView) convertView.findViewById(R.id.tv_item_task_detail_date_task_list);
            lvTaskThingDetailHolder.content= (TextView) convertView.findViewById(R.id.tv_item_task_detail_content_task_list);
            convertView.setTag(lvTaskThingDetailHolder);
        }else {
            lvTaskThingDetailHolder= (LvTaskThingDetailHolder) convertView.getTag();
        }
        lvTaskThingDetailHolder.title.setText(list.get(position).getItemTaskTitle());
        if (list.get(position).getItemTaskType().equals("暴恐")){
            lvTaskThingDetailHolder.type.setBackgroundColor(Color.rgb(243,152,38));
        }else if (list.get(position).getItemTaskType().equals("火灾")){
            lvTaskThingDetailHolder.type.setBackgroundColor(Color.rgb(241,90,74));
        }else if (list.get(position).getItemTaskType().equals("交通事故")){
            lvTaskThingDetailHolder.type.setBackgroundColor(Color.rgb(13,183,137));
        }else {
            lvTaskThingDetailHolder.type.setBackgroundColor(Color.rgb(47,47,47));
        }
        Log.i("tag","daper+===="+list.get(position).getItemTaskType());
        lvTaskThingDetailHolder.type.setText(list.get(position).getItemTaskType());
        lvTaskThingDetailHolder.man.setText(list.get(position).getItemTaskMan());
        lvTaskThingDetailHolder.date.setText(list.get(position).getItemTaskDate());
        lvTaskThingDetailHolder.content.setText(list.get(position).getItemTaskContent());
        return convertView;
    }

    private static class LvTaskThingDetailHolder{
        TextView title,type,man,date,content;
    }
}
