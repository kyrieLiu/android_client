package com.chinasoft.ctams.fragment.subjectFragment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chinasoft.ctams.R;

import java.util.HashMap;
import java.util.List;

import sql.TopicMessage;

public class TopicListAdapter extends BaseAdapter {
	private Context context;
	LayoutInflater inflater;
	private List<TopicMessage> list;
	public  boolean checkShow=false;

	public HashMap<Integer, Boolean> isSelectedMap;

	public TopicListAdapter(Context context,   List<TopicMessage> list) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.list=list;
		isSelectedMap = new HashMap<Integer, Boolean>();
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
	public int getCount() {
		return list.size();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_subject_center_list, null);
			holder.iv_cover = (ImageView) convertView.findViewById(R.id.iv_item_subject_list_image);
			holder.tv_title = (TextView) convertView.findViewById(R.id.tv_item_subject_list_title);
			holder.checkBox=(CheckBox)convertView.findViewById(R.id.cb_suject_list);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (checkShow){
			holder.checkBox.setVisibility(View.VISIBLE);
		}else{
			holder.checkBox.setVisibility(View.GONE);
		}
		holder.tv_title.setText(list.get(position).getTopicTitle());
		// Glide加载视频封面
		Glide.with(context).load(list.get(position).getTopicSrc()).placeholder(R.mipmap.bg_sky)
				.into(holder.iv_cover);
		holder.checkBox.setChecked(getisSelectedAt(position));
		return convertView;
	}

	private class ViewHolder {
		private ImageView iv_cover;
		private TextView tv_title;
		private CheckBox checkBox;
	}

	public boolean getisSelectedAt(int position){

		//如果当前位置的key值为空，则表示该item未被选择过，返回false，否则返回true
		if(isSelectedMap.get(position) != null){
			return isSelectedMap.get(position);
		}
		return false;
	}

	public void setItemisSelectedMap(int position, boolean isSelectedMap){
		this.isSelectedMap.put(position, isSelectedMap);
		notifyDataSetChanged();
	}
}
