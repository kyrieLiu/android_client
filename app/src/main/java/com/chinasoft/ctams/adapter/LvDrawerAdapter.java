package com.chinasoft.ctams.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinasoft.ctams.R;

/**
 * Created by Administrator on 2016/6/20.
 */
public class LvDrawerAdapter extends BaseAdapter {
    private Context context;
    private String[] items;
    private int[] itembg;
    private LayoutInflater layoutInflater;

    public LvDrawerAdapter(Context context, String[] items,int[] itembg) {
        this.context = context;
        this.items = items;
        this.itembg=itembg;
        this.layoutInflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public Object getItem(int position) {
        return items[position];
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LvDrawerHolder lvDrawerHolder=null;
        if (convertView==null){
            lvDrawerHolder=new LvDrawerHolder();
            convertView=layoutInflater.inflate(R.layout.lv_item_drawer,null);
            lvDrawerHolder.itemIcon= (ImageView) convertView.findViewById(R.id.iv_item_drawer);
            lvDrawerHolder.itemName= (TextView) convertView.findViewById(R.id.tv_item_drawer);
            convertView.setTag(lvDrawerHolder);
        }else {
            lvDrawerHolder= (LvDrawerHolder) convertView.getTag();
        }

        lvDrawerHolder.itemName.setText(items[position]);
        lvDrawerHolder.itemIcon.setBackgroundResource(itembg[position]);
        return convertView;
    }

    private class LvDrawerHolder{
        ImageView itemIcon;
        TextView itemName;
    }
}
