package com.chinasoft.ctams.activity.patrol;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chinasoft.ctams.R;
import com.chinasoft.ctams.base.BaseActivity;
import com.chinasoft.ctams.model.MyServerModel;
import com.chinasoft.ctams.util.ConstantCode;
import com.chinasoft.ctams.util.SharedPreferencesHelper;
import com.chinasoft.ctams.view.CustomProgressDialog;

import java.io.File;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Kyrie on 2016/7/2.
 * Email:kyrie_liu@sina.com
 */
public class PatrolFeedBackActivity extends BaseActivity implements View.OnClickListener{
    @Bind(R.id.iv_title_back)
    ImageView iv_back;
    @Bind(R.id.tv_title)
    TextView tv_title;
    @Bind(R.id.tv_patrol_feedBack_troopsName)
    TextView tv_troopsName;
    @Bind(R.id.tv_patrol_feedBack_routeName)
    TextView tv_routeName;
    @Bind(R.id.et_patrol_feedBack_placeName)
    EditText et_placeName;
    @Bind(R.id.et_patrol_feedBack_content)
    EditText et_content;
    @Bind(R.id.bt_patrol_feedBack_selectPhoto)
    Button bt_selectPhoto;
    @Bind(R.id.iv_patrol_feedback_showPhoto)
    ImageView iv_showPhoto;
    @Bind(R.id.bt_patrol_feedBack_commit)
    Button bt_commit;
    @Bind(R.id.bt_patrol_feedBack_cancel)
    Button bt_cancel;
    @Bind(R.id.ll_patrol_feedBack)
    LinearLayout linearLayout;
    @Bind(R.id.sv_patrol_feedBack)
    ScrollView scrollView;
    @Bind(R.id.sp_patrol_feedBack_type)
    Spinner sp_type;

    private PopupWindow popupWindow;
    private File file ;
    private String planName;
    private CustomProgressDialog dialog;
    private String placeName;
    private String placeType;
    private String planId;
    private String teamId;
    private String routeId;
    private int num;
    private List<String> list;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(dialog!=null){
                dialog.dismiss();
            }
            switch (msg.what){
                case ConstantCode.POST_SUCCESS:
                    Toast.makeText(PatrolFeedBackActivity.this,"上传成功",Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case ConstantCode.POST_FAILED:
                    Toast.makeText(PatrolFeedBackActivity.this,"上传失败,请检查网络",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };


    @Override
    public int getLayoutId() {
        return R.layout.activity_patrol_feedback;
    }

    @Override
    public void initPage(Bundle savedInstanceState) {

        Bundle bundle=getIntent().getExtras();
        String routeName=bundle.getString("routeName");
        String teamName=bundle.getString("teamName");
        tv_troopsName.setText(teamName+"");
        tv_routeName.setText(routeName+"");
        planName =bundle.getString("planName");
        planId=bundle.getString("planId");
        teamId=bundle.getString("processTeamId");
        routeId=bundle.getString("processRouteId");
        num=bundle.getInt("num",0);
        initPopupWindow();//初始化popupWindow
        initSpinner();
    }


    @OnClick({R.id.iv_title_back, R.id.bt_patrol_feedBack_selectPhoto, R.id.bt_patrol_feedBack_commit,
            R.id.bt_patrol_feedBack_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.bt_patrol_feedBack_selectPhoto:
                popupWindow.showAtLocation(linearLayout, Gravity.BOTTOM,0,0);
                break;
            case R.id.bt_patrol_feedBack_commit:
                updateData();
                break;
            case R.id.bt_patrol_feedBack_cancel:
                finish();
                break;
            //照相机
            case R.id.tv_popup_patrol_feedBack_camera:
                Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Uri imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"image.jpg"));
                //指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
                openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(openCameraIntent, ConstantCode.CAMERA_REQUEST_CODE);
                break;
            //本地图片
            case R.id.tv_popup_patrol_feedBack_native:
                Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
                startActivityForResult(intent, ConstantCode.IMAGE_REQUEST_CODE);

                break;
            case R.id.tv_popup_patrol_feedBack_cancel:
                popupWindow.dismiss();
                break;
        }
    }
    private void initPopupWindow(){
        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(this.LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.popup_patro_feedback_select_pictrue, null);
        popupWindow = new PopupWindow(popupView, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.bg_black));
        TextView tv_camera = (TextView) popupView.findViewById(R.id.tv_popup_patrol_feedBack_camera);
        TextView tv_native = (TextView) popupView.findViewById(R.id.tv_popup_patrol_feedBack_native);
        TextView tv_cancel = (TextView) popupView.findViewById(R.id.tv_popup_patrol_feedBack_cancel);
        tv_camera.setOnClickListener(this);
        tv_native.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);

    }
    /**
     * 上传反馈资料
     */
    private void updateData(){
        String placeName=et_placeName.getText().toString();
        String content=et_content.getText().toString();
        SharedPreferencesHelper helper= SharedPreferencesHelper.getInstance();
        String longitude=helper.getLocation(this).get("longitude");
        String latitude=helper.getLocation(this).get("latitude");
        String coordinate=longitude+","+latitude;

        if (dialog==null){
            dialog= CustomProgressDialog.createDialog(this);
            dialog.setMessage("正在上传");
        }
        dialog.show();
       MyServerModel.getInstance(this).commitFeedBack(handler,file,teamId,routeId,placeName,placeType,content,coordinate, planId,num,this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == PatrolFeedBackActivity.this.RESULT_OK) {//照片返回码 。
            switch (requestCode) {
                case ConstantCode.IMAGE_REQUEST_CODE:
                    //照片的原始资源地址
                    Uri uri = data.getData();
                    if ( null == uri )
                        return ;
                    final String scheme = uri.getScheme();
                    String path = null;
                    if ( scheme == null )
                        path = uri.getPath();
                    else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
                        path = uri.getPath();
                    } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
                        Cursor cursor = PatrolFeedBackActivity.this.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
                        if ( null != cursor ) {
                            if ( cursor.moveToFirst() ) {
                                int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
                                if ( index > -1 ) {
                                    path = cursor.getString( index );
                                }
                            }
                            cursor.close();
                        }
                    }
                    file=new File(path);
//                    Bitmap bitmap_native= BitmapFactory.decodeFile(file.getPath());
//                    if (bitmap_native != null) {
//                        Bitmap smallBitmap = ImageTools.zoomBitmap(bitmap_native,
//                                bitmap_native.getWidth() / 5, bitmap_native.getHeight()
//                                        / 5);
//                        bitmap_native.recycle();
//
//
//                        iv_showPhoto.setImageBitmap(smallBitmap);
//                    }

                    Glide.with(PatrolFeedBackActivity.this).load(file).into(iv_showPhoto);

                    break;
                case ConstantCode.CAMERA_REQUEST_CODE:
                    file = new File(Environment.getExternalStorageDirectory()
                            + "/image.jpg");
                    Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());

                    if (bitmap != null) {
                        Bitmap smallBitmap = ImageTools.zoomBitmap(bitmap,
                                bitmap.getWidth() / 5, bitmap.getHeight()
                                        / 5);
                        bitmap.recycle();

                        iv_showPhoto.setImageBitmap(smallBitmap);
                    }

                    break;

                default:
                    break;
            }

        }
        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
        popupWindow.dismiss();
        }
    /**
     * 初始化地点类型spinner
     */

    private void initSpinner(){
        final String[] arrType = {"重点检测点", "非重点检测点"};
        ArrayAdapter typeAdapter = new ArrayAdapter(this, R.layout.sp_item_patrol, arrType);
        sp_type.setAdapter(typeAdapter);
        sp_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                placeType = arrType[position] + "";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dialog=null;
        MyServerModel.getInstance(this).cancelTag(this);
    }
}
