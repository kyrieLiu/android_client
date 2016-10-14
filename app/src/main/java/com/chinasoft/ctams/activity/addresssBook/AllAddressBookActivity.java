package com.chinasoft.ctams.activity.addresssBook;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.chinasoft.ctams.R;
import com.chinasoft.ctams.activity.addresssBook.adapter.AddressbookViewPageAdapter;
import com.chinasoft.ctams.activity.addresssBook.fragment.GroupManageAddressFragment;
import com.chinasoft.ctams.activity.addresssBook.fragment.HistoricalRecordFragment;
import com.chinasoft.ctams.activity.addresssBook.fragment.NativeAddressFragment;
import com.chinasoft.ctams.activity.addresssBook.fragment.OftenContactAddressFragment;
import com.chinasoft.ctams.activity.addresssBook.fragment.OrganizeAddressFragment;
import com.chinasoft.ctams.activity.addresssBook.fragment.PersonalAddressFragment;
import com.chinasoft.ctams.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Administrator on 2016/6/23.
 */
public class AllAddressBookActivity extends BaseActivity {
    @Bind(R.id.tl_address_book)
    TabLayout tabLayout;
    @Bind(R.id.vp_address_book)
    ViewPager viewPager;

    private List<Fragment> fragmentList;
    private String flag;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_address_book_main;
    }

    @Override
    public void initPage(Bundle savedInstanceState) {
        flag=getIntent().getStringExtra("flag")+"";
        Log.i("info","获取的flag"+flag);
        init();

    }

    private void init(){
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        fragmentList = new ArrayList<>();
        NativeAddressFragment nativeFragment=new NativeAddressFragment();
        PersonalAddressFragment personFragment=new PersonalAddressFragment();
        OrganizeAddressFragment organizeFragment=new OrganizeAddressFragment();
        OftenContactAddressFragment oftenContactFragment=new OftenContactAddressFragment();
        GroupManageAddressFragment groupManageFragment=new GroupManageAddressFragment();
        HistoricalRecordFragment historicalRecordFragment=new HistoricalRecordFragment();
        fragmentList.add(personFragment);
        fragmentList.add(nativeFragment);
        fragmentList.add(organizeFragment);
        fragmentList.add(groupManageFragment);
        fragmentList.add(oftenContactFragment);
        fragmentList.add(historicalRecordFragment);
        AddressbookViewPageAdapter adapter = new AddressbookViewPageAdapter(this.getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabsFromPagerAdapter(adapter);

        if (flag.equals("gr")){
            viewPager.setCurrentItem(0);
        }
        else if (flag.equals("jg")){
            viewPager.setCurrentItem(2);
        }
        else if (flag.equals("gg")){
            viewPager.setCurrentItem(3);
        }

    }

}
