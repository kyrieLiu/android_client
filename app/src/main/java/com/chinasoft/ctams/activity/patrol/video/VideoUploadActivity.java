package com.chinasoft.ctams.activity.patrol.video;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.chinasoft.ctams.R;
import com.chinasoft.ctams.base.BaseActivity;
import com.chinasoft.ctams.model.MyServerModel;
import com.chinasoft.ctams.util.ConstantCode;
import com.chinasoft.ctams.util.SharedPreferencesHelper;

import java.io.File;

import butterknife.Bind;
import butterknife.OnClick;

public class VideoUploadActivity extends BaseActivity {

    @Bind(R.id.iv_title_back)
    ImageView iv_back;
    @Bind(R.id.tv_title)
    TextView tv_title;
    @Bind(R.id.et_video_upload_title)
    EditText et_title;
    @Bind(R.id.et_video_upload_content)
    EditText et_content;
    @Bind(R.id.bt_video_upload_vcr)
    Button bt_Vcr;
    @Bind(R.id.bt_video_upload_local)
    Button bt_local;
    @Bind(R.id.iv_video_upload_photo)
    ImageView iv_show_photo;
    @Bind(R.id.bt_video_upload_commit)
    Button bt_commit;
    @Bind(R.id.bt_video_upload_cancel)
    Button bt_cancel;
    @Bind(R.id.iv_video_upload_play)
    ImageView iv_play;
    @Bind(R.id.fl_video_upload_show)
    FrameLayout fl_show;
    @Bind(R.id.vv_video_upload_play)
    VideoView videoView;


    private static final int VIDEO_CAPTURE = 0;
    private static final int VIDEO_NATIVE = 1;

    private File file;
    private MediaController mediaController;
    private ProgressDialog progressDialog;
    private int progress = 0;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ConstantCode.GET_DOWNLOAD:
                    progress = msg.arg1;
                    progressDialog.setProgress(progress);
                    break;
                case ConstantCode.POST_SUCCESS:
                    Toast.makeText(VideoUploadActivity.this, "上传成功!", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    progressDialog.setTitle("上传成功");
                    progressDialog.setMessage("");
                    VideoUploadActivity.this.finish();
                    break;
                case ConstantCode.POST_FAILED:
                    Toast.makeText(VideoUploadActivity.this, "上传失败,请检查网络", Toast.LENGTH_SHORT).show();
                    break;
            }

        }
    };


    @Override
    public int getLayoutId() {
        return R.layout.activity_video_upload;
    }

    @Override
    public void initPage(Bundle savedInstanceState) {
        tv_title.setText("视频上传");

        mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);

    }

    @OnClick({R.id.iv_title_back, R.id.bt_video_upload_vcr, R.id.bt_video_upload_local, R.id.bt_video_upload_commit,
            R.id.bt_video_upload_cancel, R.id.iv_video_upload_play})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            //摄像头
            case R.id.bt_video_upload_vcr:
//                Intent intent_vcr = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
//                intent_vcr.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 600000);
//                intent_vcr.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
//                startActivityForResult(intent_vcr, VIDEO_CAPTURE);
                Intent intent_vcr=new Intent(VideoUploadActivity.this,TranscribeVideoActivity.class);
                startActivityForResult(intent_vcr,6);
                break;
            //本地视频
            case R.id.bt_video_upload_local:
                Intent intent_local = new Intent(Intent.ACTION_PICK);
                intent_local.setType("video/*");
                startActivityForResult(intent_local, VIDEO_NATIVE);
                break;
            //提交
            case R.id.bt_video_upload_commit:
                String loginName = SharedPreferencesHelper.getInstance().getUserName(this);
                String title = et_title.getText().toString();
                String content = et_content.getText().toString();
                MyServerModel.getInstance(this).uploadVideo(handler, file, title, content, loginName, this);
                initProgressDialog();
                break;
            case R.id.bt_video_upload_cancel:
                finish();
                break;
            case R.id.iv_video_upload_play:
                playVideo();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = null;
        if (resultCode == Activity.RESULT_OK && requestCode == VIDEO_CAPTURE) {
            Uri uri = data.getData();
            Cursor cursor = this.getContentResolver().query(uri, null, null, null, null);
            if (cursor != null && cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex(MediaStore.Video.VideoColumns._ID));
                String filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATA));
                file = new File(filePath);

                cursor.close();
                fl_show.setVisibility(View.VISIBLE);
                videoView.setVisibility(View.GONE);
                Glide.with(VideoUploadActivity.this).load(file).into(iv_show_photo);
            }
        } else if (resultCode == Activity.RESULT_OK && requestCode == VIDEO_NATIVE) {
            Uri uri = data.getData();
            file = getFileByUri(uri);
            Log.i("info", "file了路径" + file.getPath());
//            MediaMetadataRetriever mmr=new MediaMetadataRetriever();//实例化MediaMetadataRetriever对象
//            mmr.setDataSource(file.getAbsolutePath());
//            bitmap=mmr.getFrameAtTime();//获得视频第一帧的Bitmap对象
//            String duration = mmr.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_DURATION);//时长(毫秒)

        }
        else if(requestCode==6&&resultCode==6){
            file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES) + File.separator
                    + "反恐维稳"+  File.separator + "ctams.mp4");
                        MediaMetadataRetriever mmr=new MediaMetadataRetriever();//实例化MediaMetadataRetriever对象
           mmr.setDataSource(file.getAbsolutePath());
           bitmap=mmr.getFrameAtTime();//获得视频第一帧的Bitmap对象
            fl_show.setVisibility(View.VISIBLE);
            videoView.setVisibility(View.GONE);
            iv_show_photo.setImageBitmap(bitmap);

        }


    }


    public File getFileByUri(Uri uri) {
        String path = null;
        if ("file".equals(uri.getScheme())) {
            path = uri.getEncodedPath();
            if (path != null) {
                path = Uri.decode(path);
                ContentResolver cr = this.getContentResolver();
                StringBuffer buff = new StringBuffer();
                buff.append("(").append(MediaStore.Images.ImageColumns.DATA).append("=").append("'" + path + "'").append(")");
                Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        new String[]{MediaStore.Images.ImageColumns._ID, MediaStore.Images.ImageColumns.DATA}, buff.toString(), null,
                        null);
                int index = 0;
                int dataIdx = 0;
                for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                    index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                    index = cur.getInt(index);
                    dataIdx = cur.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    path = cur.getString(dataIdx);
                }
                cur.close();
                if (index == 0) {
                } else {
                    Uri u = Uri.parse("content://media/external/images/media/" + index);
                }
            }
            if (path != null) {
                return new File(path);
            }
        } else if ("content".equals(uri.getScheme())) {
            // 4.2.2以后
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = this.getContentResolver().query(uri, proj, null, null, null);
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                path = cursor.getString(columnIndex);
            }
            cursor.close();

            return new File(path);
        } else {
            Log.i("info", "Uri Scheme:" + uri.getScheme());
        }
        return null;
    }

    /**
     * 播放视频
     */
    private void playVideo() {
        videoView.setVisibility(View.VISIBLE);
        fl_show.setVisibility(View.GONE);
        mediaController.show(0);
        videoView.setVideoURI(Uri.parse(file.getAbsolutePath()));
        videoView.start();
        videoView.requestFocus();
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                fl_show.setVisibility(View.VISIBLE);
                videoView.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 初始化进度条
     */
    private void initProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setIcon(R.mipmap.video_update_gv_home_page_icon);
        progressDialog.setTitle("正在上传...");
        progressDialog.setMessage("请稍后");
        progressDialog.setMax(100);
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
                progress = 0;
                progressDialog.setProgress(progress);
                MyServerModel.getInstance(VideoUploadActivity.this).cancelTag(VideoUploadActivity.this);
                progressDialog.dismiss();
            }
        });
        progressDialog.show();
        progress = (progress > 0) ? progress : 0;
        progressDialog.setProgress(progress);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != videoView) {
            videoView = null;

        }
        if (progressDialog != null) {
            progressDialog = null;
        }
    }

}
