package com.chinasoft.ctams.fragment.subjectFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.chinasoft.ctams.R;
import com.chinasoft.ctams.base.BaseFragment;
import com.chinasoft.ctams.fragment.subjectFragment.adapter.TopicListAdapter;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import sql.TopicMessage;

/**
 * Created by ls1213 on 2016/8/11.
 */

public class SubjectMainFragment extends BaseFragment {
    @Bind(R.id.rv_topic_main_activity)
    ListView listView;
    @Bind(R.id.tv_theme_delete)
    TextView tv_themeDelete;
    @Bind(R.id.tv_theme_cancel)
    TextView tv_themeCancel;
    @Bind(R.id.ll_theme_operation)
    LinearLayout ll_operation;

    private List<TopicMessage> topicBeanList;
    private List<TopicMessage> selectedList = new ArrayList<TopicMessage>(); 	//记录被选中过的item

    private static SubjectMainFragment fragment;
    private TopicListAdapter adapter;

    public static SubjectMainFragment getInstance() {
        if (fragment == null) {
            fragment = new SubjectMainFragment();
        }
        return fragment;
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_subject_main;
    }

    @Override
    public void initPage(Bundle savedInstanceState) {
        init();
    }

    private void init() {
        topicBeanList = DataSupport.findAll(TopicMessage.class);
        adapter = new TopicListAdapter(getActivity(), topicBeanList);
        listView.setAdapter(adapter);
        listenListView();

    }

    private void listenListView() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TopicMessage message = (TopicMessage) adapter.getItem(position);
                Intent intent = new Intent(getActivity(), SubjectDetailActivity.class);
                intent.putExtra("topicUrl", message.getTopicSrc());
                intent.putExtra("topicTitle", message.getTopicTitle());
                intent.putExtra("topicTime", message.getTopicTime());
                intent.putExtra("isPush", false);
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                selectedList.clear();
                adapter.checkShow=true;
                adapter.notifyDataSetChanged();
                ll_operation.setVisibility(View.VISIBLE);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        boolean isSelected=adapter.getisSelectedAt(position);
                        if (!isSelected){
                            selectedList.add(topicBeanList.get(position));
                        }else{
                            selectedList.remove(topicBeanList.get(position));
                        }
                        //选中状态的切换
                        adapter.setItemisSelectedMap(position, !isSelected);

                    }
                });

                return true;
            }
        });
    }


    @OnClick({R.id.tv_theme_delete, R.id.tv_theme_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_theme_delete:
                deleteData();
                break;
            case R.id.tv_theme_cancel:
                adapter.checkShow=false;
                adapter.notifyDataSetChanged();
                ll_operation.setVisibility(View.GONE);

                break;
        }
    }
    /**
     * 删除数据
     */
    private void deleteData(){
        for(TopicMessage message:selectedList){
            topicBeanList.remove(message);
            adapter.checkShow=false;
            adapter.notifyDataSetChanged();
            message.delete();
            ll_operation.setVisibility(View.GONE);
            adapter.isSelectedMap.clear();
        }
    }


}
