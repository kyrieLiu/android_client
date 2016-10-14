package com.chinasoft.ctams.activity.sendPaper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinasoft.ctams.R;

import java.util.List;

/**
 * Created by ls1213 on 2016/8/17.
 */

public class LvSelectAddressAdapter extends BaseAdapter {
    private Context context;
    private List<String> list;
    private LayoutInflater layoutInflater;

    public LvSelectAddressAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
        this.layoutInflater=LayoutInflater.from(context);
    }
    public void updateListView(List<String> list){
        this.list=list;
        notifyDataSetChanged();
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
        LvSelectAddressHolder lvSelectAddressHolder=null;
        if (convertView == null){
            lvSelectAddressHolder=new LvSelectAddressHolder();
            convertView=layoutInflater.inflate(R.layout.item_lv_select_address,null);
            lvSelectAddressHolder.name= (TextView) convertView.findViewById(R.id.tv_item_select_address);
            convertView.setTag(lvSelectAddressHolder);
        }else {
            lvSelectAddressHolder= (LvSelectAddressHolder) convertView.getTag();
        }
        lvSelectAddressHolder.name.setText(list.get(position));
        return convertView;
    }
    public static class LvSelectAddressHolder{
        TextView name;
    }
}
