package com.chinasoft.ctams.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinasoft.ctams.R;

import org.litepal.crud.DataSupport;

import java.util.List;

import sql.ChatInformation;

/**
 * Created by Administrator on 2016/6/16.
 */
public class GvFuntionsAdapter extends BaseAdapter {
    private Context context;
    private int[] icons;
    private String[] functionNames;
    private LayoutInflater layoutInflater;
    public int mark;

    public GvFuntionsAdapter(Context context, int[] icons, String[] functionNames) {
        this.context = context;
        this.icons = icons;
        this.functionNames = functionNames;
        this.layoutInflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return functionNames.length;
    }

    @Override
    public Object getItem(int position) {
        return functionNames[position];
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.gv_item_homepage_functions, null);
            viewHolder.function= (TextView) convertView.findViewById(R.id.tv_item_gv_homepage_funtions);
            viewHolder.icon= (ImageView) convertView.findViewById(R.id.iv_item_gv_homepage_funtions);
            viewHolder.iv_notification=(ImageView)convertView.findViewById(R.id.iv_item_gv_homepage_flag);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.function.setText(functionNames[position]);
        viewHolder.icon.setImageResource(icons[position]);
        if (position==7){
            if (mark!=1){
                List<ChatInformation> chatInformationList = DataSupport.findAll(ChatInformation.class);
                int chatSize=chatInformationList.size();
                if (chatSize>0){
                    viewHolder.iv_notification.setVisibility(View.VISIBLE);
                }
            }else{
                viewHolder.iv_notification.setVisibility(View.GONE);
            }

        }

        return convertView;
    }

    private class ViewHolder {
        ImageView icon,iv_notification;
        TextView function;

    }

}
