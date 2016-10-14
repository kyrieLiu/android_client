package com.chinasoft.ctams.activity.statistic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinasoft.ctams.R;
import com.chinasoft.ctams.bean.bean_json.StatisticReceiveInfoBean;

import java.util.List;

/**
 * Created by Kyrie on 2016/6/29.
 * Email:kyrie_liu@sina.com
 */
public class StatisticListAdapter extends BaseAdapter{
    private  List<StatisticReceiveInfoBean> list;
    private Context context;
    public StatisticListAdapter(Context context, List<StatisticReceiveInfoBean> list){
        this.list=list;
        this.context=context;
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
        if (convertView==null){
            viewHolder=new ViewHolder();
            convertView= LayoutInflater.from(context).inflate(R.layout.item_statistic_receive_information,null);
            viewHolder.count=(TextView)convertView.findViewById(R.id.tv_item_statistic_count);
            viewHolder.type=(TextView)convertView.findViewById(R.id.tv_item_statistic_type);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.count.setText(list.get(position).getCount()+"");
        viewHolder.type.setText(list.get(position).getType()+"");
        return convertView;
    }
    private class ViewHolder{
        private TextView type,count;
    }
}
