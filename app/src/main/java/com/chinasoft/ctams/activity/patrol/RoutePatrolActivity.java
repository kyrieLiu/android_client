package com.chinasoft.ctams.activity.patrol;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinasoft.ctams.R;
import com.chinasoft.ctams.base.BaseActivity;
import com.chinasoft.ctams.view.CustomProgressDialog;

import butterknife.Bind;
import butterknife.OnClick;

public class RoutePatrolActivity extends BaseActivity {

    @Bind(R.id.iv_title_back)
    ImageView iv_back;
    @Bind(R.id.tv_title)
    TextView tv_title;
    @Bind(R.id.webView_route_patrol)
    WebView webview;
    private CustomProgressDialog dialog;

    @OnClick(R.id.iv_title_back)
    public void onClick() {
        finish();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_route_patrol;
    }

    @Override
    public void initPage(Bundle savedInstanceState) {
        tv_title.setText("巡逻路线");
        WebSettings webSettings = webview.getSettings();
        //设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        //设置可以访问文件
        webSettings.setAllowFileAccess(true);
        //设置支持缩放
        webSettings.setBuiltInZoomControls(true);
        SharedPreferences preferences = getSharedPreferences("ip", Context.MODE_PRIVATE);
        String url = preferences.getString("ipNumber", "");
        String routeUrl = getIntent().getStringExtra("routeUrl");

        Log.i("info", "获取的路径url" + url + routeUrl);
        if (routeUrl != null) {
            //加载需要显示的网页
            webview.loadUrl("http://" + url + routeUrl);

            //设置Web视图
            webview.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    if (dialog == null) {
                        dialog = CustomProgressDialog.createDialog(RoutePatrolActivity.this);
                        dialog.setMessage("正在加载");
                    }
                    dialog.show();
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    dialog.dismiss();
                }
            });
        }


    }
}
