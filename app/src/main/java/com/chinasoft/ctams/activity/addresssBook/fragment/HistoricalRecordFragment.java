package com.chinasoft.ctams.activity.addresssBook.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.CallLog.Calls;
import android.widget.ListView;

import com.chinasoft.ctams.R;
import com.chinasoft.ctams.activity.addresssBook.adapter.LvHistoryRecordAdapter;
import com.chinasoft.ctams.activity.addresssBook.bean.HistoryRecordBean;
import com.chinasoft.ctams.base.BaseFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Administrator on 2016/7/6.
 */
public class HistoricalRecordFragment extends BaseFragment {
    @Bind(R.id.lv_history_record_fragment)
    ListView lvHistoryRecordFragment;
    private List<HistoryRecordBean> historyRecordBeanList;
    private LvHistoryRecordAdapter lvHistoryRecordAdapter;

    private String[] projection={Calls._ID,Calls.CACHED_NAME,Calls.NUMBER,Calls.DATE,Calls.TYPE};
    private final  int ID_COLUMN_INDEX = 0;
    private final  int CACHED_NAME_COLUMN_INDEX = 1;
    private final  int NUMBER_COLUMN_INDEX = 2;
    private final  int DATE_COLUMN_INDEX = 3;
    private final  int TYPE_COLUMN_INDEX = 4;
    @Override
    public int getLayoutId() {
        return R.layout.fragment_history_record;
    }

    @Override
    public void initPage(Bundle savedInstanceState) {
        init();
        initData();
    }

    private void init() {
        historyRecordBeanList = new ArrayList<>();
        lvHistoryRecordAdapter = new LvHistoryRecordAdapter(getContext(), historyRecordBeanList);
        lvHistoryRecordFragment.setAdapter(lvHistoryRecordAdapter);
    }

    private void initData() {
        getContactHistory();
        lvHistoryRecordAdapter.notifyDataSetChanged();
    }

    private void getContactHistory() {
        Cursor cursor = getContext().getContentResolver().query(Calls.CONTENT_URI, projection, null, null, Calls.DEFAULT_SORT_ORDER+" limit 0,10");
        if (cursor!=null && cursor.getCount()>0) {
            while (cursor.moveToNext()) {
                CallLog callLog = new CallLog();
                HistoryRecordBean historyRecordBean = new HistoryRecordBean();
                historyRecordBean.setContactName(cursor.getString(CACHED_NAME_COLUMN_INDEX));
                historyRecordBean.setContactPhone(cursor.getString(NUMBER_COLUMN_INDEX));
                historyRecordBean.setContactId(cursor.getString(ID_COLUMN_INDEX));

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(Long.parseLong(cursor.getString(DATE_COLUMN_INDEX)));
                String time = simpleDateFormat.format(date);
                historyRecordBean.setContactTime(time);
                String type;
                switch (Integer.parseInt(cursor.getString(TYPE_COLUMN_INDEX))) {
                    case Calls.INCOMING_TYPE:
                        type = "呼入";
                        break;
                    case Calls.OUTGOING_TYPE:
                        type = "呼出";
                        break;
                    case Calls.MISSED_TYPE:
                        type = "未接";
                        break;
                    default:
                        type = "挂断";//应该是挂断.根据我手机类型判断出的
                        break;
                }
                historyRecordBean.setContactState(type);
                historyRecordBeanList.add(historyRecordBean);
            }
        }
        cursor.close();
    }

}
