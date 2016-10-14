package com.chinasoft.ctams.fragment.mediafragment.monitorList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;
import android.widget.Toast;

import com.chinasoft.ctams.R;
import com.chinasoft.ctams.base.BaseFragment;
import com.chinasoft.ctams.fragment.mediafragment.monitorList.activity.MonitorListActivity;
import com.chinasoft.ctams.fragment.mediafragment.monitorList.bean.MonitorAreaBean;
import com.chinasoft.ctams.model.MyServerModel;
import com.chinasoft.ctams.model.ServerModel;
import com.chinasoft.ctams.util.ConstantCode;
import com.chinasoft.ctams.util.myTree.Node;
import com.chinasoft.ctams.util.myTree.adapter.SimpleTreeListViewAdapter;
import com.chinasoft.ctams.util.myTree.adapter.TreeViewAdapter;
import com.chinasoft.ctams.view.CustomProgressDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Administrator on 2016/6/29.
 */
public class MonitorVideoListFragment extends BaseFragment {

    private static MonitorVideoListFragment fragment;

    private final String MONITOR_LIST_TREE = "people/findAreaTree";


    @Bind(R.id.lv_my_tree_monitor)
    ListView lvMyTreeMonitor;
    private List<MonitorAreaBean> monitorAreaBeanList;
    private SimpleTreeListViewAdapter<MonitorAreaBean> simpleTreeListViewAdapter;
    private CustomProgressDialog dialog;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            dialog.dismiss();
            if (msg.what == ConstantCode.POST_SUCCESS) {
                    monitorAreaBeanList.addAll((List<MonitorAreaBean>) msg.obj);
                    initData();
            } else if (msg.what == ConstantCode.DATA_IS_NULL) {
                Toast.makeText(getContext(), R.string.data_is_null, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), R.string.getDataFailed, Toast.LENGTH_SHORT).show();
            }
        }
    };

    public static MonitorVideoListFragment getInstance() {
        if (fragment == null) {
            fragment = new MonitorVideoListFragment();
        }
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_monitor_video_list;
    }

    @Override
    public void initPage(Bundle savedInstanceState) {
        init();
        initData();
    }

    private void init() {
        monitorAreaBeanList = new ArrayList<>();
        if (dialog==null){
            dialog=CustomProgressDialog.createDialog(getActivity());
            dialog.setMessage("正在加载...");
        }
        dialog.show();
        ServerModel.getInstance(getContext()).getMonitorAreaListTree(handler, MONITOR_LIST_TREE, getContext());


    }

    private void initData() {
        try {
            simpleTreeListViewAdapter = new SimpleTreeListViewAdapter<>(getContext(), lvMyTreeMonitor, monitorAreaBeanList, 0);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        lvMyTreeMonitor.setAdapter(simpleTreeListViewAdapter);
        simpleTreeListViewAdapter.setOnTreeNodeClickListener(new TreeViewAdapter.OnTreeNodeClickListener() {
            @Override
            public void onClick(Node node, int position) {
                if (node.isLeaf()) {
                    String placeId=node.getId();
                    if ("".equals(placeId) || placeId ==null){
                        return;
                    }
                    Intent intent=new Intent(getActivity(), MonitorListActivity.class);
                    intent.putExtra("placeId",placeId);
                    startActivity(intent);
                } else {
                }
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        dialog=null;
        MyServerModel.getInstance(getActivity()).cancelTag(getActivity());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        handler.removeMessages(0);
    }




}
