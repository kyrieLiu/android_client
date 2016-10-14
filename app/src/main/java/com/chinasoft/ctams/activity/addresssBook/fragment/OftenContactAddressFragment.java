package com.chinasoft.ctams.activity.addresssBook.fragment;

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
import com.chinasoft.ctams.activity.addresssBook.ContactPhoneActivity;
import com.chinasoft.ctams.activity.addresssBook.adapter.PublicBookSortAdapter;
import com.chinasoft.ctams.activity.addresssBook.util.CharacterParser;
import com.chinasoft.ctams.activity.addresssBook.util.PinyinComparator;
import com.chinasoft.ctams.base.BaseFragment;
import com.chinasoft.ctams.bean.bean_json.OftenContactAddressBean;
import com.chinasoft.ctams.model.MyServerModel;
import com.chinasoft.ctams.util.ConstantCode;
import com.chinasoft.ctams.util.SharedPreferencesHelper;
import com.chinasoft.ctams.view.AddressBookSideView;
import com.chinasoft.ctams.view.CustomProgressDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;


public class OftenContactAddressFragment extends BaseFragment {

    @Bind(R.id.et_address_book_filter)
    EditText etAddressBookFilter;
    @Bind(R.id.lv_address_book)
    ListView lvAddressBook;
    @Bind(R.id.tv_address_book_index)
    TextView tvAddressBookIndex;
    @Bind(R.id.as_address_book_side_view)
    AddressBookSideView asAddressBookSideView;
    private PublicBookSortAdapter adapter;


    //汉字转化为拼音的类
    private CharacterParser characterParser;
    //根据拼音来排列ListVIew里面的数据类
    private PinyinComparator pinyinComparator;

    private List<OftenContactAddressBean> list;

    private CustomProgressDialog dialog;
    private boolean nameFlag = true;//判断名字是否为空

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (dialog!=null){
                dialog.dismiss();
            }
            switch (msg.what) {
                case ConstantCode.POST_SUCCESS:
                    list = (List<OftenContactAddressBean>) msg.obj;
                    initData();
                    setTouchListener();
                    break;
                case ConstantCode.POST_FAILED:
                    Toast.makeText(getActivity(),"网络异常,请检查网络",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.fragment_address_book;
    }

    @Override
    public void initPage(Bundle savedInstanceState) {

        asAddressBookSideView.setTextView(tvAddressBookIndex);

        // 实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        loadData();


    }


    private void loadData() {
        if (dialog == null) {
            dialog = CustomProgressDialog.createDialog(getActivity());
            dialog.setMessage("正在加载...");
        }
        dialog.show();
        String username = SharedPreferencesHelper.getInstance().getPeopleId(getActivity());
        MyServerModel.getInstance(getActivity()).loadOftenContact(handler, username,getActivity());
    }

    private void initData() {
        for (int i = 0; i < list.size(); i++) {
            // 汉字转化为拼音
            String pinyin = characterParser.getSelling(list.get(i).getPercommname());
            String sortString = pinyin.substring(0, 1).toUpperCase();
            // 正则表达式,判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                list.get(i).setSortLetters(sortString.toUpperCase());
            } else {
                list.get(i).setSortLetters("#");
            }
        }
    }


    /**
     * 根据输入框的值来过滤数据并更新ListView
     */
    private void filterData(String filterStr) {
        List<OftenContactAddressBean> filterDateList = new ArrayList<OftenContactAddressBean>();

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = list;
        } else {
            filterDateList.clear();
            for (OftenContactAddressBean sortModel : list) {
                String name = sortModel.getPercommname();
                if (name.indexOf(filterStr.toString()) != -1
                        || characterParser.getSelling(name).startsWith(
                        filterStr.toString())) {
                    filterDateList.add(sortModel);
                }
            }
        }

        // 根据a-z进行排序
        Collections.sort(filterDateList, pinyinComparator);
        adapter.updateListView(filterDateList);
    }


    private void setTouchListener() {
        // 设置右侧触摸监听
        asAddressBookSideView.setOnTouchingLetterChangedListener(new AddressBookSideView.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                // 该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    lvAddressBook.setSelection(position);
                }

            }
        });


        lvAddressBook.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // 利用adapter.getItem(Position)来获取当前的position所对应的对象
                String name = ((OftenContactAddressBean) adapter.getItem(position)).getPercommname();
                String phoneNumber = ((OftenContactAddressBean) adapter.getItem(position)).getPercommtel();
                Intent intent = new Intent(getActivity(), ContactPhoneActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("phoneNumber", phoneNumber);
                startActivity(intent);
            }
        });

        // 根据a-z进行排序源数据
        Collections.sort(list, pinyinComparator);
        //创建适配器
        adapter = new PublicBookSortAdapter(getActivity(), list);
        lvAddressBook.setAdapter(adapter);

        // 根据输入框输入值的改变来过滤搜索
        etAddressBookFilter.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // 当输入框里面的值为空,更新为原来的列表,否则为过滤数据列表
                filterData(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dialog=null;
        MyServerModel.getInstance(getActivity()).cancelTag(getActivity());
    }
}
