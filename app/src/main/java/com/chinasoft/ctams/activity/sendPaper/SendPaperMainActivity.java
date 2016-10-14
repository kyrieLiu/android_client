package com.chinasoft.ctams.activity.sendPaper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.chinasoft.ctams.R;
import com.chinasoft.ctams.activity.scheduleManager.adapter.VpDateManagerAdapter;
import com.chinasoft.ctams.activity.sendPaper.activity.AddSendPaperActivity;
import com.chinasoft.ctams.activity.sendPaper.fragment.BackSendPaperFragment;
import com.chinasoft.ctams.activity.sendPaper.fragment.PassedSendPaperFragment;
import com.chinasoft.ctams.activity.sendPaper.fragment.UntreatedSendPaperFragment;
import com.chinasoft.ctams.base.BaseActivity;
import com.chinasoft.ctams.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 李硕 on 2016/6/26.
 * 值守管理主界面
 */
public class SendPaperMainActivity extends BaseActivity {


    @Bind(R.id.tv_tab_untreated_send_paper_list)
    TextView tvTabUntreatedSendPaperList;
    @Bind(R.id.tv_tab_passed_send_paper_list)
    TextView tvTabPassedSendPaperList;
    @Bind(R.id.tv_tab_back_send_paper_list)
    TextView tvTabBackSendPaperList;
    @Bind(R.id.vp_send_paper_list)
    ViewPager vpSendPaper;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_head_menu)
    TextView tv_addTask;

    private List<BaseFragment> fragmentList;
    private VpDateManagerAdapter vpDateManagerAdapter;
    private UntreatedSendPaperFragment untreatedSendPaperFragment;
    private PassedSendPaperFragment passedSendPaperFragment;
    private BackSendPaperFragment backSendPaperFragment;
    public static boolean isChange=false;
    @Override
    public int getLayoutId() {
        return R.layout.fragment_send_paper_list;
    }

    @Override
    public void initPage(Bundle savedInstanceState) {
        init();
        initData();
    }

    private void init() {
        tvTitle.setText("信息报送");
        tv_addTask.setText("新增");
        tv_addTask.setVisibility(View.VISIBLE);
        fragmentList = new ArrayList<>();
        initFragmentList();
        vpDateManagerAdapter = new VpDateManagerAdapter(getSupportFragmentManager(), fragmentList);
        vpSendPaper.setAdapter(vpDateManagerAdapter);
    }

    private void initFragmentList() {
        untreatedSendPaperFragment = new UntreatedSendPaperFragment();
        passedSendPaperFragment = new PassedSendPaperFragment();
        backSendPaperFragment = new BackSendPaperFragment();
        fragmentList.add(untreatedSendPaperFragment);
        fragmentList.add(passedSendPaperFragment);
        fragmentList.add(backSendPaperFragment);
    }

    private void initData() {
//        llTitlebarMainRightLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(getActivity(),AddSendPaperActivity.class);
//                intent.putExtra("isAdd",true);
//                startActivity(intent);
//            }
//        });
        tvTabUntreatedSendPaperList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isWhichCheck(0);
            }
        });
        tvTabPassedSendPaperList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isWhichCheck(1);
            }
        });
        tvTabBackSendPaperList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isWhichCheck(2);
            }
        });
        vpSendPaper.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                isWhichCheck(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    private void isWhichCheck(int which) {
        switch (which) {
            case 0:
                tvTabUntreatedSendPaperList.setTextColor(getResources().getColor(R.color.red));
                tvTabPassedSendPaperList.setTextColor(getResources().getColor(R.color.default_text_color));
                tvTabBackSendPaperList.setTextColor(getResources().getColor(R.color.default_text_color));
                vpSendPaper.setCurrentItem(0);
                break;
            case 1:
                tvTabUntreatedSendPaperList.setTextColor(getResources().getColor(R.color.default_text_color));
                tvTabPassedSendPaperList.setTextColor(getResources().getColor(R.color.mainColorBlue));
                tvTabBackSendPaperList.setTextColor(getResources().getColor(R.color.default_text_color));
                vpSendPaper.setCurrentItem(1);
                break;
            case 2:
                tvTabUntreatedSendPaperList.setTextColor(getResources().getColor(R.color.default_text_color));
                tvTabPassedSendPaperList.setTextColor(getResources().getColor(R.color.default_text_color));
                tvTabBackSendPaperList.setTextColor(getResources().getColor(R.color.red));
                vpSendPaper.setCurrentItem(2);
                break;
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.iv_title_back,R.id.tv_head_menu})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.tv_head_menu:
                startActivity(new Intent(this, AddSendPaperActivity.class));
                break;
        }

    }
}
