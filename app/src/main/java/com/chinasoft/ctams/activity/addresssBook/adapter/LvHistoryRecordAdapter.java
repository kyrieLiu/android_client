package com.chinasoft.ctams.activity.addresssBook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinasoft.ctams.R;
import com.chinasoft.ctams.activity.addresssBook.bean.HistoryRecordBean;

import java.util.List;

/**
 * Created by Administrator on 2016/7/6.
 */
public class LvHistoryRecordAdapter extends BaseAdapter {
    private Context context;
    private List<HistoryRecordBean> list;
    private LayoutInflater layoutInflater;

    public LvHistoryRecordAdapter(Context context, List<HistoryRecordBean> list) {
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
        LvHistoryRecordHolder lvHistoryRecordHolder=null;
        if (convertView==null){
            lvHistoryRecordHolder=new LvHistoryRecordHolder();
            convertView=layoutInflater.inflate(R.layout.item_lv_history_record_fragment,null);
            lvHistoryRecordHolder.contactName= (TextView) convertView.findViewById(R.id.tv_item_contact_name_history_record);
            lvHistoryRecordHolder.contactPhone= (TextView) convertView.findViewById(R.id.tv_item_contact_phone_history_record);
            lvHistoryRecordHolder.contactState= (TextView) convertView.findViewById(R.id.tv_item_contact_state_history_record);
            lvHistoryRecordHolder.contactTime= (TextView) convertView.findViewById(R.id.tv_item_contact_time_history_record);
            convertView.setTag(lvHistoryRecordHolder);
        }else {
            lvHistoryRecordHolder= (LvHistoryRecordHolder) convertView.getTag();
        }
        HistoryRecordBean historyRecordBean=list.get(position);
        if (historyRecordBean!=null) {
            lvHistoryRecordHolder.contactName.setText(historyRecordBean.getContactName());
            lvHistoryRecordHolder.contactPhone.setText(historyRecordBean.getContactPhone());
            lvHistoryRecordHolder.contactState.setText(historyRecordBean.getContactState());
            lvHistoryRecordHolder.contactTime.setText(historyRecordBean.getContactTime());
        }
        return convertView;
    }
    private class LvHistoryRecordHolder{
        TextView contactName,contactPhone,contactTime,contactState;
    }
}
