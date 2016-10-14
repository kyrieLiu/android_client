package com.chinasoft.ctams.activity.search.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.chinasoft.ctams.R;
import com.chinasoft.ctams.activity.search.adapter.ComprehensiveAdapter;
import com.chinasoft.ctams.activity.search.bean.ComprehensiveSearchBean;
import com.chinasoft.ctams.base.BaseActivity;
import com.chinasoft.ctams.model.MyServerModel;
import com.chinasoft.ctams.util.ConstantCode;
import com.chinasoft.ctams.view.CustomProgressDialog;
import com.chinasoft.ctams.view.XListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Kyrie on 2016/7/28.
 * Email:kyrie_liu@sina.com
 */
public class ComprehensiveSearchActivity extends BaseActivity implements XListView.IXListViewListener{
    @Bind(R.id.rl_search_back)
    RelativeLayout rl_back;
    @Bind(R.id.bt_search)
    Button bt_search;
    @Bind(R.id.et_search_keywords)
    AutoCompleteTextView et_keywords;
    @Bind(R.id.lv_search_data)
    XListView lv_data;
    private CustomProgressDialog dialog;
    private List<ComprehensiveSearchBean> list=new ArrayList<>();

    private int pageNo=1;
    private ComprehensiveAdapter adapter;
    private boolean keyFlag=false;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (dialog!=null){
                dialog.dismiss();
            }
            lv_data.stopLoadMore();
            switch (msg.what){
                case ConstantCode.POST_SUCCESS:
                    if (keyFlag){
                        list.clear();
                    }else{
                        pageNo++;
                    }
                    List<ComprehensiveSearchBean> newList= (List<ComprehensiveSearchBean>) msg.obj;
                    list.addAll(newList);
                    adapter.notifyDataSetChanged();
                    break;
                case ConstantCode.POST_FAILED:
                    Toast.makeText(ComprehensiveSearchActivity.this,"加载失败,请检查网络",Toast.LENGTH_SHORT).show();
                    break;
                case ConstantCode.POST_NULL_RESPONSE:
                    Toast.makeText(ComprehensiveSearchActivity.this,"服务器暂无数据",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };


    @Override
    public int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    public void initPage(Bundle savedInstanceState) {
        lv_data.setPullLoadEnable(true);
        lv_data.setPullRefreshEnable(false);
        lv_data.setXListViewListener(this);
        adapter = new ComprehensiveAdapter(ComprehensiveSearchActivity.this,list);
        lv_data.setAdapter(adapter);

        loadData("");//加载数据

        if (dialog==null){
            dialog=CustomProgressDialog.createDialog(this);
            dialog.setMessage("正在加载...");
        }
        dialog.show();
        listenListView();
    }

    @OnClick({R.id.rl_search_back, R.id.bt_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_search_back:
                finish();
                break;
            case R.id.bt_search:
              String   keyword=et_keywords.getText().toString()+"";
                keyFlag=true;
                pageNo=1;
                loadData(keyword);
                dialog.show();
                break;
        }
    }
    private void loadData(String keyword){
        Log.i("info","pageNo更改后"+pageNo);
        MyServerModel.getInstance(this).loadSearchData(handler,pageNo,keyword,this);

    }

    @Override
    public void onRefresh() {
    }

    @Override
    public void onLoadMore() {
        keyFlag=false;
        loadData("");
    }
    /**
     * 监听ListView
     */
    private void listenListView(){
        lv_data.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ComprehensiveSearchBean bean= (ComprehensiveSearchBean) adapter.getItem(position);
                Intent intent=new Intent(ComprehensiveSearchActivity.this,ComprehensiveSearchDetailsActivity.class);
               Bundle bundle=new Bundle();
                bundle.putSerializable("bean",bean);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyServerModel.getInstance(this).cancelTag(this);
        dialog=null;
    }
}
