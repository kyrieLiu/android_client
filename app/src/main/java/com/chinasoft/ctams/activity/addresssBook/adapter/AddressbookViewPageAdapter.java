package com.chinasoft.ctams.activity.addresssBook.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * 说明:
 */
public class AddressbookViewPageAdapter extends FragmentPagerAdapter {
    private List<Fragment> list;
    private String []name={"个人通讯录","本地通讯录","机构通讯录","群组管理","常用联系人","历史记录"};

    public AddressbookViewPageAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.list=list;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return name[position];
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        return;
    }
}
