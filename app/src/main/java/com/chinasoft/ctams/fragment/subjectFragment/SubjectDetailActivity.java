package com.chinasoft.ctams.fragment.subjectFragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chinasoft.ctams.R;
import com.chinasoft.ctams.fragment.subjectFragment.adapter.TopicViewPagerAdapter;
import com.chinasoft.ctams.base.BaseActivity;
import com.chinasoft.ctams.util.TouchListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import sql.TopicMessage;

/**
 * Created by ls1213 on 2016/8/11.
 */

public class SubjectDetailActivity extends BaseActivity {

    @Bind(R.id.vp_subject_show)
    ViewPager viewpager;
    @Bind(R.id.iv_subject_return)
    ImageView iv_back;
    private String srcUrl;
    private String topicTitle;
    private String topicTime;
    private boolean isPush = false;
    private List<TopicMessage> topicBeanList;
    private String url;
    private ImageView imageView;
    private Bitmap bitmap;

    @Override
    public int getLayoutId() {
        return R.layout.activity_subject_show_main;
    }

    @Override
    public void initPage(Bundle savedInstanceState) {
        Intent intent=getIntent();
        isPush=intent.getBooleanExtra("isPush",false);
        if (isPush) {
            topicBeanList = formatStrToView(intent.getStringExtra("ztt"));
            setData(topicBeanList);
        } else {
            topicBeanList=new ArrayList<>();
            srcUrl = intent.getStringExtra("topicUrl");
            topicTitle = intent.getStringExtra("topicTitle");
            topicTime = intent.getStringExtra("topicTime");
            TopicMessage message=new TopicMessage();
            message.setTopicSrc(srcUrl);
            message.setTopicTime(topicTime);
            message.setTopicTitle(topicTitle);
           topicBeanList.add(message);
            setData(topicBeanList);
        }
    }



    private List<TopicMessage> formatStrToView(String result) {
        List<TopicMessage> list = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                TopicMessage topicMessage = new TopicMessage();
                topicMessage.setTopicId(jsonObject.getString("thematicmapId"));
                topicMessage.setTopicSrc(jsonObject.getString("thematicmapUrl"));
                topicMessage.setTopicTitle(jsonObject.getString("thematicmapName"));
                topicMessage.setTopicTime(jsonObject.getString("thematicmapDate"));
                topicMessage.save();
                list.add(topicMessage);
            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
            return list;
        }
    }


    @OnClick({ R.id.iv_subject_return})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_subject_return:
                finish();
                break;
        }
    }
    private  Handler  handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.i("info","加载图片");
            if (msg.what==1){
                imageView.setImageBitmap(bitmap);
            }
        }
    };
    /**
     * 加载专题图图片
     */
    private void setData(final List<TopicMessage> topicList){
        List<View> list = new ArrayList<>();
        int size=topicList.size();
        for (int i = 0; i < size; i++) {
            StringBuilder builder = new StringBuilder();
            View view = LayoutInflater.from(this).inflate(R.layout.item_vp_subject_show, null);
            imageView = (ImageView) view.findViewById(R.id.ziv_subject_show);
            TextView textView = (TextView) view.findViewById(R.id.tv_subject_title);
            TextView tv_number=(TextView)view.findViewById(R.id.tv_subject_number_sign);
            url=topicList.get(i).getTopicSrc();

            Glide.with(this).load(url).into(imageView);
            imageView.setOnTouchListener(new TouchListener(imageView));

            builder.append(topicList.get(i).getTopicTime());
            builder.append("    " + topicList.get(i).getTopicTitle());
            textView.setText(builder);
            if (size>1){
                tv_number.setVisibility(View.VISIBLE);
                tv_number.setText(i+1+"/"+size);
            }

            list.add(view);
        }
        TopicViewPagerAdapter adapter = new TopicViewPagerAdapter(list);
        viewpager.setAdapter(adapter);
    }



}
