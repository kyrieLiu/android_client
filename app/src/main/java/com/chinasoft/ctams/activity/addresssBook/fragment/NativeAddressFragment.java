package com.chinasoft.ctams.activity.addresssBook.fragment;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.chinasoft.ctams.R;
import com.chinasoft.ctams.activity.addresssBook.ContactPhoneActivity;
import com.chinasoft.ctams.activity.addresssBook.adapter.NativeAddressBookSortAdapter;
import com.chinasoft.ctams.activity.addresssBook.bean.AddressBookSortModel;
import com.chinasoft.ctams.activity.addresssBook.util.CharacterParser;
import com.chinasoft.ctams.activity.addresssBook.util.NativePinyinComparator;
import com.chinasoft.ctams.base.BaseFragment;
import com.chinasoft.ctams.view.AddressBookSideView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;


public class NativeAddressFragment extends BaseFragment {

    @Bind(R.id.et_address_book_filter)
    EditText etAddressBookFilter;
    @Bind(R.id.lv_address_book)
    ListView lvAddressBook;
    @Bind(R.id.tv_address_book_index)
    TextView tvAddressBookIndex;
    @Bind(R.id.as_address_book_side_view)
    AddressBookSideView asAddressBookSideView;
    private NativeAddressBookSortAdapter adapter;


    //汉字转化为拼音的类
    private CharacterParser characterParser;
    //根据拼音来排列ListVIew里面的数据类
    private NativePinyinComparator pinyinComparator;

    private List<AddressBookSortModel> mSortList;

    String[] PHONES_PROJECTION = new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER, "sort_key"};

    @Override
    public int getLayoutId() {
        return R.layout.fragment_address_book;
    }

    @Override
    public void initPage(Bundle savedInstanceState) {

        asAddressBookSideView.setTextView(tvAddressBookIndex);

        // 实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new NativePinyinComparator();

        fillData();
    }

private void fillData(){
    mSortList=new ArrayList<>();
    AddressBookSortModel sortModel=null;
    ContentResolver resolver = getActivity().getContentResolver();
    // 获取手机联系人
    Cursor cursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, PHONES_PROJECTION,
            null, null, "sort_key COLLATE LOCALIZED asc");
    while (cursor.moveToNext()) {
        sortModel = new AddressBookSortModel();
        sortModel.setName(cursor.getString(0));
        sortModel.setPhoneNumber(cursor.getString(1).replace(" ", ""));
        // 汉字转化为拼音
        String pinyin = characterParser.getSelling(cursor.getString(0));
        String sortString = pinyin.substring(0, 1).toUpperCase();
        // 正则表达式,判断首字母是否是英文字母
        if (sortString.matches("[A-Z]")) {
            sortModel.setSortLetters(sortString.toUpperCase());
        } else {
            sortModel.setSortLetters("#");
        }
        mSortList.add(sortModel);
    }
    cursor.close();
    setTouchListener();

}


    /**
     * 根据输入框的值来过滤数据并更新ListView
     */
    private void filterData(String filterStr) {
        List<AddressBookSortModel> filterDateList = new ArrayList<AddressBookSortModel>();

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = mSortList;
        } else {
            filterDateList.clear();
            for (AddressBookSortModel sortModel : mSortList) {
                String name = sortModel.getName();
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
                String name = ((AddressBookSortModel) adapter.getItem(position)).getName();
                String phoneNumber = ((AddressBookSortModel) adapter.getItem(position)).getPhoneNumber();
                Intent intent = new Intent(getActivity(), ContactPhoneActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("phoneNumber", phoneNumber);
                startActivity(intent);
            }
        });

        // 根据a-z进行排序源数据
        Collections.sort(mSortList, pinyinComparator);
        Log.i("info","mSortList的长度"+mSortList.size());
        //创建适配器
        adapter = new NativeAddressBookSortAdapter(getActivity(), mSortList);
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
}
