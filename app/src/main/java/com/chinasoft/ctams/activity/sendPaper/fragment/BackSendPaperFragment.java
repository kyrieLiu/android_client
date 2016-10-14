package com.chinasoft.ctams.activity.sendPaper.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.chinasoft.ctams.R;
import com.chinasoft.ctams.activity.sendPaper.SendPaperMainActivity;
import com.chinasoft.ctams.activity.sendPaper.activity.SendPaperDetailActivity;
import com.chinasoft.ctams.activity.sendPaper.adapter.LvSendPaperAdapter;
import com.chinasoft.ctams.activity.task.bean.SendPaperBean;
import com.chinasoft.ctams.base.BaseFragment;
import com.chinasoft.ctams.model.ServerModel;
import com.chinasoft.ctams.util.ConstantCode;
import com.chinasoft.ctams.util.SharedPreferencesHelper;
import com.chinasoft.ctams.view.CustomProgressDialog;
import com.chinasoft.ctams.view.XListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Administrator on 2016/6/21.
 */
public class BackSendPaperFragment extends BaseFragment {
    private final String URL_SEND_PAPER_LIST = "bizSubmittedInfo/listBizReceiveinfoCon";
    @Bind(R.id.lv_back_send_paper)
    XListView lvBackSendPaper;
    private LvSendPaperAdapter lvSendPaperAdapter;

    private List<SendPaperBean> sendPaperBeanList;
    private int pageNo = 1;
    private boolean isFresh = true;

    private CustomProgressDialog dialog;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (dialog!=null)
                dialog.dismiss();
            if (lvBackSendPaper!=null)
            lvBackSendPaper.stopLoadMore();
            if (msg.what == ConstantCode.POST_SUCCESS) {
                if (isFresh) {
                    sendPaperBeanList.clear();
                    sendPaperBeanList.addAll((List<SendPaperBean>) msg.obj);
                    lvSendPaperAdapter.notifyDataSetChanged();
                    pageNo = 1;
                } else {
                    sendPaperBeanList.addAll((List<SendPaperBean>) msg.obj);
                    lvSendPaperAdapter.notifyDataSetChanged();
                    Toast.makeText(getContext(), "加载成功！", Toast.LENGTH_SHORT).show();
                }
            } else if (msg.what == ConstantCode.DATA_IS_NULL) {
                if (pageNo != 1) {
                    pageNo--;
                    Toast.makeText(getContext(), R.string.no_more_data, Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getContext(), R.string.data_is_null, Toast.LENGTH_SHORT).show();
                }
            } else if (msg.what == ConstantCode.DATA_DELETED_SUCCESS) {
                lvSendPaperAdapter.notifyDataSetChanged();
            } else if (msg.what == ConstantCode.DATA_DELETED_FAILED) {
                Toast.makeText(getContext(), "删除失败，请重试！", Toast.LENGTH_SHORT).show();
            } else {
                if (pageNo != 1) {
                    pageNo--;
                }
                Toast.makeText(getContext(), R.string.getDataFailed, Toast.LENGTH_SHORT).show();
            }
        }
    };
    private String organizationId;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_back_send_paper_list;
    }

    @Override
    public void initPage(Bundle savedInstanceState) {
        init();
        initData();
    }

    private void init() {
        sendPaperBeanList = new ArrayList<>();

        lvSendPaperAdapter = new LvSendPaperAdapter(getContext(), sendPaperBeanList);
        lvBackSendPaper.setAdapter(lvSendPaperAdapter);
        lvBackSendPaper.setPullRefreshEnable(false);
        lvBackSendPaper.setPullLoadEnable(true);
        organizationId = SharedPreferencesHelper.getInstance().getOrganizationId(getActivity());
        ServerModel.getInstance(getActivity()).getSendPaperByType(handler, URL_SEND_PAPER_LIST, "1000033", organizationId, 1,getActivity());
        if (dialog==null){
            dialog=CustomProgressDialog.createDialog(getActivity());
            dialog.setMessage("正在请求数据...");
        }
        dialog.show();
    }

    private void initData() {
        lvBackSendPaper.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
            }

            @Override
            public void onLoadMore() {
                pageNo++;
                isFresh=false;
                ServerModel.getInstance(getActivity()).getSendPaperByType(handler, URL_SEND_PAPER_LIST, "1000033", organizationId, pageNo,getActivity());
            }
        });
        lvBackSendPaper.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), SendPaperDetailActivity.class);
                intent.putExtra("detail", sendPaperBeanList.get(position-1));
                startActivity(intent);
            }
        });


    }

    @Override
    public void onResume() {
        if (SendPaperMainActivity.isChange) {
            sendPaperBeanList.clear();
            lvSendPaperAdapter.notifyDataSetChanged();
            pageNo = 1;
            ServerModel.getInstance(getActivity()).getSendPaperByType(handler, URL_SEND_PAPER_LIST, "1000033", organizationId, pageNo,getActivity());
            SendPaperMainActivity.isChange=false;
        }
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ServerModel.getInstance(getActivity()).cancelTag(getActivity());
        dialog=null;

    }


}
