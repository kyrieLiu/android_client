package com.chinasoft.ctams.fragment.mineMainFragment.activity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.chinasoft.ctams.R;
import com.chinasoft.ctams.base.BaseActivity;
import com.chinasoft.ctams.model.ServerModel;
import com.chinasoft.ctams.util.ConstantCode;
import com.chinasoft.ctams.view.ClearableEditTextWithIcon;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/6/20.
 */
public class UpdatePersonInfoActivity extends BaseActivity {
    private final String UPDATE_MINE_INFO = "people/editMobile";

    @Bind(R.id.et_update_person_info)
    ClearableEditTextWithIcon etUpdatePersonInfo;
    @Bind(R.id.bt_commit_update_person_info)
    Button btCommitUpdatePersonInfo;
    @Bind(R.id.tv_update_person_info)
    TextView tvUpdatePersonInfo;
    @Bind(R.id.tv_title_titlebar_main)
    TextView tvTitleTitlebarMain;
    @Bind(R.id.iv_left_titlebar_main_icon)
    ImageView ivLeftTitlebarMainIcon;
    @Bind(R.id.ll_titlebar_main_left_layout)
    LinearLayout llTitlebarMainLeftLayout;
    @Bind(R.id.rg_update_person_info)
    RadioGroup rgUpdatePersonInfo;
    @Bind(R.id.rbt_update_person_info)
    RadioButton rbtUpdatePersonInfo;
    @Bind(R.id.rbt_update_person_info1)
    RadioButton rbtUpdatePersonInfo1;
    @Bind(R.id.tv_birthday_updatee_mine)
    TextView tvBirthdayUpdateeMine;

    private String field;
    private String itemName;
    private String itemValue;
    private int position;
    private DatePickerDialog datePickerDialog;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == ConstantCode.POST_SUCCESS) {
                Intent intent = new Intent();
                intent.putExtra("updateResult", itemValue);
                setResult(position, intent);
                UpdatePersonInfoActivity.this.finish();
                Toast.makeText(getApplicationContext(), R.string.updata_success, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), R.string.updata_failed, Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.acitivity_update_person_info_data;
    }

    @Override
    public void initPage(Bundle savedInstanceState) {
        init();
        initData();
    }

    private void init() {
        field = getIntent().getStringExtra("field");
        itemName = getIntent().getStringExtra("itemName");
        itemValue = getIntent().getStringExtra("itemValue");
        position = getIntent().getIntExtra("position", 0);
        tvUpdatePersonInfo.setText(itemName + "：");
        tvTitleTitlebarMain.setText("修改个人信息");
        ivLeftTitlebarMainIcon.setImageResource(R.mipmap.title_back_icon);
        if (position == 1) {
            etUpdatePersonInfo.setVisibility(View.GONE);
            rgUpdatePersonInfo.setVisibility(View.VISIBLE);
            Log.i("tag", "____~" + itemValue);
            if (itemValue.equals("男")) {
                rbtUpdatePersonInfo.setChecked(true);
                rbtUpdatePersonInfo1.setChecked(false);
            } else {
                rbtUpdatePersonInfo.setChecked(false);
                rbtUpdatePersonInfo1.setChecked(true);
            }
        }else if (position == 2){
            etUpdatePersonInfo.setText(itemValue);
            etUpdatePersonInfo.setVisibility(View.GONE);
            tvBirthdayUpdateeMine.setVisibility(View.VISIBLE);
            tvBirthdayUpdateeMine.setText(itemValue);
            showDatePicker();
        }else {
            etUpdatePersonInfo.setText(itemValue);
            etUpdatePersonInfo.setSelection(itemValue.length());
        }
    }

    private void initData() {
        btCommitUpdatePersonInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 String reuslt= etUpdatePersonInfo.getText().toString();
                if (null==reuslt || reuslt.equals("")) {
                    Toast.makeText(UpdatePersonInfoActivity.this,"内容不能为空",Toast.LENGTH_SHORT).show();
                    etUpdatePersonInfo.setText(itemValue);
                }
//                else {
//                    itemValue = etUpdatePersonInfo.getText().toString();
//                }
                ServerModel.getInstance(UpdatePersonInfoActivity.this).upDatePersonInfo(UpdatePersonInfoActivity.this, handler, UPDATE_MINE_INFO, field, itemValue);
            }
        });
        llTitlebarMainLeftLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdatePersonInfoActivity.this.setResult(ConstantCode.POST_FAILED);
                finish();
            }
        });
        rgUpdatePersonInfo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == rbtUpdatePersonInfo.getId()) {
                    itemValue = rbtUpdatePersonInfo.getText().toString();
                    Log.i("tag", "____~" + itemValue);
                } else {
                    itemValue = rbtUpdatePersonInfo1.getText().toString();
                    Log.i("tag", "____~" + itemValue);
                }
            }
        });
    }

    private void showDatePicker(){
        Calendar calendar=Calendar.getInstance();
        datePickerDialog=new DatePickerDialog(this,null,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE,"完成", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DatePicker datePicker=datePickerDialog.getDatePicker();
                int year = datePicker.getYear();
                int month = datePicker.getMonth()+1;
                int day = datePicker.getDayOfMonth();
                itemValue=year+"-"+month+"-"+day;
                tvBirthdayUpdateeMine.setText(itemValue);
            }
        });
        datePickerDialog.show();
    }

    @Override
    public void onBackPressed() {
        this.setResult(ConstantCode.POST_FAILED);
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ServerModel.getInstance(this).cancelTag(this);

    }
}
