package com.chinasoft.ctams.activity.addresssBook.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.chinasoft.ctams.R;
import com.chinasoft.ctams.activity.addresssBook.ContactPhoneActivity;
import com.chinasoft.ctams.activity.addresssBook.adapter.AddressGroupAdapter;
import com.chinasoft.ctams.activity.addresssBook.bean.AddressBookSortModel;
import com.chinasoft.ctams.activity.addresssBook.bean.GroupManageBean;
import com.chinasoft.ctams.model.MyServerModel;
import com.chinasoft.ctams.util.ConstantCode;
import com.chinasoft.ctams.util.SharedPreferencesHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 说明:
 */
public class GroupManageAddressFragment extends Fragment {
    private View view;
    private ExpandableListView elv_address;
    public static Map<String,List<AddressBookSortModel>> map,defaultMap;
    private static List<String> parentList;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case ConstantCode.POST_SUCCESS:
                   Map<String,List<GroupManageBean>> map= (Map<String, List<GroupManageBean>>) msg.obj;
                    Log.i("info","Map的长度"+map.size());
                    loadPage(map);
                    break;
                case ConstantCode.POST_FAILED:
                    Toast.makeText(getActivity(),"网络异常,请检查网络",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.fragment_address_group_manage,container,false);
        elv_address=(ExpandableListView)view.findViewById(R.id.elv_address_group);
        String username= SharedPreferencesHelper.getInstance().getPeopleId(getActivity());
        MyServerModel.getInstance(getActivity()).loadGroupManageAddress(handler,username,getActivity());
        return view;
    }

    private void loadPage(Map<String,List<GroupManageBean>> map){
        parentList=new ArrayList<>();
          for (String key:map.keySet()){
              parentList.add(key);
              List<GroupManageBean> list=map.get(key);
          }
        final AddressGroupAdapter adapter=new AddressGroupAdapter(getActivity(),parentList,map);
        elv_address.setAdapter(adapter);
        elv_address.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                GroupManageBean bean= (GroupManageBean) adapter.getChild(groupPosition,childPosition);
                String name =bean.getPercommname();
                String phoneNumber =bean.getPercommtel();
                Intent intent = new Intent(getActivity(), ContactPhoneActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("phoneNumber", phoneNumber);
                startActivity(intent);
                return false;
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MyServerModel.getInstance(getActivity()).cancelTag(getActivity());

    }
}
