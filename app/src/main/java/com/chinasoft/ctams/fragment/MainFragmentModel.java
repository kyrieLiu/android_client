package com.chinasoft.ctams.fragment;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.chinasoft.ctams.R;
import com.chinasoft.ctams.fragment.subjectFragment.SubjectMainFragment;
import com.chinasoft.ctams.fragment.homePageFragment.HomePageFragment;
import com.chinasoft.ctams.fragment.mediafragment.monitorList.MonitorVideoListFragment;
import com.chinasoft.ctams.fragment.mineMainFragment.PersonMainFragment;

/**
 * Created by Administrator on 2016/6/16.
 */
public class MainFragmentModel {
    private FragmentManager fragmentManager;
    private SubjectMainFragment subjectMainFragment;//专题图
    private PersonMainFragment detailPersonalDataFragment;//个人中心信息展示
    private HomePageFragment homePageFragment;//首页
    private MonitorVideoListFragment monitorVideoListFragment;//实时监控
    private FragmentTransaction transaction;

    //切换四个fragment
    public void changeFragment(int number,MainFragment mainFragment) {
        fragmentManager = mainFragment.getActivity().getSupportFragmentManager();
        setTabSelection(number);
    }

    //管理Fragment
    private void setTabSelection(int index) {
        transaction = fragmentManager.beginTransaction();
        hindFragment();
        switch (index) {
            case 0:
                if (homePageFragment == null) {
                    homePageFragment =HomePageFragment.getInstance();
                    transaction.add(R.id.fl_mainActivity_container, homePageFragment);
                } else {
                    transaction.show(homePageFragment);
                }
                break;
            case 1:
                if (subjectMainFragment ==null){
                    subjectMainFragment = SubjectMainFragment.getInstance();
                    transaction.add(R.id.fl_mainActivity_container, subjectMainFragment);
                }else{
                    transaction.show(subjectMainFragment);
                }
                break;
            case 2:
                if (monitorVideoListFragment == null) {
                    monitorVideoListFragment = MonitorVideoListFragment.getInstance();
                    transaction.add(R.id.fl_mainActivity_container, monitorVideoListFragment);
                } else {
                    transaction.show(monitorVideoListFragment);
                }
                break;
            case 3:
                if (detailPersonalDataFragment == null) {
                    detailPersonalDataFragment = PersonMainFragment.getInstance();
                    transaction.add(R.id.fl_mainActivity_container, detailPersonalDataFragment);
                } else {
                    transaction.show(detailPersonalDataFragment);
                }
                break;
            default:
                break;
        }
        transaction.commit();
    }

    // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
    private void hindFragment() {
        if (detailPersonalDataFragment != null) {
            transaction.hide(detailPersonalDataFragment);
        }
        if (subjectMainFragment != null) {
            transaction.hide(subjectMainFragment);
        }
        if(homePageFragment!=null){
            transaction.hide(homePageFragment);
        }
        if (monitorVideoListFragment!=null){
            transaction.hide(monitorVideoListFragment);
        }
    }

    /**
     * 清除所有framgnent
     */
    public void clearFragment(){
        if (detailPersonalDataFragment != null) {
            transaction.remove(detailPersonalDataFragment);
        }
        if (subjectMainFragment != null) {
            transaction.remove(subjectMainFragment);
        }
        if(homePageFragment!=null){
            transaction.remove(homePageFragment);
        }
        if (monitorVideoListFragment!=null){
            transaction.remove(monitorVideoListFragment);
        }
    }
}
