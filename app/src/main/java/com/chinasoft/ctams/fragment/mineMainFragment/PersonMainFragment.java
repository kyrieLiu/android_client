package com.chinasoft.ctams.fragment.mineMainFragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chinasoft.ctams.R;
import com.chinasoft.ctams.base.BaseFragment;
import com.chinasoft.ctams.fragment.mineMainFragment.activity.CutPictureForHeadActivity;
import com.chinasoft.ctams.fragment.mineMainFragment.activity.UpdatePersonInfoActivity;
import com.chinasoft.ctams.fragment.mineMainFragment.bean.UserInfoBean;
import com.chinasoft.ctams.model.ServerModel;
import com.chinasoft.ctams.util.ConstantCode;
import com.chinasoft.ctams.view.CircleImageView;
import com.chinasoft.ctams.view.CustomProgressDialog;

import java.io.File;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/6/16.
 */
public class PersonMainFragment extends BaseFragment {

    //个人信息
    private final String MINE_INFO="people/showOnePeople";

    private static PersonMainFragment fragment;
    @Bind(R.id.iv_headicon_mine_info)
    CircleImageView ivHeadiconMineInfo;
    @Bind(R.id.tv_account_mine_info)
    TextView tvAccountMineInfo;
    @Bind(R.id.tv_name_item_mine_info_main)
    TextView tvNameItemMineInfoMain;

    @Bind(R.id.tv_sex_item_mine_info_main)
    TextView tvSexItemMineInfoMain;

    @Bind(R.id.tv_birthday_item_mine_info_main)
    TextView tvBirthdayItemMineInfoMain;
    @Bind(R.id.rl_birthday_mine_info)
    RelativeLayout rlBirthdayMineInfo;
    @Bind(R.id.tv_national_item_mine_info_main)
    TextView tvNationalItemMineInfoMain;


    @Bind(R.id.tv_organization_item_mine_info_main)
    TextView tvOrganizationItemMineInfoMain;

    @Bind(R.id.tv_phone_item_mine_info_main)
    TextView tvPhoneItemMineInfoMain;

    private CustomProgressDialog dialog;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (dialog!=null)
            dialog.dismiss();
            if (msg.what == ConstantCode.POST_SUCCESS) {
                UserInfoBean userInfoBean= (UserInfoBean) msg.obj;
                setUserInfoBean(userInfoBean);
            }else {
                Toast.makeText(getActivity(),"获取资料失败,请检查网络!",Toast.LENGTH_SHORT).show();
            }
        }
    };


    public static PersonMainFragment getInstance() {
        if (fragment == null) {
            fragment = new PersonMainFragment();
        }
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_person_main;
    }

    @Override
    public void initPage(Bundle savedInstanceState) {
        initData();
    }

    private void initData() {
        ServerModel.getInstance(getActivity()).getPersonInfo(getActivity(),handler,MINE_INFO);
        if (dialog==null){
            dialog= CustomProgressDialog.createDialog(getActivity());
            dialog.setMessage("正在加载...");
        }
        dialog.show();

    }

    @Override
    public void onResume() {
        super.onResume();
    }



    private void setUserInfoBean(UserInfoBean userInfoBean){

        File file=new File(Environment.getExternalStorageDirectory()+"/avater.png");
        Bitmap bitmap=BitmapFactory.decodeFile(file.getPath());
        if (bitmap!=null){
            Log.i("info","file文件"+file+"  bitmap "+bitmap);
            ivHeadiconMineInfo.setImageBitmap(bitmap);
        }else{
            ivHeadiconMineInfo.setImageResource(R.mipmap.app_logo);
        }

        tvNameItemMineInfoMain.setText(userInfoBean.getPeopleName());
        tvSexItemMineInfoMain.setText(userInfoBean.getPeopleSex());
        tvNationalItemMineInfoMain.setText(userInfoBean.getPeopleNational());
        tvBirthdayItemMineInfoMain.setText(userInfoBean.getPeopleDate());
        tvOrganizationItemMineInfoMain.setText(userInfoBean.getPeopleOrganization());
        tvPhoneItemMineInfoMain.setText(userInfoBean.getPeoplePhone());
        tvAccountMineInfo.setText(userInfoBean.getPeopleLoginname());
    }

    @OnClick({ R.id.iv_headicon_mine_info,R.id.rl_phone_mine_info})
    public void onClick(View view) {
        Intent intent=new Intent(getActivity(),UpdatePersonInfoActivity.class);
        switch (view.getId()) {
            case R.id.iv_headicon_mine_info:
                Intent intent1=new Intent(getActivity(),CutPictureForHeadActivity.class);

                startActivityForResult(intent1,200);
                break;

            case R.id.rl_phone_mine_info:
                intent.putExtra("field","people_Phone");
                intent.putExtra("itemName","联系方式");
                intent.putExtra("itemValue",tvPhoneItemMineInfoMain.getText().toString());
                intent.putExtra("position",6);
                startActivityForResult(intent,200);
                break;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!(ConstantCode.POST_FAILED==resultCode)) {
            String updateResult = data.getStringExtra("updateResult");
            switch (resultCode) {

                case 6:
                    tvPhoneItemMineInfoMain.setText(updateResult);
                    break;
                case 7:
                    File file=new File(Environment.getExternalStorageDirectory()
                            + "/avater.png");
                    Bitmap bitmap= BitmapFactory.decodeFile(file.getPath());
                    ivHeadiconMineInfo.setImageBitmap(bitmap);
                    break;
            }
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
            dialog=null;
        ServerModel.getInstance(getActivity()).cancelTag(getActivity());
    }
}
