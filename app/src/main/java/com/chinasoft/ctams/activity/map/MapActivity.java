package com.chinasoft.ctams.activity.map;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.chinasoft.ctams.R;
import com.chinasoft.ctams.base.BaseActivity;
import com.chinasoft.ctams.util.SharedPreferencesHelper;
import com.chinasoft.ctams.view.CustomProgressDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Kyrie on 2016/6/28.
 * Email:kyrie_liu@sina.com
 */
public class MapActivity extends BaseActivity implements View.OnClickListener{

    @Bind(R.id.iv_title_back)
    ImageView iv_back;
    @Bind(R.id.tv_title)
    TextView tv_title;
    @Bind(R.id.tv_head_menu)
    TextView tv_menu;
    @Bind(R.id.wv_showMap)
    WebView webview;
    @Bind(R.id.bt_map_screen)
    Button bt_screen;

    private CustomProgressDialog dialog;
    private PopupWindow popupWindow;
    private String url;
    private String port;
    private TextView tv_search;
    private TextView tv_showNearTerminal;

    @Override
    public int getLayoutId() {
        return R.layout.activity_map;
    }

    @Override
    public void initPage(Bundle savedInstanceState) {
        tv_title.setText("地图查看");
        tv_menu.setVisibility(View.VISIBLE);
        tv_menu.setBackgroundResource(R.mipmap.icon_right_popup_menu);
        initPopupWindow();
        WebSettings webSettings = webview.getSettings();
        //设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);

        //设置可以访问文件
        webSettings.setAllowFileAccess(true);
        //设置支持缩放
        webSettings.setBuiltInZoomControls(true);
        webview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        SharedPreferences preferences = getSharedPreferences("ip", Context.MODE_PRIVATE);
        url = preferences.getString("ipNumber", "");
        port= SharedPreferencesHelper.getInstance().getPort(this);
        //加载需要显示的网页
        webview.loadUrl("http://" + url + ":"+port+"/ctams/mapJsp/map/androidSearch.jsp");

        //设置Web视图
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (dialog == null) {
                    dialog = CustomProgressDialog.createDialog(MapActivity.this);
                    dialog.setMessage("正在加载");
                }
                dialog.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (dialog!=null){
                    dialog.dismiss();
                }
            }
        });
    }


    @OnClick({R.id.iv_title_back, R.id.tv_head_menu, R.id.bt_map_screen})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.tv_head_menu:
                int[] location = new int[2];
                webview  .getLocationInWindow(location);
                int x = location[0];
                int y = location[1];
                popupWindow.showAtLocation(webview, Gravity.RIGHT | Gravity.TOP, 0,y);
                break;
            case R.id.tv_popup_item1:
                tv_title.setText("地图查看");

                popupWindow.dismiss();
                webview.loadUrl("http://" + url + ":"+port+"/ctams/mapJsp/map/androidSearch.jsp");
                break;
            case R.id.tv_popup_item2:
                tv_title.setText("移动终端");
                popupWindow.dismiss();
                webview.loadUrl("http://" + url + ":"+port+"/ctams/mapJsp/androidMap/androidOnleTerminal.jsp");
                break;
            case R.id.tv_popup_item3:
                tv_title.setText("视频终端");
                popupWindow.dismiss();
                webview.loadUrl("http://" + url + ":"+port+"/ctams/mapJsp/androidMap/androidVideoLocal.jsp");
                webview.setWebChromeClient(new WebChromeClient());
                //调用HTML中的jsObj接口实现交互
                webview.addJavascriptInterface(getHtmlObject(),"jsObj");
                break;
            case R.id.tv_popup_item4:
                tv_title.setText("地图绘制");
                bt_screen.setVisibility(View.VISIBLE);
                popupWindow.dismiss();
                webview.loadUrl("http://" + url + ":"+port+"/ctams/mapJsp/androidMap/androidPath.jsp");
                break;
            case R.id.bt_map_screen:
                GetandSaveCurrentImage();
                break;
        }
    }
    private void initPopupWindow() {
        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(this.LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.popup_view, null);
        popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.bg_black));
        tv_search = (TextView)popupView.findViewById(R.id.tv_popup_item1);
        tv_showNearTerminal = (TextView) popupView.findViewById(R.id.tv_popup_item2);
        TextView tv_video=(TextView)popupView.findViewById(R.id.tv_popup_item3);
        TextView tv_draw=(TextView)popupView.findViewById(R.id.tv_popup_item4);
        LinearLayout linearLayout=(LinearLayout)popupView.findViewById(R.id.ll_popup);
        LinearLayout.LayoutParams params= (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
        params.height=600;
        linearLayout.setLayoutParams(params);

        tv_search.setText("地图查看");
        tv_search.setOnClickListener(this);
        tv_showNearTerminal.setText("移动终端");
        tv_showNearTerminal.setOnClickListener(this);
        tv_showNearTerminal.setVisibility(View.VISIBLE);
        tv_video.setText("视频终端");
        tv_video.setVisibility(View.VISIBLE);
        tv_draw.setText("地图绘制");
        tv_draw.setVisibility(View.VISIBLE);
        tv_draw.setOnClickListener(this);
        tv_video.setOnClickListener(this);

    }
    /**
     * 获取和保存当前屏幕的截图
     */
    private void GetandSaveCurrentImage()
    {
        //1.构建Bitmap
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int w = display.getWidth();
        int h = display.getHeight();

        Bitmap bmp = Bitmap.createBitmap( w, h, Bitmap.Config.ARGB_8888 );

        //2.获取屏幕
        View decorview = this.getWindow().getDecorView();
        decorview.setDrawingCacheEnabled(true);
        bmp = decorview.getDrawingCache();


        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "地图截屏");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            Toast.makeText(this,"截屏保存成功",Toast.LENGTH_SHORT).show();
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(this.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getAbsolutePath())));
    }

    /**
     * 调用HTML中jsObj接口
     */
    private Object getHtmlObject(){
        Object object=new Object(){
            @JavascriptInterface
          public void JavaCallBack(final String videoPort,final String videoUrl,final String videoUser,final String password,final String videoName){
                Log.i("info","videoPort "+videoPort+" vidoeUrl "+videoUrl+" videoUser "+videoUser+" password "+password+" videoName "+videoName);
               MonitorVideoDialog dialog=new MonitorVideoDialog(MapActivity.this,videoPort,videoUrl,videoUser,password,videoName);
                dialog.show();
                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        dialog.dismiss();
                    }
                });

          }
        };
                return object;
    }

}
