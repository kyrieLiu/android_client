package com.chinasoft.ctams.fragment.mineMainFragment.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chinasoft.ctams.R;
import com.chinasoft.ctams.base.BaseActivity;
import com.chinasoft.ctams.fragment.mineMainFragment.bean.OrgBean;
import com.chinasoft.ctams.model.ServerModel;
import com.chinasoft.ctams.util.ConstantCode;
import com.chinasoft.ctams.util.myTree.Node;
import com.chinasoft.ctams.util.myTree.adapter.SimpleTreeListViewAdapter;
import com.chinasoft.ctams.util.myTree.adapter.TreeViewAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by ls1213 on 2016/8/5.
 */

public class SelectAreaAndOrgActivity extends BaseActivity {

    private final String GET_ORG_TREE_DATA="people/findOrganizationList";
    private final String GET_AREA_TREE_DATA="people/findAreaList";
    private final String UPDATE_MINE_INFO = "people/editMobile";

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_head_menu)
    TextView tvHeadMenu;
    @Bind(R.id.lv_my_tree)
    ListView lvMyTree;
    @Bind(R.id.tv_selected_item_tree)
    EditText tvSelectedItemTree;
    private List<OrgBean> orgBeanList;
    private  boolean isSubmit=false;
    private boolean isArea=true;
    private boolean isUpdate=true;

    private Node orgBean;
    private SimpleTreeListViewAdapter<OrgBean> simpleTreeListViewAdapter;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what== ConstantCode.POST_SUCCESS){
                if (isSubmit){
                    isSubmit=false;
                    Intent intent = new Intent();
                    intent.putExtra("updateResult", orgBean.getName());
                    setResult(isArea?4:5, intent);
                    finish();
                }else {
                    orgBeanList.addAll((List<OrgBean>) msg.obj);

                    initData();
                }
            }else if (msg.what==ConstantCode.DATA_IS_NULL){
                Toast.makeText(SelectAreaAndOrgActivity.this,R.string.data_is_null,Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(SelectAreaAndOrgActivity.this,R.string.getDataFailed,Toast.LENGTH_SHORT).show();
            }
        }
    };


    @Override
    public int getLayoutId() {
        return R.layout.activity_select_area_org_tree;
    }

    @Override
    public void initPage(Bundle savedInstanceState) {
        init();
    }

    private void init() {
        isArea=getIntent().getBooleanExtra("isArea",true);
        isUpdate=getIntent().getBooleanExtra("isUpdate",true);
        tvTitle.setText("请选择");
        tvHeadMenu.setText("确定");
        tvHeadMenu.setVisibility(View.VISIBLE);
        orgBeanList=new ArrayList<>();
        ServerModel.getInstance(SelectAreaAndOrgActivity.this).getOrganizationsTreeData(handler, isArea?GET_AREA_TREE_DATA:GET_ORG_TREE_DATA,isArea);
    }

    private void initData() {
        try {
            simpleTreeListViewAdapter=new SimpleTreeListViewAdapter<>(SelectAreaAndOrgActivity.this,lvMyTree,orgBeanList,0);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        lvMyTree.setAdapter(simpleTreeListViewAdapter);
        simpleTreeListViewAdapter.setOnTreeNodeClickListener(new TreeViewAdapter.OnTreeNodeClickListener() {
            @Override
            public void onClick(Node node, int position) {
                orgBean=node;
                tvSelectedItemTree.setText(orgBean.getName());
            }
        });
    }

    @Override
    public void onBackPressed() {
        setResult(ConstantCode.POST_FAILED);
        super.onBackPressed();
    }

    @OnClick({R.id.iv_title_back, R.id.tv_head_menu})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                setResult(ConstantCode.POST_FAILED);
                finish();
                break;
            case R.id.tv_head_menu:
                isSubmit=true;
                String content=tvSelectedItemTree.getText().toString();
                if (null==content || "".equals(content)){
                    Toast.makeText(SelectAreaAndOrgActivity.this,"选择错误，请重试！",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isUpdate){
                    Intent intent = new Intent();
                    intent.putExtra("updateResult", orgBean.getName());
                    setResult(isArea?4:5, intent);
                    finish();
                } else {
                    ServerModel.getInstance(SelectAreaAndOrgActivity.this).upDatePersonInfo(SelectAreaAndOrgActivity.this, handler,UPDATE_MINE_INFO,isArea?"PEOPLE_DIVISION": "PEOPLE_ORGANIZATION", orgBean.getId());
                }
                break;
        }
    }

}
