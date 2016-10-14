package com.chinasoft.ctams.fragment.mediafragment.monitorList.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinasoft.ctams.R;
import com.chinasoft.ctams.fragment.mediafragment.monitorList.bean.MonitorBean;

import java.util.List;

/**
 * Created by Administrator on 2016/6/29.
 */
public class LvMonitorVideoAdapter extends BaseAdapter {
    private Context context;
    private List<MonitorBean.MonitorRowsBean> list;
    private LayoutInflater layoutInflater;

    public LvMonitorVideoAdapter(Context context, List<MonitorBean.MonitorRowsBean> list) {
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
        LvMonitorVideoHolder lvMonitorVideoHolder=null;
        if (convertView==null){
            lvMonitorVideoHolder=new LvMonitorVideoHolder();
            convertView=layoutInflater.inflate(R.layout.item_lv_monitor_video_list,null);
            lvMonitorVideoHolder.monitorName= (TextView) convertView.findViewById(R.id.tv_item_monitor_video_name);
            lvMonitorVideoHolder.monitorNumber= (TextView) convertView.findViewById(R.id.tv_item_monitor_video_number);
            lvMonitorVideoHolder.monitorPlace= (TextView) convertView.findViewById(R.id.tv_item_monitor_video_place);
            lvMonitorVideoHolder.videoDescript = (TextView) convertView.findViewById(R.id.tv_item_monitor_video_decript);
            convertView.setTag(lvMonitorVideoHolder);
        }else {
            lvMonitorVideoHolder= (LvMonitorVideoHolder) convertView.getTag();
        }
            lvMonitorVideoHolder.monitorName.setText(list.get(position).getVideoName());
            lvMonitorVideoHolder.videoDescript.setText(list.get(position).getVideoDescribe());
            lvMonitorVideoHolder.monitorPlace.setText(list.get(position).getPlaceId());
            lvMonitorVideoHolder.monitorNumber.setText(list.get(position).getVideoNumber());
        return convertView;
    }
    private class LvMonitorVideoHolder{
        TextView monitorName,monitorNumber,monitorPlace, videoDescript;
    }
}
