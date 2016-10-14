package com.chinasoft.ctams.activity.scheduleManager.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.chinasoft.ctams.base.BaseFragment;

import java.util.List;

/**
 * Created by Administrator on 2016/6/18.
 */
public class VpDateManagerAdapter extends FragmentPagerAdapter {
    private List<BaseFragment> list;
    public VpDateManagerAdapter(FragmentManager fm,List<BaseFragment> list) {
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

//    @Override
//    public boolean isViewFromObject(View view, Object object) {
//        return view==object;
//    }
@Override
public void destroyItem(ViewGroup container, int position, Object object) {
    return;
}
}
