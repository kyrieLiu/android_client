package com.chinasoft.ctams.fragment;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.chinasoft.ctams.R;
import com.chinasoft.ctams.base.BaseFragment;

import butterknife.Bind;

/**
 * Created by Administrator on 2016/6/16.
 */
public class MainFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener {
    @Bind(R.id.rg_mainActivity)
    RadioGroup rgMainActivity;
    @Bind(R.id.rb_mainActiviy_date)
    RadioButton rb_date;
    @Bind(R.id.rb_mainActivity_addressBook)
    RadioButton rb_addressBook;
    private MainFragmentModel mainFragmentModel;
    private MainFragment mainFragment;
    private TextView titlebar;


    public static MainFragment getInstance(){
        MainFragment mainFragment=new MainFragment();
        return mainFragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    public void initPage(Bundle savedInstanceState) {
        mainFragmentModel = new MainFragmentModel();
        mainFragment = this;

            mainFragmentModel.changeFragment(0, mainFragment);
        rgMainActivity.setOnCheckedChangeListener(this);
        titlebar= (TextView) getActivity().findViewById(R.id.tv_title_titlebar_main);
    }
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            //首页
            case R.id.rb_mainActivity_homePage:
                mainFragmentModel.changeFragment(0, mainFragment);
                titlebar.setText(R.string.home_page_titlebar);
                break;
            //视频列表
            case R.id.rb_mainActiviy_date:
                mainFragmentModel.changeFragment(1, mainFragment);
                titlebar.setText("专题图");
                break;
            //值守管理
            case R.id.rb_mainActivity_addressBook:
                mainFragmentModel.changeFragment(2, mainFragment);
                titlebar.setText("实时监控");
                break;
            //个人信息中心
            case R.id.rb_mainActivity_mine:
                mainFragmentModel.changeFragment(3, mainFragment);
                titlebar.setText(R.string.radioButton_main_mine);
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mainFragmentModel!=null){
            mainFragmentModel.clearFragment();
            mainFragmentModel=null;
        }
    }
}
