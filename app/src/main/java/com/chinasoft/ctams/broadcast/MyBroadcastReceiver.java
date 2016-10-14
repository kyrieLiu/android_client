package com.chinasoft.ctams.broadcast;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.chinasoft.ctams.R;
import com.chinasoft.ctams.activity.addresssBook.AllAddressBookActivity;
import com.chinasoft.ctams.activity.main.MainActivity;
import com.chinasoft.ctams.activity.scheduleManager.DateListMainActivity;
import com.chinasoft.ctams.activity.task.activity.TaskThingsListActivity;
import com.chinasoft.ctams.fragment.homePageFragment.schedule.activity.LeaderPushWorkScheduleListActivity;
import com.chinasoft.ctams.fragment.homePageFragment.schedule.activity.OfficePushWorkScheduleListActivity;
import com.chinasoft.ctams.fragment.subjectFragment.SubjectDetailActivity;
import com.chinasoft.ctams.util.SharedPreferencesHelper;
import com.daniulive.smartpublisher.CameraPublishActivity;

import org.androidpn.client.Constants;
import org.litepal.crud.DataSupport;

import sql.TopicMessage;

/**
 * Created by Kyrie on 2016/6/30.
 * Email:kyrie_liu@sina.com
 */
public class MyBroadcastReceiver extends BroadcastReceiver {

    private static int id=0;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onReceive(Context context, Intent intent) {
        String title=intent.getStringExtra(Constants.NOTIFICATION_TITLE);
        String content=intent.getStringExtra(Constants.NOTIFICATION_MESSAGE);
        String apiKey=intent.getStringExtra(Constants.NOTIFICATION_API_KEY);
        id++;
        Log.i("info","id数值"+id);
        PendingIntent pendingIntent=null;
        Intent turnIntent=new Intent();
        //值班
        if (apiKey.equals("0")){
            turnIntent.setClass(context, DateListMainActivity.class);
       }
        //销毁
        else if (apiKey.equals("3")){
            turnIntent.setClass(context, MainActivity.class);
            DataSupport.deleteAll(TopicMessage.class);
        }
        //视频通知
        else if (apiKey.equals("4")) {
            turnIntent.setClass(context, CameraPublishActivity.class);
        }
        //办内
        else if(apiKey.equals("bn")){
            turnIntent.setClass(context, OfficePushWorkScheduleListActivity.class);
        }
        //领导
        else if(apiKey.equals("ld")){
            turnIntent.setClass(context, LeaderPushWorkScheduleListActivity.class);
        }
        //专题图
        else if (apiKey.equals("ztt")){
            String contentsZtt=intent.getStringExtra(Constants.NOTIFICATION_URI);
            turnIntent.putExtra("ztt",contentsZtt);
            turnIntent.putExtra("isPush",true);
            turnIntent.setClass(context, SubjectDetailActivity.class);
        }
        //任务事件
        else if(apiKey.equals("rw")){
            turnIntent.setClass(context, TaskThingsListActivity.class);
            SharedPreferencesHelper.getInstance().saveTaskPushTime(context);

        }
            //通讯录
        else{
            turnIntent.setClass(context,AllAddressBookActivity.class);
            if (apiKey.equals("gr")){
                turnIntent.putExtra("flag","gr");
            }
            else if (apiKey.equals("jg")){
                turnIntent.putExtra("flag","jg");
            }
            else if (apiKey.equals("gg")){
                turnIntent.putExtra("flag","gg");
            }else{

            }
        }

        pendingIntent=PendingIntent.getActivity(context,0,turnIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle(title);
        builder.setContentText(content);
        builder.setSmallIcon(R.mipmap.app_logo);
        builder.setVibrate(new long[2]);
        builder.setTicker("新消息通知");
        builder.setContentIntent(pendingIntent);
        Notification notification=builder.getNotification();
        notification.flags=Notification.FLAG_AUTO_CANCEL;
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notification.defaults |=Notification.DEFAULT_SOUND;//设置为默认的声音

        notificationManager.notify(id, notification);

    }
}
