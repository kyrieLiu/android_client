package com.chinasoft.ctams.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.chinasoft.ctams.activity.scheduleManager.bean.DateListBean;
import com.chinasoft.ctams.activity.task.bean.SendPaperBean;
import com.chinasoft.ctams.activity.task.bean.TaskItemBean;
import com.chinasoft.ctams.activity.task.bean.TaskListBean;
import com.chinasoft.ctams.fragment.mineMainFragment.bean.OrgBean;
import com.chinasoft.ctams.fragment.mineMainFragment.bean.UserInfoBean;
import com.chinasoft.ctams.okhttp.CacheType;
import com.chinasoft.ctams.okhttp.Callback;
import com.chinasoft.ctams.okhttp.OKHttpUtils;
import com.chinasoft.ctams.util.ConstantCode;
import com.chinasoft.ctams.util.SharedPreferencesHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/6/25.
 */
public class ServerModel {
    private static ServerModel serverModel = null;
    private static String URL;
    private HashMap params;
    private String url;
    private static OKHttpUtils okHttpUtils;

    private ServerModel(Context context) {
        okHttpUtils = new OKHttpUtils.Builder(context).build();
        SharedPreferences preferences = context.getSharedPreferences("ip", Context.MODE_PRIVATE);
        String url_ip = preferences.getString("ipNumber", "");
        String port=SharedPreferencesHelper.getInstance().getPort(context);
        URL = "http://" + url_ip + ":"+port+"/ctams/";
    }


    public static ServerModel getInstance(Context context) {
        if (serverModel == null) {
            serverModel = new ServerModel(context);
        }
        return serverModel;
    }

    /**
     * 登录Model
     */
    public void login(final Handler handler, String username, String userpwd, String url,String display,String phoneModel, String version, final Context context) {

        RequestBody body = new FormBody.Builder().add("peopleLoginname", username).add("peoplePassword", userpwd)
                .add("devName",phoneModel).add("devNo",display).add("devVersion",version).add("devType","手机").build();
        Request request = new Request.Builder().tag(context).url(URL + url).post(body).build();
        okHttpUtils.request(request, CacheType.ONLY_NETWORK, new com.chinasoft.ctams.okhttp.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(ConstantCode.POST_FAILED);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String result = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        int code=jsonObject.getInt("resultCode");
                        if (code == 0){
                            JSONObject jsonObject1=jsonObject.getJSONObject("result").getJSONObject("people");
                            String username=jsonObject1.getString("peopleLoginname");
                            String userpassword=jsonObject1.getString("peoplePassword");
                            String peopleId=jsonObject1.getString("peopleId");
                            String peopleName=jsonObject1.getString("peopleName");
                            String organization=jsonObject1.getString("peopleOrganizationName");
                            String organizationId=jsonObject1.getString("peopleOrganization");
                            SharedPreferencesHelper.getInstance().saveUserInfo(context,username,userpassword,peopleId,peopleName,organization,organizationId);
                            handler.sendEmptyMessage(ConstantCode.POST_SUCCESS);
                        }else if (code==2){
                            handler.sendEmptyMessage(ConstantCode.DEV_EXCEPTION);
                        }else {
                            handler.sendEmptyMessage(ConstantCode.POST_FAILED);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        handler.sendEmptyMessage(ConstantCode.POST_FAILED);
                    }
                } else {

                    handler.sendEmptyMessage(ConstantCode.POST_FAILED);
                }
            }
        });
    }


    /**
     * 获取值班列表
     */
    public void getDateListData(final Handler handler, String url1, final List<DateListBean> list, String page, Context context) {
        params = new HashMap();
        params.put("pageNo", page);
        url = getUrl(params, URL + url1);
        Request request = new Request.Builder().tag(context).url(url).build();
        okHttpUtils.request(request, CacheType.NETWORK_ELSE_CACHED, new com.chinasoft.ctams.okhttp.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(ConstantCode.POST_FAILED);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String result = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        JSONArray jsonArray = new JSONArray(jsonObject.getString("rows"));
                        if (!(jsonArray.length() == 0)) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                DateListBean dateListBean = new DateListBean();
                                dateListBean.setDateListId(jsonObject1.getString("schedulId"));
                                dateListBean.setDateListName(jsonObject1.getString("schedulIndicate"));
                                dateListBean.setStartTime(jsonObject1.getString("schedulStarttime"));
                                dateListBean.setEndTime(jsonObject1.getString("schedulEndtime"));
                                dateListBean.setOrganization(jsonObject1.getString("schedulMechanism"));
                                list.add(dateListBean);
                            }
                        }
                        handler.sendEmptyMessage(ConstantCode.POST_SUCCESS);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.i("tag", "异常");
                    handler.sendEmptyMessage(ConstantCode.POST_FAILED);
                }
            }
        });

    }

    /**
     * 获取个人信息
     */
    public void getPersonInfo(Context context, final Handler handler, String url1) {
        params = new HashMap();
        params.put("peopleLoginname", SharedPreferencesHelper.getInstance().getUserName(context));
        url = getUrl(params, URL + url1);
        Log.i("tag","ur:"+url);
        Request request = new Request.Builder().tag(context).url(url).build();
        okHttpUtils.request(request, CacheType.NETWORK_ELSE_CACHED, new com.chinasoft.ctams.okhttp.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(ConstantCode.POST_FAILED);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Log.i("tag", "222");
                    String result = response.body().string();
                    Log.i("tag", "414" + result);
                    try {
                        UserInfoBean userInfoBean = new UserInfoBean();
                        JSONObject jsonObject = new JSONObject(result);
                        userInfoBean.setPeoplePhotourl(jsonObject.getString("peoplePhotourl"));
                        userInfoBean.setPeopleName(jsonObject.getString("peopleName"));
                        userInfoBean.setPeopleDate(jsonObject.getString("peopleDate"));
                        userInfoBean.setPeopleSex(jsonObject.getString("peopleSex"));
                        userInfoBean.setPeopleNational(jsonObject.getString("peopleNational"));
                        userInfoBean.setPeopleOrganization(jsonObject.getString("peopleOrganizationName"));
                        userInfoBean.setPeopleDivision(jsonObject.getString("peopleDivision"));
                        userInfoBean.setPeoplePhone(jsonObject.getString("peoplePhone"));
                        userInfoBean.setPeoplePhotourl(jsonObject.getString("peoplePhotourl"));
                        userInfoBean.setPeopleLoginname(jsonObject.getString("peopleLoginname"));
                        Message message = handler.obtainMessage();
                        message.obj = userInfoBean;
                        message.what = ConstantCode.POST_SUCCESS;
                        handler.sendMessage(message);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.i("tag", "请求异常");
                    handler.sendEmptyMessage(ConstantCode.POST_FAILED);
                }
            }
        });
    }

    /**
     * 更新个人信息
     */
    public void upDatePersonInfo(Context context, final Handler handler, String url, String field, String fieldValue) {

        RequestBody requestBody = new FormBody.Builder().add("peopleLoginname", SharedPreferencesHelper.getInstance().getUserName(context)).add("field", field).add("fieldValue", fieldValue).build();
        Request request = new Request.Builder().tag(context).url(URL + url).post(requestBody).build();
        Log.i("tag","------"+URL+url);
        okHttpUtils.request(request, CacheType.ONLY_NETWORK, new com.chinasoft.ctams.okhttp.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(ConstantCode.POST_FAILED);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String result = response.body().string();
                    Log.i("tag","---111---"+result);
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        int resultCode = jsonObject.getInt("resultCode");
                        if (resultCode == 0) {
                            handler.sendEmptyMessage(ConstantCode.POST_SUCCESS);
                        } else {
                            handler.sendEmptyMessage(ConstantCode.POST_FAILED);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        handler.sendEmptyMessage(ConstantCode.POST_FAILED);
                    }
                } else {
                    handler.sendEmptyMessage(ConstantCode.POST_FAILED);
                }
            }
        });
    }

    /**
     *机构树状图
     */
    public void getOrganizationsTreeData(final Handler handler, String url1, final boolean isArea) {
        Request request = new Request.Builder().url(URL + url1).build();
        okHttpUtils.request(request, CacheType.NETWORK_ELSE_CACHED, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("info","加载失败"+e);
                handler.sendEmptyMessage(ConstantCode.POST_FAILED);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()){
                    Log.i("tag","1");
                    String result=response.body().string();
                    Log.i("tag","22---"+result);
                    try {
                        JSONObject jsonObject=new JSONObject(result);
                        int code=jsonObject.getInt("resultCode");
                        if (code == 0){
                            List<OrgBean> list=new ArrayList<OrgBean>();
                            JSONObject jsonObject1=jsonObject.getJSONObject("result");
                            JSONArray jsonArray;
                            if (isArea){
                                jsonArray= jsonObject1.getJSONArray("area");
                            }else {
                                jsonArray = jsonObject1.getJSONArray("organization");
                            }
                            if (jsonArray.length()==0){
                                handler.sendEmptyMessage(ConstantCode.DATA_IS_NULL);
                            }else {
                                funTree(jsonArray, list);
                                Message message = new Message();
                                message.obj = list;
                                message.what = ConstantCode.POST_SUCCESS;
                                handler.sendMessage(message);
                            }
                        }else {
                            handler.sendEmptyMessage(ConstantCode.POST_FAILED);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    handler.sendEmptyMessage(ConstantCode.POST_FAILED);
                }
            }
        });
    }



    public void sendHeadIcon(final Handler handler, String url1, File file,Context context) {
        MultipartBody.Builder builder=new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("peopleLoginname", SharedPreferencesHelper.getInstance().getUserName(context))
                .addFormDataPart("field","PEOPLE_PHOTOURL");
        if (file != null){
            builder.addFormDataPart("peoplePhotourl",file.getName(), RequestBody.create(MediaType.parse("image/png"),file));
        }
        RequestBody requestBody=builder.build();
        Request request=new Request.Builder().tag(context).post(requestBody).url(URL+url1).build();
        okHttpUtils.request(request, CacheType.ONLY_NETWORK, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(ConstantCode.POST_FAILED);
                Log.i("tag","2");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()){
                    Log.i("tag","1");
                    String result=response.body().string();
                    Log.i("info","result  ===="+result);
                    try {
                        JSONObject jsonObject=new JSONObject(result);
                        int code=jsonObject.getInt("resultCode");
                        if (code == 0){
                            handler.sendEmptyMessage(ConstantCode.POST_SUCCESS);
                        }
                        Log.i("tag","上传成功");
                    } catch (JSONException e) {
                        e.printStackTrace();
                       // handler.sendEmptyMessage(ConstantCode.POST_FAILED);
                    }
                }else {
                    handler.sendEmptyMessage(ConstantCode.POST_FAILED);
                    Log.i("tag","3");
                }
            }
        });

    }
    /**
     *  信息报送查询
     */

    public void getSendPaperByType(final Handler handler, String url1, String type,final String orgnizationId,int pageNo,Context context){
        params=new HashMap();
        params.put("receiveinfoStatic",type);
        params.put("pageNo",String.valueOf(pageNo));
        params.put("organizationId",orgnizationId);
        url =getUrl(params,URL+url1);
        Request request=new Request.Builder().tag(context).url(url).build();
        okHttpUtils.request(request, CacheType.NETWORK_ELSE_CACHED, new com.chinasoft.ctams.okhttp.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(ConstantCode.POST_FAILED);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        List<SendPaperBean> list=new ArrayList<SendPaperBean>();
                        String result=response.body().string();
                        Log.i("tag",result);
                        JSONObject jsonObject = new JSONObject(result);
                        JSONArray jsonArray = new JSONArray(jsonObject.getString("rows"));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            SendPaperBean sendPaperBean = new SendPaperBean();
                            sendPaperBean.setSendPaperId(jsonObject1.getString("receiveinfoId"));
                            sendPaperBean.setSendPaperTitle(jsonObject1.getString("receiveinfoTitle"));
                            sendPaperBean.setSendPaperStatus(jsonObject1.getString("receiveinfoStatic"));
                            sendPaperBean.setSendPaperContent(jsonObject1.getString("receiveinfoContent"));
                            sendPaperBean.setSendPaperDate(jsonObject1.getString("receiveinfoDate"));
                            sendPaperBean.setSendPaperMessageType(jsonObject1.getString("receiveinfoMessagertype"));
                            sendPaperBean.setSendPaperOpinion(jsonObject1.getString("receiveinfoOpinion"));
                            sendPaperBean.setSendPaperOrganization(jsonObject1.getString("receiveinfoMechanism"));
                            sendPaperBean.setSendPaperPhoneVoice(jsonObject1.getString("receiveinfoSound"));
                            sendPaperBean.setSendPaperPicture(jsonObject1.getString("receiveinfoPicture"));
                            sendPaperBean.setSendPaperPerson(jsonObject1.getString("receiveinfoPerson"));
                            sendPaperBean.setSendPaperReceivedPerson(jsonObject1.getString("receiveinfoSendee"));
                            sendPaperBean.setSendPaperReceivedTime(jsonObject1.getString("receiveinfoTime"));
                            sendPaperBean.setSendPaperSendUnit(jsonObject1.getString("receiveinfoSubmmitpositiopn"));
                            sendPaperBean.setSendPaperReceivedUnit(jsonObject1.getString("receiveinfoRecposition"));
                            sendPaperBean.setSendPaperSubmitMode(jsonObject1.getString("receiveinfoSubmmitmode"));
                            sendPaperBean.setSendPaperSubmitType(jsonObject1.getString("receiveinfoSubmittype"));
                            sendPaperBean.setSendPaperReceiveinfoAddress(jsonObject1.getString("receiveinfoAddress"));
                            list.add(sendPaperBean);
                        }
                        if (list.size()==0){
                            handler.sendEmptyMessage(ConstantCode.DATA_IS_NULL);
                        }else {
                            Message message = handler.obtainMessage();
                            message.what = ConstantCode.POST_SUCCESS;
                            message.obj = list;
                            handler.sendMessage(message);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        handler.sendEmptyMessage(ConstantCode.POST_FAILED);
                    }
                }else {
                    handler.sendEmptyMessage(ConstantCode.POST_FAILED);
                }
            }
        });
    }


    /**
     *删除信息报送
     * @param handler
     * @param url
     * @param ids id集合
     */
    public void DeleteSendPaperById(final Handler handler,String url,String[] ids,Context context){
        StringBuilder idss=new StringBuilder();
        for (int i=0;i<ids.length;i++){
            idss.append(ids[i]);
        }
        RequestBody requestBody=new FormBody.Builder().add("ids",idss.toString()).build();
        Request request=new Request.Builder().tag(context).url(URL+url).post(requestBody).build();
        okHttpUtils.request(request, CacheType.ONLY_NETWORK, new com.chinasoft.ctams.okhttp.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(ConstantCode.DATA_DELETED_FAILED);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()){
                    Log.i("tag","111--");
                    String result=response.body().string();
                    Log.i("tag",result);
                    try {
                        JSONObject jsonObject=new JSONObject(result);
                        int resultCode=jsonObject.getInt("resultCode");
                        if (resultCode==0){
                            handler.sendEmptyMessage(ConstantCode.DATA_DELETED_SUCCESS);
                        }else {
                            handler.sendEmptyMessage(ConstantCode.DATA_DELETED_FAILED);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    handler.sendEmptyMessage(ConstantCode.DATA_DELETED_FAILED);
                    Log.i("tag","78787");
                }
            }
        });
    }

    /**
     * 添加信息报送
     */
    public void AddSendPaper(final Handler handler,String url,String title,String messageType,String sendUnit,String sendPerson,String receivedUnit,String content,String sendAddress,Context context ){
        SimpleDateFormat format=new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
        String sendTime=format.format(new Date());
        RequestBody requestBody=new FormBody.Builder().add("receiveinfoTitle",title).add("receiveinfoMessagertype",messageType).add("receiveinfoDate",sendTime).add("receiveinfoMechanism",SharedPreferencesHelper.getInstance().getOrganizationId(context))
                .add("receiveinfoPerson",sendPerson).add("receiveinfoSubmmitpositiopn",sendUnit).add("receiveinfoRecposition",receivedUnit).add("receiveinfoContent",content).add("receiveinfoAddress",sendAddress).build();
        Request request=new Request.Builder().tag(context).url(URL+url).post(requestBody).build();
        okHttpUtils.request(request, CacheType.ONLY_NETWORK, new com.chinasoft.ctams.okhttp.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(ConstantCode.POST_FAILED);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()){
                    Log.i("tag","jinlaile");
                    String result=response.body().string();
                    Log.i("tag",result);
                    try {
                        JSONObject jsonObject=new JSONObject(result);
                        int resultCode=jsonObject.getInt("resultCode");
                        if (resultCode==0){
                            handler.sendEmptyMessage(ConstantCode.POST_SUCCESS);
                        }else {
                            handler.sendEmptyMessage(ConstantCode.POST_FAILED);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    handler.sendEmptyMessage(ConstantCode.POST_FAILED);
                }
            }
        });
    }

    /**
     * 修改信息报送
     */
    public void UpdateMessagePaper(final Handler handler,String url,String title,String messageType,String sendTime,String sendUnit,String sendPerson,String receivedUnit,String content,String id,Context context){
        RequestBody requestBody=new FormBody.Builder().add("receiveinfoTitle",title).add("receiveinfoMessagertype",messageType).add("receiveinfoDate",sendTime)
                .add("receiveinfoPerson",sendPerson).add("receiveinfoSubmmitpositiopn",sendUnit).add("receiveinfoRecposition",receivedUnit).add("receiveinfoContent",content).add("receiveinfoId",id).build();
        Request request=new Request.Builder().tag(context).url(URL+url).post(requestBody).build();
        okHttpUtils.request(request, CacheType.ONLY_NETWORK, new com.chinasoft.ctams.okhttp.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(ConstantCode.DATA_ADD_UPDATE_FAILED);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()){
                    String result=response.body().string();
                    Log.i("tag",result);
                    try {
                        JSONObject jsonObject=new JSONObject(result);
                        int resultCode=jsonObject.getInt("resultCode");
                        if (resultCode==0){
                            handler.sendEmptyMessage(ConstantCode.DATA_ADD_UPDATE_SUCCESS);
                        }else {
                            handler.sendEmptyMessage(ConstantCode.DATA_ADD_UPDATE_FAILED);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    Log.i("tag","11");
                    handler.sendEmptyMessage(ConstantCode.DATA_ADD_UPDATE_FAILED);
                }
            }
        });
    }





    /**
     * 获取任务事件列表
     * @param handler
     * @param url1
     * @param pageNo
     * @param context
     */
    public void getTaskThingList(final Handler handler, String url1, int pageNo,String orginaztionId, final Context context){
        params=new HashMap();
        params.put("pageNo",String.valueOf(pageNo));
        params.put("organizationId",orginaztionId);
        url = getUrl(params,URL+url1);
        Log.i("info","获取的url  "+url);
        final Request request=new Request.Builder().tag(context).url(url).build();
        okHttpUtils.request(request, CacheType.NETWORK_ELSE_CACHED, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(ConstantCode.POST_FAILED);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()){
                    try {
                        String result=response.body().string();
                        Log.i("info","获取的任务事件"+result);
                        JSONObject jsonObject= new JSONObject(result);
                            JSONArray jsonArray=jsonObject.getJSONArray("rows");
                            if (jsonArray.length()==0){
                                handler.sendEmptyMessage(ConstantCode.DATA_IS_NULL);
                            }else {
                                List<TaskListBean> list=new ArrayList<TaskListBean>();
                                String time=SharedPreferencesHelper.getInstance().getTaskPushTime(context);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                                    TaskListBean taskListBean=new TaskListBean();
                                    taskListBean.setThingId(jsonObject2.getString("thingId"));
                                    taskListBean.setThingPlace(jsonObject2.getString("thingPlace"));
                                    taskListBean.setThingName(jsonObject2.getString("thingName"));
                                    taskListBean.setThingType(jsonObject2.getString("thingType"));
                                    if (!time.equals("")){
                                        taskListBean.setThingDate(time);
                                    }else{
                                        taskListBean.setThingDate(jsonObject2.getString("thingDate"));
                                    }
                                    taskListBean.setThingIntroduction(jsonObject2.getString("thingIntroduction"));
                                    taskListBean.setThingMechanism(jsonObject2.getString("thingMechanism"));
                                    list.add(taskListBean);
                                }
                                Message message=new Message();
                                message.obj=list;
                                message.what=ConstantCode.POST_SUCCESS;
                                handler.sendMessage(message);
                            }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    handler.sendEmptyMessage(ConstantCode.POST_FAILED);
                }
            }
        });
    }

    /**
     * 获取任务事件子事件列表
     * @param handler
     * @param url1
     * @param pId
     * @param context
     */
    public void getTaskDetailItemList(final Handler handler,String url1,String pId,Context context){
        Log.i("tag","123456");
        params=new HashMap();
        params.put("id",pId);
        url=getUrl(params,URL+url1);
        Log.i("tag","+++++"+pId);
        Log.i("tag","+++++"+url);
        Request request=new Request.Builder().tag(context).url(url).build();
        okHttpUtils.request(request, CacheType.NETWORK_ELSE_CACHED, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("tag", "+++++---3");
                handler.sendEmptyMessage(ConstantCode.POST_FAILED);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        String result = response.body().string();
                        Log.i("tag", "+++++---" + result);
                        JSONObject jsonObject = new JSONObject(result);
                        int code = jsonObject.getInt("resultCode");
                        if (code == 0) {
                            JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                            JSONArray jsonArray = jsonObject1.getJSONArray("inforList");
                            List<TaskItemBean> list = new ArrayList<TaskItemBean>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                                TaskItemBean taskItemBean = new TaskItemBean();
                                taskItemBean.setItemTaskTitle(jsonObject2.getString("receiveinfoTitle"));
                                taskItemBean.setItemTaskContent(jsonObject2.getString("receiveinfoContent"));
                                taskItemBean.setItemTaskDate(jsonObject2.getString("receiveinfoDate"));
                                taskItemBean.setItemTaskId(jsonObject2.getString("receiveinfoId"));
                                taskItemBean.setItemTaskMan(jsonObject2.getString("receiveinfoPerson"));
                                taskItemBean.setItemTaskType(jsonObject2.getString("receiveinfoMessagertype"));
                                list.add(taskItemBean);
                            }
                            Message message = new Message();
                            message.what = ConstantCode.POST_SUCCESS;
                            message.obj = list;
                            handler.sendMessage(message);

                        } else {
                            Log.i("tag", "+++++---1");
                            handler.sendEmptyMessage(ConstantCode.POST_FAILED);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
              else {
                    Log.i("tag","+++++---2");
                    handler.sendEmptyMessage(ConstantCode.POST_FAILED);
                }
            }
        });
    }

    public void getMonitorAreaListTree(final Handler handler, String url1, final Context context){
        RequestBody requestBody = new FormBody.Builder().build();
        Request request = new Request.Builder().tag(context).url(URL + url1).post(requestBody).build();
        okHttpUtils.request(request, CacheType.NETWORK_ELSE_CACHED, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(ConstantCode.POST_FAILED);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()){
                    String result=response.body().string();
                    Log.i(" info","监控列表"+result);
                   try {
                        JSONObject jsonObject=new JSONObject(result);
                        int code=jsonObject.getInt("resultCode");
                        if (code == 0){
                            List<OrgBean> list=new ArrayList<OrgBean>();
                            JSONObject jsonObject1=jsonObject.getJSONObject("result");
                            JSONArray jsonArray= jsonObject1.getJSONArray("area");
                            if (jsonArray.length()==0){
                                handler.sendEmptyMessage(ConstantCode.DATA_IS_NULL);
                            }else {
                                funTree(jsonArray, list);
                                Message message = new Message();
                                message.obj = list;
                                message.what = ConstantCode.POST_SUCCESS;
                                handler.sendMessage(message);
                            }
                        }else {
                            handler.sendEmptyMessage(ConstantCode.POST_FAILED);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else {
                    handler.sendEmptyMessage(ConstantCode.POST_FAILED);
                }
            }
        });

    }

    public void getMonitorInfo(final Handler handler,String url1,String id,Context context){
        params=new HashMap();
        params.put("id",id);
        url=getUrl(params,URL+url1);
        Request request=new Request.Builder().tag(context).url(url).build();
        okHttpUtils.request(request, CacheType.NETWORK_ELSE_CACHED, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(ConstantCode.POST_FAILED);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()){
                    String result=response.body().string();
                    Log.i("tag","----"+result);
                    try {
                        JSONObject jsonObject=new JSONObject(result);
                        int code=jsonObject.getInt("resultCode");
                        if (code == 0){

                        }else {
                            handler.sendEmptyMessage(ConstantCode.POST_FAILED);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else {
                    handler.sendEmptyMessage(ConstantCode.POST_FAILED);
                }
            }
        });

    }



    /**
     * 获取报送地点列表
     * @param handler
     * @param url1
     * @param context
     */
    public void getSendAddressListData(final Handler handler,String url1,Context context){
        params=new HashMap();
        params.put("pageNo","1");
        url=getUrl(params,URL+url1);
        Log.i("tag","url:"+url);
        Request request=new Request.Builder().tag(context).url(url).build();
        okHttpUtils.request(request, CacheType.NETWORK_ELSE_CACHED, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(ConstantCode.POST_FAILED);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()){
                    try {
                        String result=response.body().string();
                        Log.i("tag","result:"+result);
                        JSONObject jsonObject=new JSONObject(result);
                        int code=jsonObject.getInt("resultCode");
                        Log.i("tag","result111:");
                        if (code==0){
                            Log.i("tag","result222:");
                            JSONObject jsonObject1=jsonObject.getJSONObject("result");
                            JSONArray jsonArray=jsonObject1.getJSONArray("adressList");
                            List<String> list=new ArrayList<String>();
                            for (int i=0;i<jsonArray.length();i++){
                                Log.i("tag","result:"+i);
                                JSONObject jsonObject2=jsonArray.getJSONObject(i);
                                String name=jsonObject2.getString("adressName");
                                Log.i("tag","result1:"+name);
                                list.add(name);
                            }
                            Message message=new Message();
                            message.what=ConstantCode.POST_SUCCESS;
                            message.obj=list;
                            handler.sendMessage(message);
                        }else {
                            handler.sendEmptyMessage(ConstantCode.POST_FAILED);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    handler.sendEmptyMessage(ConstantCode.POST_FAILED);
                }
            }
        });
    }

    public void closeSpy(Context context){
        params=new HashMap();
        params.put("userName",SharedPreferencesHelper.getInstance().getUserName(context));
        url =getUrl(params,URL+"apnUser/accMqOut");
        Request request=new Request.Builder().tag(context).url(url).build();
        okHttpUtils.request(request, CacheType.ONLY_NETWORK, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
            }
        });
    }

    /**
     * 根据URL和相应参数进行get请求
     * @param params
     * @param baseUrl
     * @return
     */
    private String getUrl(HashMap<String, String> params, String baseUrl) {
        StringBuilder builder = new StringBuilder(baseUrl);
        if (params.isEmpty()) {
            return baseUrl;
        } else {
            builder.append("?");
            Set<Map.Entry<String, String>> set = params.entrySet();
            for (Map.Entry<String, String> entry : set) {
                builder.append(entry.getKey());
                builder.append("=");
                builder.append(entry.getValue());
                builder.append("&");
            }
            builder = builder.replace(builder.lastIndexOf("&"), builder.length() + 1, "");
        }
        return builder.toString();
    }

    private void  funTree(JSONArray jsonArray,List<OrgBean> list){
        try {
//            if (jsonArray.length()==0){
//                return;
//            }else {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String id = jsonObject.getString("id");
                    String name = jsonObject.getString("name");
                    String pid = jsonObject.getString("parentId");
                    OrgBean orgBean = new OrgBean(id, name, pid);
                    list.add(orgBean);
                    JSONArray jsonArray1 = jsonObject.getJSONArray("children");
                    if (jsonArray1.length()!=0) {
                        funTree(jsonArray1, list);
                    }
//                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 取消请求
     * @param tag
     */
    public void cancelTag(Object tag)
    {
        for (Call call : okHttpUtils.getClient().dispatcher().queuedCalls())
        {
            if (tag.equals(call.request().tag()))
            {
                call.cancel();
            }
        }
        for (Call call : okHttpUtils.getClient().dispatcher().runningCalls())
        {
            if (tag.equals(call.request().tag()))
            {
                call.cancel();
            }
        }
    }
}
