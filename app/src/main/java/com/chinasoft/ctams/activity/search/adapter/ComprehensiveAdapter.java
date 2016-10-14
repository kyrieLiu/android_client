package com.chinasoft.ctams.activity.search.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinasoft.ctams.R;
import com.chinasoft.ctams.activity.search.bean.ComprehensiveSearchBean;

import java.util.List;

/**
 * Created by Kyrie on 2016/7/28.
 * Email:kyrie_liu@sina.com
 */
public class ComprehensiveAdapter extends BaseAdapter{
    private List<ComprehensiveSearchBean> list;
    private Context context;
    private LayoutInflater layoutInflater;
    public ComprehensiveAdapter(Context context,List<ComprehensiveSearchBean> list){
        this.context=context;
        this.list=list;
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
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        ComprehensiveSearchBean bean=list.get(position);
        if (convertView==null){
            viewHolder=new ViewHolder();
            convertView=layoutInflater.inflate(R.layout.item_lv_monitor_video_list,null);
            viewHolder.tv_title= (TextView) convertView.findViewById(R.id.tv_item_monitor_video_name);
            viewHolder.tv_time= (TextView) convertView.findViewById(R.id.tv_item_monitor_video_number);
            viewHolder.tv_type= (TextView) convertView.findViewById(R.id.tv_item_monitor_video_place);
            viewHolder.tv_organize = (TextView) convertView.findViewById(R.id.tv_item_monitor_video_decript);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_title.setText(bean.getLTitle()+"");
        viewHolder.tv_time.setText(bean.getLTimer());
        viewHolder.tv_organize.setText(bean.getLOrganization());
        viewHolder.tv_type.setText(bean.getLType());
        return convertView;
    }
    private class ViewHolder{
        private TextView tv_title,tv_type,tv_time,tv_organize;
    }
}
