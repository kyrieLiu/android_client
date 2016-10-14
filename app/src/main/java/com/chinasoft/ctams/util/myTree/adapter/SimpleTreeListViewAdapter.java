package com.chinasoft.ctams.util.myTree.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.chinasoft.ctams.R;
import com.chinasoft.ctams.util.myTree.Node;

import java.util.List;

/**
 * Created by Administrator on 2016/7/15.
 */
public class SimpleTreeListViewAdapter<T> extends TreeViewAdapter<T> {
    public SimpleTreeListViewAdapter(Context context, ListView tree, List data, int defalutExpandLevel) throws IllegalAccessException {
        super(context, tree, data, defalutExpandLevel);
    }

    @Override
    public View getConvertView(Node node, int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if (convertView==null){
            convertView=layoutInflater.inflate(R.layout.item_my_tree,parent,false);
            viewHolder=new ViewHolder();
            viewHolder.iv= (ImageView) convertView.findViewById(R.id.item_icon);
            viewHolder.tv= (TextView) convertView.findViewById(R.id.item_text);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        if (node.getNodeIcon()==-1){
            viewHolder.iv.setVisibility(View.INVISIBLE);
        }else {
            viewHolder.iv.setVisibility(View.VISIBLE);
            viewHolder.iv.setImageResource(node.getNodeIcon());
        }
        Log.i("tag",node.getName()+"---");
        viewHolder.tv.setText(node.getName());
        return convertView;
    }
    private class ViewHolder{
        ImageView iv;
        TextView tv;
    }
}
