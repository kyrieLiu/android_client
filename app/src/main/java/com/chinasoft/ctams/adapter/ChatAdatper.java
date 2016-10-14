package com.chinasoft.ctams.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chinasoft.ctams.R;
import com.chinasoft.ctams.view.CircleImageView;

import java.util.List;

import sql.AlreadyReadMessage;

/**
 * Created by Kyrie on 2016/6/21.
 * Email:kyrie_liu@sina.com
 * 聊天界面适配器
 */
public class ChatAdatper extends BaseAdapter {
    private List<AlreadyReadMessage> list;
    private Context context;
    public ChatAdatper(List<AlreadyReadMessage> list, Context context){
        this.list=list;
        this.context=context;

    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        AlreadyReadMessage information=list.get(position);
            viewHolder=new ViewHolder();
            if (information.getReceive()){
                convertView= LayoutInflater.from(context).inflate(R.layout.item_lv_chat_receive,null);
            }else{
                convertView=LayoutInflater.from(context).inflate(R.layout.item_lv_chat_send,null);
            }
            viewHolder.tv_content=(TextView)convertView.findViewById(R.id.tv_chat_message);
            viewHolder.iv_headPhoto=(CircleImageView) convertView.findViewById(R.id.civ_chat_head_photo);
            viewHolder.tv_content.setText(list.get(position).getMessageContent()+"");
        if (information.getReceive()){
            Glide.with(context).load(information.getPhotoUrl()).placeholder(R.mipmap.send_paper_gv_home_page_icon)
                    .into(viewHolder.iv_headPhoto);
        }else{
            Glide.with(context).load(information.getPhotoUrl()).placeholder(R.mipmap.all_select_gv_home_page_icon)
                    .into(viewHolder.iv_headPhoto);
        }


        return convertView;
    }
    private class ViewHolder{
        private TextView tv_content;
        private CircleImageView iv_headPhoto;
    }
}
