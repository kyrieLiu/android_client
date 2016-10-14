package com.chinasoft.ctams.fragment.subjectFragment.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chinasoft.ctams.R;

import java.util.List;

import sql.TopicMessage;

/**
 * Created by ls1213 on 2016/8/11.
 */

public class RvItemTopicListMainAdapter extends RecyclerView.Adapter<RvItemTopicListMainAdapter.RvTopicListHolder> implements View.OnClickListener{
    private Context context;
    private LayoutInflater layoutInflater;
    private List<TopicMessage> topicBeanList;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public RvItemTopicListMainAdapter(Context context, List<TopicMessage> topicBeanList) {
        this.context = context;
        this.topicBeanList = topicBeanList;
        layoutInflater=LayoutInflater.from(context);
    }

    @Override
    public RvTopicListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=layoutInflater.inflate(R.layout.item_rv_topic_list,parent,false);
        RvTopicListHolder rvTopicListHolder=new RvTopicListHolder(view);
        //将创建的View注册点击事件
        view.setOnClickListener(this);
        return rvTopicListHolder;
    }

    @Override
    public void onBindViewHolder(RvTopicListHolder holder, int position) {
        Log.i("tag","url...."+topicBeanList.get(position).getTopicSrc());
        Glide.with(context).load(topicBeanList.get(position).getTopicSrc()).into(holder.topSrc);
        holder.topTitle.setText(topicBeanList.get(position).getTopicTitle());
        holder.topicTime.setText(topicBeanList.get(position).getTopicTime());
        holder.itemView.setTag(topicBeanList.get(position));
    }



    @Override
    public int getItemCount() {
        return topicBeanList.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v,(TopicMessage) v.getTag());
        }
    }


    public static class RvTopicListHolder extends RecyclerView.ViewHolder{
        ImageView topSrc;
        TextView topTitle;
        TextView topicTime;

        public RvTopicListHolder(View itemView) {
            super(itemView);
            topSrc= (ImageView) itemView.findViewById(R.id.iv_item_rv_topic_list);
            topTitle= (TextView) itemView.findViewById(R.id.tv_item_rv_title_topic_list);
            topicTime= (TextView) itemView.findViewById(R.id.tv_item_rv_time_topic_list);
        }
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
    //define interface
    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view , TopicMessage data);
    }
}
