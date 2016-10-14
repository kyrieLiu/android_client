package com.chinasoft.ctams.activity.sendPaper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chinasoft.ctams.R;
import com.chinasoft.ctams.activity.sendPaper.adapter.LvSelectAddressAdapter;
import com.chinasoft.ctams.base.BaseActivity;
import com.chinasoft.ctams.model.ServerModel;
import com.chinasoft.ctams.util.ConstantCode;
import com.chinasoft.ctams.view.CustomProgressDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by ls1213 on 2016/8/16.
 */

public class SelectSendAddressActivity extends BaseActivity {

    private final String GET_SELECT_SEND_ADDRESS_LIST="tLongitude/findMapAllAdress";

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.lv_select_address)
    ListView lvSelectAddress;
    @Bind(R.id.et_select_address_filter)
    EditText et_filter;
    private List<String> sendAddressBeanList=new ArrayList<>();
    private LvSelectAddressAdapter lvSelectAddressAdapter;

    private CustomProgressDialog dialog;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            dialog.dismiss();
            if (msg.what == ConstantCode.POST_SUCCESS){
                sendAddressBeanList.addAll((List<String>) msg.obj);
                lvSelectAddressAdapter = new LvSelectAddressAdapter(SelectSendAddressActivity.this, sendAddressBeanList);
                lvSelectAddress.setAdapter(lvSelectAddressAdapter);
                listenEditText();
                listenListView();
            }else {
                Toast.makeText(SelectSendAddressActivity.this,R.string.getDataFailed,Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_select_send_address;
    }

    @Override
    public void initPage(Bundle savedInstanceState) {
        tvTitle.setText("选择报送地点");
        ServerModel.getInstance(this).getSendAddressListData(handler,GET_SELECT_SEND_ADDRESS_LIST,SelectSendAddressActivity.this);
        if (dialog==null){
            dialog=CustomProgressDialog.createDialog(this);
            dialog.setMessage("请稍后...");
        }
        dialog.show();

    }

    /**
     * 根据输入的关键字快速查询
     */
    private void listenEditText(){
        et_filter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterData(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private void listenListView(){
        lvSelectAddress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String address= (String) lvSelectAddressAdapter.getItem(position);
                sendAddress(address);
            }
        });
    }

    private void filterData(String filterStr) {
        List<String> filterDateList = new ArrayList<String>();

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = sendAddressBeanList;
        } else {
            filterDateList.clear();
            for (String key : sendAddressBeanList) {
                if (key.indexOf(filterStr.toString()) != -1) {
                    filterDateList.add(key);
                }
            }
        }
        lvSelectAddressAdapter.updateListView(filterDateList);
    }


    @OnClick({R.id.iv_title_back,R.id.tv_selected_place})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                setResult(ConstantCode.POST_FAILED);
                finish();
                break;
            case R.id.tv_selected_place:
                String address=et_filter.getText().toString();
                sendAddress(address);
                break;
        }
    }
    private void sendAddress(String address){
        Intent intent=new Intent();
        intent.putExtra("selectAddress",address);
        setResult(10,intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dialog=null;
    }
}
