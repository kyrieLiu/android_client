package com.chinasoft.ctams.activity.sendPaper.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinasoft.ctams.R;
import com.chinasoft.ctams.activity.task.bean.SendPaperBean;

import java.util.List;

/**
 * Created by Administrator on 2016/6/21.
 */
public class LvSendPaperAdapter extends BaseAdapter {
    private Context context;
    private List<SendPaperBean> list;
    private LayoutInflater layoutInflater;
    public LvSendPaperAdapter(Context context, List<SendPaperBean> list) {
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
        LvSendPaperHolder lvSendPaperHolder=null;
        if (convertView==null){
            lvSendPaperHolder=new LvSendPaperHolder();
            convertView=layoutInflater.inflate(R.layout.lv_item_send_paper_list,null);
            lvSendPaperHolder.sendPaperTitle= (TextView) convertView.findViewById(R.id.tv_item_send_paper_title);
            lvSendPaperHolder.sendPaperContent= (TextView) convertView.findViewById(R.id.tv_item_send_paper_content);
            lvSendPaperHolder.sendPaperPlace = (TextView) convertView.findViewById(R.id.tv_item_send_place);
            lvSendPaperHolder.sendPaperTime= (TextView) convertView.findViewById(R.id.tv_item_send_paper_time);
            lvSendPaperHolder.iv_type=(ImageView) convertView.findViewById(R.id.iv_item_task_type);
            convertView.setTag(lvSendPaperHolder);
        }else {
            lvSendPaperHolder= (LvSendPaperHolder) convertView.getTag();
        }
        lvSendPaperHolder.sendPaperTitle.setText(list.get(position).getSendPaperTitle());

        lvSendPaperHolder.sendPaperContent.setText(list.get(position).getSendPaperContent());
        lvSendPaperHolder.sendPaperPlace.setText(list.get(position).getSendPaperPerson());
        if (list.get(position).getSendPaperMessageType().equals("暴恐")){
            lvSendPaperHolder.iv_type.setImageResource(R.mipmap.type_terror);
        }else if (list.get(position).getSendPaperMessageType().equals("火灾")){
            lvSendPaperHolder.iv_type.setImageResource(R.mipmap.type_fire);
        }else if (list.get(position).getSendPaperMessageType().equals("交通事故")){
            lvSendPaperHolder.iv_type.setImageResource(R.mipmap.type_traffic_logo);
        }else {
            lvSendPaperHolder.iv_type.setImageResource(R.mipmap.type_unlawful_assembly);
        }
        lvSendPaperHolder.sendPaperTime.setText(list.get(position).getSendPaperDate());
        return convertView;
    }

    private class LvSendPaperHolder{
        TextView sendPaperTitle,sendPaperContent, sendPaperPlace,sendPaperTime;
        ImageView iv_type;
    }
}
