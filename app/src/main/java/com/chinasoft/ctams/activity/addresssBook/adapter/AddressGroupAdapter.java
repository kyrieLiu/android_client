package com.chinasoft.ctams.activity.addresssBook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinasoft.ctams.R;
import com.chinasoft.ctams.activity.addresssBook.bean.GroupManageBean;

import java.util.List;
import java.util.Map;

/**
 * 说明:
 */
public class AddressGroupAdapter extends BaseExpandableListAdapter {
    private List<String> parentList;
    private Map<String,List<GroupManageBean>> map;
    private Context context;

    public AddressGroupAdapter(Context context, List<String> parentList, Map<String,List<GroupManageBean>> map){
        this.context=context;
        this.parentList=parentList;
        this.map=map;
    }

    @Override
    public int getGroupCount() {
        return parentList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        String groupName=parentList.get(groupPosition);
        int childCount=map.get(groupName).size();
        return childCount;
    }

    @Override
    public Object getGroup(int groupPosition) {
        String groupName=parentList.get(groupPosition);
        return groupName;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        String groupName=parentList.get(groupPosition);
        GroupManageBean childModel=map.get(groupName).get(childPosition);
        return childModel;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.item_group_address_parent,null);
        }
        ImageView imageView= (ImageView) convertView.findViewById(R.id.iv_item_group_parent);
        if (isExpanded){
            imageView.setImageResource(R.mipmap.arrow_down);
        }else{
            imageView.setImageResource(R.mipmap.arrow_up);
        }
        TextView parentText=(TextView)convertView.findViewById(R.id.tv_item_group_parent_text);
        parentText.setText(parentList.get(groupPosition));
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView=LayoutInflater.from(context).inflate(R.layout.item_group_address_child,null);
        }
        TextView textName=(TextView)convertView.findViewById(R.id.tv_item_group_child_manage_name);
        TextView textPhoneNumber=(TextView)convertView.findViewById(R.id.tv_item_group_child_management_phoneNumber);
        String parentText=parentList.get(groupPosition);
        GroupManageBean model=map.get(parentText).get(childPosition);
        textName.setText(model.getPercommname());
        textPhoneNumber.setText(model.getPercommtel());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
