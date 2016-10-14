package com.chinasoft.ctams.activity.statistic;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.chinasoft.ctams.R;
import com.chinasoft.ctams.base.BaseActivity;
import com.chinasoft.ctams.bean.bean_json.StatisticReceiveInfoBean;
import com.chinasoft.ctams.model.MyServerModel;
import com.chinasoft.ctams.util.ConstantCode;
import com.chinasoft.ctams.view.CustomProgressDialog;
import com.chinasoft.ctams.view.HistogramView;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Kyrie on 2016/6/28.
 * Email:kyrie_liu@sina.com
 */
public class StatisticActivity extends BaseActivity {

    @Bind(R.id.bt_statistic_receive_type)
    Button bt_type;
    @Bind(R.id.bt_statistic_receive_date)
    Button bt_date;
    @Bind(R.id.bt_statistic_receive_administration)
    Button bt_administration;
    @Bind(R.id.bt_statistic_receive_submmit)
    Button bt_submmit;
    @Bind(R.id.tv_statistic_type)
    TextView tv_index_type;
    @Bind(R.id.ll_statistic_receive_information)
    LinearLayout ll_statisticReceiveInformation;
    @Bind(R.id.lv_statistic_receive_information)
    ListView lv_receiveInformation;

    private View[] views;
    private HistogramView histogramView;
    private CustomProgressDialog dialog;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (dialog!=null)
            dialog.dismiss();
            switch (msg.what) {
                case ConstantCode.POST_SUCCESS:
                    List<StatisticReceiveInfoBean> list = (List<StatisticReceiveInfoBean>) msg.obj;
                    int[] data=new int[list.size()];
                    for (int i=0;i<list.size();i++){
                    data[i]=list.get(i).getCount()*100;
                }
                    histogramView = new HistogramView(StatisticActivity.this, list);
                    histogramView.setProgress(data);
                    ll_statisticReceiveInformation.removeAllViews();
                    ll_statisticReceiveInformation.addView(histogramView);
                    StatisticListAdapter adapter=new StatisticListAdapter(StatisticActivity.this,list);
                    lv_receiveInformation.setAdapter(adapter);
                    break;
            }
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_statistic_receive_info;
    }

    @Override
    public void initPage(Bundle savedInstanceState) {
        MyServerModel.getInstance(this).loadCharStatistic(handler,"bizReceiveinfo/findReInfoByMsgType",this);
        views=new View[]{bt_type,bt_date,bt_submmit,bt_administration};
        if (dialog==null){
            dialog=CustomProgressDialog.createDialog(this);
            dialog.setMessage("请稍后...");
        }
        dialog.show();
        changeBackground(bt_type);
    }



    @OnClick({R.id.bt_statistic_receive_type, R.id.bt_statistic_receive_date, R.id.bt_statistic_receive_administration, R.id.bt_statistic_receive_submmit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_statistic_receive_type:
                MyServerModel.getInstance(this).loadCharStatistic(handler,"bizReceiveinfo/findReInfoByMsgType",this);
                changeBackground(bt_type);
                tv_index_type.setText("事件类型");
                break;
            case R.id.bt_statistic_receive_date:
                MyServerModel.getInstance(this).loadCharStatistic(handler,"bizReceiveinfo/findReInfoByMonth",this);
                changeBackground(bt_date);
                tv_index_type.setText("报送日期");
                break;
            case R.id.bt_statistic_receive_administration:
                MyServerModel.getInstance(this).loadCharStatistic(handler,"bizReceiveinfo/findReInfoByPosition",this);
               changeBackground(bt_administration);
                tv_index_type.setText("行政机构");
                break;
            case R.id.bt_statistic_receive_submmit:
                MyServerModel.getInstance(this).loadCharStatistic(handler,"bizReceiveinfo/findReInfoBySub ",this);
                changeBackground(bt_submmit);
                tv_index_type.setText("报送单位");
                break;
        }
    }
    private void changeBackground(View view){
        dialog.show();
        for (View v:views){
            if (v==view){
                v.setBackgroundColor(getResources().getColor(R.color.colorBackgroundGray));
                v.setClickable(false);
            }else{
                v.setBackgroundColor(getResources().getColor(R.color.white));
                v.setClickable(true);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dialog=null;
        MyServerModel.getInstance(this).cancelTag(this);
    }
}
