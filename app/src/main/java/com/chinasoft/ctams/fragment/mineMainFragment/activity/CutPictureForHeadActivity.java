package com.chinasoft.ctams.fragment.mineMainFragment.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chinasoft.ctams.R;
import com.chinasoft.ctams.base.BaseActivity;
import com.chinasoft.ctams.model.ServerModel;
import com.chinasoft.ctams.util.ConstantCode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by ls1213 on 2016/8/1.
 */

public class CutPictureForHeadActivity extends BaseActivity {

    private final String UPDATE_MINE_INFO="people/editMobile";


    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_head_menu)
    TextView tvHeadMenu;
    @Bind(R.id.iv_head_icon_prew)
    ImageView ivHeadIconPrew;
    private static int CAMERA_REQUEST_CODE = 1;
    private static int PHOTO_REQUEST_CODE = 2;
    private static int CROP_REQUEST_CODE=3;
    private File files;
    private ProgressDialog progressDialog;
    private Intent dataIntent = null;
    private boolean isLoading=true;
    private String path="";

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            progressDialog.dismiss();
            isLoading=false;
            progressDialog=null;
            if (msg.what== ConstantCode.POST_SUCCESS){
                Toast.makeText(CutPictureForHeadActivity.this,"上传成功！",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra("updateResult",path);
                setResult(7, intent);
                finish();

            }else {
                Toast.makeText(CutPictureForHeadActivity.this,"上传失败，请重试！",Toast.LENGTH_SHORT).show();

            }
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_cut_head_icon;
    }

    @Override
    public void initPage(Bundle savedInstanceState) {
        init();
        initData();
    }

    private void init() {
        tvTitle.setText("上传头像");
        tvHeadMenu.setVisibility(View.VISIBLE);
        tvHeadMenu.setText("上传");
    }

    private void initData() {

        File file=new File(Environment.getExternalStorageDirectory()+"/avater.png");
        if (file!=null){
            Bitmap bitmap=BitmapFactory.decodeFile(file.getPath());
            ivHeadIconPrew.setImageBitmap(bitmap);
        }else{
            ivHeadIconPrew.setImageResource(R.mipmap.a);
        }


    }


    @Override
    public void onBackPressed() {
        setResult(ConstantCode.POST_FAILED);
        super.onBackPressed();
    }

    @OnClick({R.id.iv_title_back, R.id.tv_head_menu, R.id.bt_take_photo_cut_head_icon, R.id.bt_photos_cut_head_icon})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                setResult(ConstantCode.POST_FAILED);
                finish();
                break;
            case R.id.tv_head_menu:
                if (files==null || path.equals("")){
                    Toast.makeText(CutPictureForHeadActivity.this,"请选择合适的图片！",Toast.LENGTH_SHORT).show();
                }else {
                    isLoading = true;
                    progressDialog = ProgressDialog.show(CutPictureForHeadActivity.this, "上传头像", "正在上传中...");
                    ServerModel.getInstance(CutPictureForHeadActivity.this).sendHeadIcon(handler, UPDATE_MINE_INFO, files, CutPictureForHeadActivity.this);
                }
                break;
            case R.id.bt_take_photo_cut_head_icon:
                doTakePhoto();
                break;
            case R.id.bt_photos_cut_head_icon:
                getPhotoFromPic();
                break;
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == CutPictureForHeadActivity.this.RESULT_OK) {
            switch (requestCode) {
                // 如果是直接从相册获取
                case 1:

                    startPhotoZoom(data.getData());
                    break;
                // 如果是调用相机拍照时
                case 2:
                    File temp = new File(Environment.getExternalStorageDirectory()
                            + "/xiaoma.jpg");
                    startPhotoZoom(Uri.fromFile(temp));
                    break;
                // 取得裁剪后的图片
                case 3:
                    if (data != null) {
                        dataIntent = data;
                        setPicToView(data);
                    }
                    break;
                default:
                    break;

            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * 裁剪图片方法实现
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        //下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);
    }

    /**
     * 保存裁剪之后的图片数据
     * @param picdata
     */
    private void setPicToView(Intent picdata) {
        Bundle extras = picdata.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            saveBitmapToFile(photo);
            ivHeadIconPrew.setImageBitmap(photo);

        }
    }
    private void saveBitmapToFile(Bitmap bitmap){
        Bitmap.CompressFormat format= Bitmap.CompressFormat.PNG;

        path=Environment.getExternalStorageDirectory()
                + "/avater.png";
        files = new File(path);
        int quality = 100;
        OutputStream stream = null;
        try {
            stream = new FileOutputStream(files);
            bitmap.compress(format,quality,stream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void doTakePhoto(){
        Intent intent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        //下面这句指定调用相机拍照后的照片存储的路径
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri
                .fromFile(new File(Environment
                        .getExternalStorageDirectory(),
                        "xiaoma.jpg")));
        startActivityForResult(intent, 2);
    }

    private void getPhotoFromPic(){
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        startActivityForResult(intent, 1);
    }


}
