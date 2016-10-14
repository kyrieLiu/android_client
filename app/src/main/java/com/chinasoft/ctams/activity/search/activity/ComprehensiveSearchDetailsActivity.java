package com.chinasoft.ctams.activity.search.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chinasoft.ctams.R;
import com.chinasoft.ctams.activity.search.bean.ComprehensiveSearchBean;
import com.chinasoft.ctams.base.BaseActivity;
import com.chinasoft.ctams.model.MyServerModel;
import com.chinasoft.ctams.util.ConstantCode;

import java.io.File;

import butterknife.Bind;
import butterknife.OnClick;

public class ComprehensiveSearchDetailsActivity extends BaseActivity {

    @Bind(R.id.iv_title_back)
    ImageView iv_back;
    @Bind(R.id.tv_title)
    TextView tv_title;
    @Bind(R.id.tv_comprehensive_search_title)
    TextView tv_searchTitle;
    @Bind(R.id.tv_comprehensive_search_type)
    TextView tv_searchType;
    @Bind(R.id.tv_comprehensive_search_creater)
    TextView tv_searchCreater;
    @Bind(R.id.tv_comprehensive_search_time)
    TextView tv_searchTime;
    @Bind(R.id.bt_comprehensive_search_word)
    Button bt_download;
    @Bind(R.id.tv_comprehensive_word_name)
    TextView tv_word_name;

    private static final int MAX_PROGRESS=100;
    private int progress=0;
    private ProgressDialog progressDialog;
    private String searchTitle="wordName";
    private String wordUrl;


    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case ConstantCode.GET_DOWNLOAD:
                    progress=msg.arg1;
                    progressDialog.setProgress(progress);
                    break;
                case ConstantCode.POST_SUCCESS:
                   progressDialog.setTitle("下载完成");
                    progressDialog.setMessage("");

                    File file= (File) msg.obj;
                    Log.i("info","传输来的file"+file.getPath());
//                    File f=new File(Environment.getExternalStorageDirectory(),"word文档");
//                    File fp=new File(f,"tt.doc");
//                    Log.i("info","fp路径是"+fp.getPath());

                    openWord(file);

                    break;
                case ConstantCode.POST_FAILED:
                    Toast.makeText(ComprehensiveSearchDetailsActivity.this,"文件下载失败,请检查网络!",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };



    @Override
    public int getLayoutId() {
        return R.layout.activity_comprehensive_search_details;
    }

    @Override
    public void initPage(Bundle savedInstanceState) {
        tv_title.setText("查询详情");
        Bundle bundle=getIntent().getExtras();
        ComprehensiveSearchBean bean= (ComprehensiveSearchBean) bundle.getSerializable("bean");
        searchTitle = bean.getLTitle();
        tv_searchTitle.setText(searchTitle);
        tv_searchCreater.setText(bean.getLFounder());
        tv_searchTime.setText(bean.getLTimer());
        tv_searchType.setText(bean.getLType());
        tv_word_name.setText(searchTitle +".doc");
        wordUrl=bean.getLwordurl();
    }

    @OnClick({R.id.iv_title_back, R.id.bt_comprehensive_search_word})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.bt_comprehensive_search_word:
                MyServerModel.getInstance(this).downLoadWord(handler,searchTitle,wordUrl,this);
                initProgressDialog();
                break;
        }
    }

    private void openWord(File file){

        Intent intent = new Intent("android.intent.action.VIEW");

        intent.addCategory("android.intent.category.DEFAULT");

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Uri uri = Uri.fromFile(file);

        intent.setDataAndType(uri, "application/msword");
        startActivity(intent);
    }

    /**
     * 初始化进度条
     */
    private void initProgressDialog(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setIcon(R.drawable.icon_doc);
        progressDialog.setTitle("正在下载...");
        progressDialog.setMessage("请稍后");
        progressDialog.setMax(MAX_PROGRESS);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//        progressDialog.setButton("暂停", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                handler.removeMessages(ConstantCode.GET_DOWNLOAD);
//            }
//        });
        progressDialog.setButton("关闭", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                handler.removeMessages(ConstantCode.GET_DOWNLOAD);
//                progress=0;
//                progressDialog.setProgress(progress);
                MyServerModel.getInstance(ComprehensiveSearchDetailsActivity.this).cancelTag(ComprehensiveSearchDetailsActivity.this);
                progressDialog.dismiss();
            }
        });
        progressDialog.show();
        progress=(progress>0)?progress:0;
        progressDialog.setProgress(progress);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        progressDialog=null;
    }
}
