package com.chinasoft.ctams.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chinasoft.ctams.R;
import com.chinasoft.ctams.bean.bean_json.FriendListBean;

import java.util.List;

import sql.ChatInformation;

/**
 * Created by Kyrie on 2016/6/23.
 * Email:kyrie_liu@sina.com
 * 好友列表适配器
 */
public class FriendsAdapter extends BaseAdapter {
    private List<FriendListBean> userList;
    private Context context;
    private List<ChatInformation> notReadList;

    public FriendsAdapter(Context context, List<FriendListBean> userList, List<ChatInformation> dataList){
        this.context=context;
        this.userList=userList;
        this.notReadList =dataList;
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public Object getItem(int position) {
        return userList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FriendListBean friendListBean=userList.get(position);
        ViewHolder viewHolder;
        if (convertView==null){
            viewHolder=new ViewHolder();
            convertView= LayoutInflater.from(context).inflate(R.layout.lv_item_friend_list,null);
            viewHolder.tv_name=(TextView)convertView.findViewById(R.id.tv_item_friend_list_user);
            viewHolder.iv_flag=(ImageView)convertView.findViewById(R.id.iv_item_friendList_flag);
            viewHolder.iv_head_photo=(ImageView)convertView.findViewById(R.id.iv_item_friendList_headPhoto);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_name.setText(friendListBean.getPeopleName());
        String imagePath=friendListBean.getHeadPictureUrl();
        Log.i("info","好友头像地址是"+imagePath);
        Glide.with(context).load(imagePath).placeholder(R.mipmap.leader_daily_home_page)
                .into(viewHolder.iv_head_photo);

        if (friendListBean.isOnline()){
            viewHolder.tv_name.setTextColor(context.getResources().getColor(R.color.red));
        }
        // 将未读消息标志展示在相应联系人上
        if (notReadList.size()>0){
            for (ChatInformation information: notReadList){
                String peopleName=information.getPeopleName();

                if (peopleName.equals(friendListBean.getPeopleName())&&information.getTargetAccountName().equals(friendListBean.getAccountName())) {
                    viewHolder.iv_flag.setVisibility(View.VISIBLE);
                }
            }
        }
        return convertView;
    }
    private class ViewHolder{
        private TextView tv_name;
        private ImageView iv_flag,iv_head_photo;
    }
}
