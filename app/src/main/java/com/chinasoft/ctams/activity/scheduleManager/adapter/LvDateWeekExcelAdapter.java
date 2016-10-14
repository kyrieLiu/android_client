package com.chinasoft.ctams.activity.scheduleManager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinasoft.ctams.R;
import com.chinasoft.ctams.bean.bean_json.ScheduleDetailsBean;

import java.util.List;

/**
 * Created by Administrator on 2016/6/18.
 */
public class LvDateWeekExcelAdapter extends BaseAdapter {
    private Context context;
    List<ScheduleDetailsBean> list;

    public LvDateWeekExcelAdapter(Context context,  List<ScheduleDetailsBean> list) {
        this.context = context;
        this.list = list;
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
        ViewHolder viewHolder;
        ScheduleDetailsBean detailsBean=list.get(position);
        if (convertView==null){
            viewHolder=new ViewHolder();
            convertView=LayoutInflater.from(context).inflate(R.layout.lv_item_fragment_date_excel,null);
            viewHolder.date=(TextView)convertView.findViewById(R.id.tv_item_fragment_excel_date);
            viewHolder.name=(TextView)convertView.findViewById(R.id.tv_item_fragment_excel_name);
            viewHolder.startTime = (TextView) convertView.findViewById(R.id.tv_item_fragment_excel_startTime);
            viewHolder.endTime=(TextView)convertView.findViewById(R.id.tv_item_fragment_excel_endTime);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.date.setText(detailsBean.getDutyrelationDate());
        viewHolder.name.setText(detailsBean.getDutyrelationPerids());
        viewHolder.startTime.setText(detailsBean.getDutystartTime());
        viewHolder.endTime.setText(detailsBean.getDutyendTime());
        return convertView;
    }
    private class ViewHolder{
        private TextView date,name,startTime,endTime;
    }
}
