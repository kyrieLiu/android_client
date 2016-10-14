package com.chinasoft.ctams.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.chinasoft.ctams.activity.addresssBook.bean.GroupManageBean;
import com.chinasoft.ctams.activity.patrol.bean.PatrolPlanBean;
import com.chinasoft.ctams.activity.search.bean.ComprehensiveSearchBean;
import com.chinasoft.ctams.bean.bean_json.FriendListBean;
import com.chinasoft.ctams.bean.bean_json.OftenContactAddressBean;
import com.chinasoft.ctams.bean.bean_json.OrganizeAddressBookBean;
import com.chinasoft.ctams.bean.bean_json.PersonalAddressBookBean;
import com.chinasoft.ctams.bean.bean_json.ScheduleDetailsBean;
import com.chinasoft.ctams.bean.bean_json.ScheduleInformationBean;
import com.chinasoft.ctams.bean.bean_json.StatisticReceiveInfoBean;
import com.chinasoft.ctams.fragment.homePageFragment.schedule.bean.LeaderDailyBean;
import com.chinasoft.ctams.fragment.homePageFragment.schedule.bean.OfficeDailyBean;
import com.chinasoft.ctams.fragment.mediafragment.monitorList.bean.MonitorBean;
import com.chinasoft.ctams.okhttp.CacheType;
import com.chinasoft.ctams.okhttp.Callback;
import com.chinasoft.ctams.okhttp.JsonCallback;
import com.chinasoft.ctams.okhttp.OKHttpUtils;
import com.chinasoft.ctams.okhttp.ProgressRequestBody;
import com.chinasoft.ctams.okhttp.UIProgressRequestListener;
import com.chinasoft.ctams.util.ConstantCode;
import com.chinasoft.ctams.util.SharedPreferencesHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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
 * Created by Kyrie on 2016/6/28.
 * Email:kyrie_liu@sina.com
 * 处理网络请求业务逻辑
 */
public class MyServerModel {

    private static MyServerModel serverModel = null;
    private static OKHttpUtils okHttpUtils;
    private HashMap<String, String> params;
    private String url;

    private MyServerModel(Context context) {
        okHttpUtils = new OKHttpUtils.Builder(context).build();
        SharedPreferences preferences = context.getSharedPreferences("ip", Context.MODE_PRIVATE);
        String url_ip = preferences.getString("ipNumber", "") + "";
        String port=SharedPreferencesHelper.getInstance().getPort(context);
        URL = "http://" + url_ip + ":"+port+"/ctams/";
    }

    private static String URL;

    public static MyServerModel getInstance(Context context) {
        if (serverModel == null) {
            serverModel = new MyServerModel(context);
        }
        return serverModel;
    }

    /**
     * 加载信息接收统计表
     */
    public void loadCharStatistic(final Handler handler, String url,Context context) {
        Request request = new Request.Builder().tag(context).url(URL + url).build();
        okHttpUtils.request(request, CacheType.NETWORK_ELSE_CACHED, new com.chinasoft.ctams.okhttp.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(ConstantCode.POST_FAILED);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null && response.isSuccessful()) {
                    String result = response.body().string();
                    Log.i("info", "获取的result" + result);
                    List<StatisticReceiveInfoBean> list = new ArrayList<StatisticReceiveInfoBean>();
                    try {
                        JSONObject object = new JSONObject(result);
                        JSONArray array = new JSONArray(object.getString("rows"));
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonObject = array.getJSONObject(i);
                            StatisticReceiveInfoBean bean = new StatisticReceiveInfoBean();
                            bean.setType(jsonObject.getString("type"));
                            bean.setCount(jsonObject.getInt("count"));
                            list.add(bean);
                        }
                        Message message = handler.obtainMessage();
                        message.what = ConstantCode.POST_SUCCESS;
                        message.obj = list;
                        handler.sendMessage(message);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    handler.sendEmptyMessage(ConstantCode.POST_FAILED);
                }
            }
        });
    }

    /**
     * 获取排班详情
     */
    public void loadScheduleDetails(final Handler handler, String id,Context context) {
        params = new HashMap<>();
        params.put("id", id);
        url = getUrl(params, URL + "schedul/showSchedul");
        Request request = new Request.Builder().tag(context).url(url).build();

        okHttpUtils.request(request, CacheType.NETWORK_ELSE_CACHED, new com.chinasoft.ctams.okhttp.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("info", "连接失败");
                handler.sendEmptyMessage(ConstantCode.POST_FAILED);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null && response.isSuccessful()) {
                    String result = response.body().string();
                    Log.i("info", "获取的result" + result);
                    List<ScheduleDetailsBean> list = new ArrayList<ScheduleDetailsBean>();
                    ScheduleInformationBean informationBean = new ScheduleInformationBean();
                    try {
                        JSONObject object = new JSONObject(result);
                        JSONArray array = new JSONArray(object.getString("rows"));

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonObject = array.getJSONObject(i);
                            if (i < array.length() - 1) {
                                ScheduleDetailsBean bean = new ScheduleDetailsBean();
                                bean.setDutyrelationDate(jsonObject.getString("dutyrelationDate"));
                                bean.setDutyrelationPerids(jsonObject.getString("dutyrelationPerids"));
                                bean.setDutystartTime(jsonObject.getString("dutystartTime"));
                                bean.setDutyendTime(jsonObject.getString("dutyendTime"));
                                list.add(bean);
                            } else if (i == array.length() - 1) {
                                informationBean.setSchedulIndicate(jsonObject.getString("schedulIndicate"));
                                informationBean.setSchedulEndtime(jsonObject.getString("schedulEndtime"));
                                informationBean.setSchedulStarttime(jsonObject.getString("schedulStarttime"));
                                informationBean.setSchedulIndicate(jsonObject.getString("schedulIndicate"));
                                informationBean.setSchedulLeader(jsonObject.getString("schedulLeader"));
                                informationBean.setSchedulPhone(jsonObject.getString("schedulPhone"));
                                informationBean.setSchedulType(jsonObject.getString("schedulType"));
                            }
                        }
                        informationBean.setList(list);
                        Message message = handler.obtainMessage();
                        message.what = ConstantCode.POST_SUCCESS;
                        message.obj = informationBean;
                        handler.sendMessage(message);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    Log.i("info", "加载 异常");
                    handler.sendEmptyMessage(ConstantCode.POST_FAILED);
                }
            }

        });

    }

    /**
     * 获取摄像头监控列表
     */
    public void loadMonitorList(final Handler handler,String id, int pageNo,Context context) {

        params = new HashMap<>();
        params.put("id",id);
        params.put("pageNo", pageNo+"");
        url = getUrl(params, URL + "videoAccessMQ/listVideoByPlaceId");
        Request request = new Request.Builder().tag(context).url(url).build();

        okHttpUtils.request(request, CacheType.NETWORK_ELSE_CACHED, new JsonCallback<MonitorBean>() {

            @Override
            public void onFailure(Call call, Exception e) {
                Log.e("error", "加载失败" + e.toString());
            }

            @Override
            public void onResponse(Call call, MonitorBean object) throws IOException {
                List<MonitorBean.MonitorRowsBean> list = object.getRows();
                Message message = handler.obtainMessage();
                message.what = ConstantCode.POST_SUCCESS;
                message.obj = list;
                handler.sendMessage(message);
            }
        });
    }

    /**
     * 加载好友列表
     */
    public void loadUserList(final Handler handler,Context context) {

        params = new HashMap<>();
        params.put("keyWord", "getFriendList");
        params.put("userName", "mi");
        url = getUrl(params, URL + "people/listApnUser");
        final Request request = new Request.Builder().tag(context).url(url).build();
        okHttpUtils.request(request, CacheType.NETWORK_ELSE_CACHED, new com.chinasoft.ctams.okhttp.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("info", "执行失败");
                handler.sendEmptyMessage(ConstantCode.POST_FAILED);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null && response.isSuccessful()) {
                    String result = response.body().string();
                    Log.i("info", "返回的内容" + result);
                    List<FriendListBean> friendsList = new ArrayList<FriendListBean>();
                    try {
                        JSONObject object = new JSONObject(result);
                        JSONArray jsonArray = new JSONArray(object.getString("rows"));
                        Log.i("info", "array的长度" + jsonArray.length());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                            FriendListBean bean = new FriendListBean();
                            bean.setCreatedDate(jsonObject.getString("createdDate"));
                            bean.setPassword(jsonObject.getString("password"));
                            bean.setAccountName(jsonObject.getString("username"));
                            bean.setId(jsonObject.getInt("id"));
                            bean.setPeopleName(jsonObject.getString("name"));
                            bean.setHeadPictureUrl(jsonObject.getString("headPicture"));
                            friendsList.add(bean);
                        }
                        ;
                    } catch (JSONException e) {
                        Log.i("info", "返回异常");
                        e.printStackTrace();
                    }
                    Message message = handler.obtainMessage();
                    message.obj = friendsList;
                    message.what = ConstantCode.POST_SUCCESS;
                    handler.sendMessage(message);
                } else {
                    handler.sendEmptyMessage(ConstantCode.POST_FAILED);

                }
            }
        });
    }


    /**
     * 常用联系人
     */
    public void loadOftenContact(final Handler handler, String peopleId,Context context) {
        params = new HashMap<>();
        params.put("peopleId", peopleId);
        url = getUrl(params, URL + "percomm/listOftenPercomm");

        Request request = new Request.Builder().tag(context).url(url).build();
        okHttpUtils.request(request, CacheType.NETWORK_ELSE_CACHED, new com.chinasoft.ctams.okhttp.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("info","常用联系人加载失败");
                handler.sendEmptyMessage(ConstantCode.POST_FAILED);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("info","常用联系人"+response);
                if (response != null ) {
                    String result = response.body().string();
                    Log.i("info", "常用联系人通讯录" + result);

                    try {
                        JSONArray array = new JSONArray(result);
                        List<OftenContactAddressBean> list = new ArrayList<OftenContactAddressBean>();
                        for (int i = 0; i < array.length(); i++) {
                            OftenContactAddressBean bean = new OftenContactAddressBean();
                            JSONObject jsonObject = array.getJSONObject(i);
                            bean.setPercommid(jsonObject.getString("percommid"));
                            bean.setPercommtel(jsonObject.getString("percommtel"));
                            String percommname = jsonObject.getString("percommname");
                            if (percommname.length() > 0) {
                                bean.setPercommname(percommname);
                                list.add(bean);
                            }

                        }
                        Message message = handler.obtainMessage();
                        message.what = ConstantCode.POST_SUCCESS;
                        message.obj = list;
                        handler.sendMessage(message);
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
     * 加载个人通讯录
     */
    public void loadPersonalAddressBook(final Handler handler, String peopleId,Context context) {
        params = new HashMap<>();
        params.put("peopleId", peopleId);
        url = getUrl(params, URL + "percomm/listPercommMo");
        Log.i("tag","---"+url);
        final Request request = new Request.Builder().tag(context).url(url).build();
        okHttpUtils.request(request, CacheType.NETWORK_ELSE_CACHED, new com.chinasoft.ctams.okhttp.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(ConstantCode.POST_FAILED);
                Log.i("info", "个人通讯录加载失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String result = response.body().string();
                    Log.i("info", "个人通讯录" + result);
                    try {
                        JSONArray array = new JSONArray(result);
                        Log.i("info", "个人通讯录长度" + array.length());
                        List<PersonalAddressBookBean> list = new ArrayList<PersonalAddressBookBean>();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonObject = array.getJSONObject(i);
                            PersonalAddressBookBean bean = new PersonalAddressBookBean();
                            bean.setPercommtel(jsonObject.getString("percommtel"));
                            bean.setPercommid(jsonObject.getString("percommid"));
                            String percommname = jsonObject.getString("percommname");
                            if (percommname.length() > 0) {
                                bean.setPercommname(percommname);
                                list.add(bean);
                            }
                        }
                        Log.i("info", "个人通讯录长度" + list.size());
                        Message message = handler.obtainMessage();
                        message.what = ConstantCode.POST_SUCCESS;
                        message.obj = list;
                        handler.sendMessage(message);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    handler.sendEmptyMessage(ConstantCode.POST_FAILED);
                }
            }

        });
    }

    /**
     * 机构通讯录
     */
    public void loadGroupAddressBook(final Handler handler,Context context) {
        Request request = new Request.Builder().tag(context).url(URL+"mechcomm/listMechcommMo").build();

        okHttpUtils.request(request, CacheType.NETWORK_ELSE_CACHED, new com.chinasoft.ctams.okhttp.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(ConstantCode.POST_FAILED);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null && response.isSuccessful()) {
                    String result = response.body().string();
                    Log.i("info", "机构通讯录" + result);
                    try {
                        JSONArray array = new JSONArray(result);
                        List<OrganizeAddressBookBean> list = new ArrayList<OrganizeAddressBookBean>();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonObject = array.getJSONObject(i);
                            OrganizeAddressBookBean bean = new OrganizeAddressBookBean();

                            bean.setMechcommPhone(jsonObject.getString("mechcommPhone"));
                            bean.setMechcommSuperior(jsonObject.getString("mechcommSuperior"));
                            String mechcommName = jsonObject.getString("mechcommName");
                            String introduce=jsonObject.getString("mechcommIntroduce");
                            if (mechcommName.length() > 0) {
                                bean.setMechcommName(mechcommName);
                                list.add(bean);
                            }
                        }
                        Message message = handler.obtainMessage();
                        message.what = ConstantCode.POST_SUCCESS;
                        message.obj = list;
                        handler.sendMessage(message);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    handler.sendEmptyMessage(ConstantCode.POST_FAILED);
                }
            }
        });
    }

    /**
     * 加载巡逻计划列表
     */
    public void loadMainPatrol(final Handler handler, Context context) {
        String username = SharedPreferencesHelper.getInstance().getUserName(context);
        params = new HashMap<>();
        params.put("name", username);
        url = getUrl(params, URL + "patrolTeam/showPatrolTeam");
        Request request = new Request.Builder().tag(context).url(url).build();
        okHttpUtils.request(request, CacheType.NETWORK_ELSE_CACHED, new JsonCallback<PatrolPlanBean>() {
            @Override
            public void onFailure(Call call, Exception e) {
                Log.i("info", "加载失败");
                handler.sendEmptyMessage(ConstantCode.POST_FAILED);
            }

            @Override
            public void onResponse(Call call, PatrolPlanBean object) throws IOException {
                Message message = handler.obtainMessage();
                message.what = ConstantCode.POST_SUCCESS;
                message.obj = object;
                handler.sendMessage(message);
            }
        });

    }

    /**
     * 上传巡逻反馈
     */
    public void commitFeedBack(final Handler handler, File file, String feedbackTeam, String feedbackRoute,
                               String feedbackName, String feedbackType,
                               final String feedbackContent, String coordinate, String planName, int num,Context context) {

        String organizeNumber=SharedPreferencesHelper.getInstance().getOrganizationId(context);
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("feedbackMechanism",organizeNumber)
                .addFormDataPart("feedbackRoute", feedbackRoute+"")
                .addFormDataPart("feedbackTeam", feedbackTeam+"")
                .addFormDataPart("feedbackName", feedbackName+"")
                .addFormDataPart("feedbackType", feedbackType+"")
                .addFormDataPart("feedbackContent", feedbackContent+"")
                .addFormDataPart("feedbackCoordinate", coordinate+"")
                .addFormDataPart("feedbackPlanid", planName+"")
                .addFormDataPart("feedbackNumber", num + "");
        if (file != null) {
            builder.addFormDataPart("feedPic", file.getName(), RequestBody.create(MediaType.parse("image/png"), file));
        }
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder().tag(context).url(URL + "feedback/saveFeedBack").post(requestBody).build();
        okHttpUtils.request(request, CacheType.ONLY_NETWORK, new com.chinasoft.ctams.okhttp.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("info", "上传失败");
                handler.sendEmptyMessage(ConstantCode.POST_FAILED);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null && response.isSuccessful()) {
                    handler.sendEmptyMessage(ConstantCode.POST_SUCCESS);
                } else {
                    handler.sendEmptyMessage(ConstantCode.POST_FAILED);
                }
            }
        });
    }

    /**
     * 巡逻过程上报
     */
    public void commitPatrolProcess(final Handler handler, String patrolplanId, String processTeam, String processRoute, String processPlacename,
                                    String processCoordinate, String processType, File file, String processRemarks, int processNumber, int flag,Context context) {
        String organizeId=SharedPreferencesHelper.getInstance().getOrganizationId(context);
        Log.i("info","巡逻上传经纬度"+processCoordinate);
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("patrolplanId", patrolplanId)
                .addFormDataPart("processTeam", processTeam)
                .addFormDataPart("feedbackMechanism",organizeId)
                .addFormDataPart("processRoute", processRoute)
                .addFormDataPart("processPlacename", processPlacename+"")
                .addFormDataPart("processCoordinate", processCoordinate+"")
                .addFormDataPart("processType", processType+"")
                .addFormDataPart("processRemarks", processRemarks+"")
                .addFormDataPart("processNumber", processNumber + "")
                .addFormDataPart("flag", flag + "");
        if (file != null) {
            builder.addFormDataPart("patprocessPic", file.getName(), RequestBody.create(MediaType.parse("image/png"), file));
        }
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder().tag(context).post(requestBody).url(URL + "bizPatprocess/savePatprocessInfor").build();
        okHttpUtils.request(request, CacheType.ONLY_NETWORK, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(ConstantCode.POST_FAILED);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                handler.sendEmptyMessage(ConstantCode.POST_SUCCESS);
            }
        });

    }


//    /**
//     * 存储个人信息
//     */
//    public void saveInformation(String username, String location, String version, String phoneModel, Context context) {
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//        String date = format.format(new Date());
//        Log.i("tag","获取的经纬度"+location);
//        RequestBody body = new FormBody.Builder().add("loginName", username).add("devcoordinate", location)
//                .add("devName", phoneModel + "").add("devVersion", version).add("devType", "手机").add("devdate", date).build();
//        Request request = new Request.Builder().tag(context).url(URL + "terminals/updateTerminals").post(body).build();
//        okHttpUtils.request(request, CacheType.ONLY_NETWORK, new com.chinasoft.ctams.okhttp.Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Log.i("info", "存储失败");
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                if (response != null ) {
//                    Log.i("info", "上传设备信息存储成功"+response);
//                    String result=response.body().string();
//                    Log.i("info","上传设备信息返回值为"+result);
//                } else {
//                    Log.i("info", "上传设备信息加载异常");
//                }
//            }
//        });
//    }
    /**
     * 存储GPS定位
     */
    public void saveGPS(String username, String location,boolean isPatrol,int patrolNumber, Context context) {

        FormBody.Builder builder=new FormBody.Builder().add("loginName", username).add("coordinate", location);
        if (isPatrol){
            builder.add("processNum",patrolNumber+"");
        }

        Log.i("info","gps坐标是"+location);
        RequestBody body=builder.build();
        Request request = new Request.Builder().tag(context).url(URL + "terminals/updateCoordinate").post(body).build();
        okHttpUtils.request(request, CacheType.ONLY_NETWORK, new com.chinasoft.ctams.okhttp.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("info", "存储失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null ) {
                    Log.i("info", "上传GPS定位存储成功"+response.body().string());
                } else {
                    Log.i("info", "上传GPS定位加载异常");
                }
            }
        });
    }

    /**
     * 加载分组通讯录
     */
    public void loadGroupManageAddress(final Handler handler, String peopleId,Context context) {

        params = new HashMap<>();
        params.put("peopleId", peopleId);
        url = getUrl(params, URL + "percomm/listPercommGroup");

        Request request = new Request.Builder().tag(context).url(url).build();
        okHttpUtils.request(request, CacheType.NETWORK_ELSE_CACHED, new com.chinasoft.ctams.okhttp.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("info", "加载失败");
                handler.sendEmptyMessage(ConstantCode.POST_FAILED);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null && response.isSuccessful()) {
                    String result = response.body().string();
                    Log.i("info", "获取的群组通讯录" + result);
                    try {
                        JSONArray array = new JSONArray(result);
                        Map<String, List<GroupManageBean>> map = new HashMap<String, List<GroupManageBean>>();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            JSONArray jsonArray = new JSONArray(object.getString("list"));
                            List<GroupManageBean> groupManageList = groupManageList = new ArrayList<GroupManageBean>();
                            for (int j = 0; j < jsonArray.length(); j++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(j);
                                GroupManageBean bean = new GroupManageBean();
                                bean.setPercommid(jsonObject.getString("percommid"));

                                bean.setPercommtel(jsonObject.getString("percommtel"));
                                String percommname = jsonObject.getString("percommname");
                                if (percommname.length() > 0) {
                                    bean.setPercommname(percommname);
                                    groupManageList.add(bean);
                                }

                            }
                            String grouping = object.getString("grouping");
                            Log.i("info", "grouping的值是" + grouping + ",,goupManage集合长度是" + groupManageList.size());
                            map.put(grouping, groupManageList);
                        }
                        Log.i("info", "map的长度是" + map.size());
                        //Log.i("info","map第一个"+map.get(0).get(0).getPercommname());
                        Message message = handler.obtainMessage();
                        message.what = ConstantCode.POST_SUCCESS;
                        message.obj = map;
                        handler.sendMessage(message);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.i("info", "加载异常");
                    handler.sendEmptyMessage(ConstantCode.POST_FAILED);
                }

            }
        });

    }

    /**
     * 综合查询
     */
    public void loadSearchData(final Handler handler, int pageNo, String keyword,Context context) {
        params = new HashMap<>();
        params.put("pageNo", pageNo + "");
        params.put("lTitle", keyword);
        url = getUrl(params, URL + "lawsRegulations/listLawsRegulationsMo");
        final Request request = new Request.Builder().tag(context).url(url).build();
        okHttpUtils.request(request, CacheType.NETWORK_ELSE_CACHED, new com.chinasoft.ctams.okhttp.Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(ConstantCode.POST_FAILED);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null) {
                    String result = response.body().string();
                    Log.i("info", "获取的搜索内容" + result);
                    try {
                        JSONObject object = new JSONObject(result);
                        List<ComprehensiveSearchBean> list = new ArrayList<ComprehensiveSearchBean>();
                        JSONArray array = object.getJSONArray("rows");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonObject = array.getJSONObject(i);
                            ComprehensiveSearchBean bean = new ComprehensiveSearchBean();
                            bean.setLFounder(jsonObject.getString("lFounder"));
                            bean.setLOrganization(jsonObject.getString("lOrganization"));
                            bean.setLTimer(jsonObject.getString("lTimer"));
                            bean.setLTitle(jsonObject.getString("lTitle"));
                            bean.setLType(jsonObject.getString("lType"));
                            bean.setLwordurl(jsonObject.getString("lwordurl"));
                            list.add(bean);
                        }
                        Message message = handler.obtainMessage();
                        message.what = ConstantCode.POST_SUCCESS;
                        message.obj = list;
                        handler.sendMessage(message);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.i("info", "解析异常" + e.toString());
                    }
                } else {
                    handler.sendEmptyMessage(ConstantCode.POST_NULL_RESPONSE);
                }
            }
        });

    }

    /**
     * 下载案例法规word文件
     */
    public void downLoadWord(final Handler handler, final String wordName, String wordUrl,Context context) {
        Request request = new Request.Builder().tag(context).url(URL + wordUrl).build();
        okHttpUtils.request(request, CacheType.NETWORK_ELSE_CACHED, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("info", "下载失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                File appDir = new File(Environment.getExternalStorageDirectory(), "word文档");
                if (!appDir.exists()) {
                    appDir.mkdir();
                }
                try {
                    is = response.body().byteStream();
                    long total = response.body().contentLength();
                    File file = new File(appDir, wordName + ".doc");
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        Log.i("info", "获取的progress" + progress);
                        Message message = handler.obtainMessage();
                        message.what = ConstantCode.GET_DOWNLOAD;
                        message.arg1 = progress;
                        handler.sendMessage(message);
                    }
                    fos.flush();
                    Message message = handler.obtainMessage();
                    message.obj = file;
                    message.what = ConstantCode.POST_SUCCESS;
                    handler.sendMessage(message);
                    Log.i("info", "文件下载成功");
                } catch (Exception e) {
                    Log.i("info", "文件下载失败" + e.toString());
                } finally {
                    if (is != null) {
                        is.close();
                    }
                    if (fos != null) {
                        fos.close();
                    }
                }
            }
        });
    }

    /**
     * 上传视频
     */

    public void uploadVideo(final Handler handler, File file, String title, String content, String loginName,Context context) {

        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("videoviewDeviceuserid", loginName)
                .addFormDataPart("videoviewVideoname", title)
                .addFormDataPart("videoviewVideosummary", content);

        if (file != null) {
            builder.addFormDataPart("videoUrl",file.getName(),RequestBody.create(MediaType.parse("text/x-markdown; charset=utf-8"),file));
        }

        RequestBody requestBody = builder.build();

        ProgressRequestBody body=new ProgressRequestBody(requestBody, new UIProgressRequestListener() {
            @Override
            public void onUIRequestProgress(long bytesWrite, long contentLength, boolean done) {
                    int percent= (int) ((100 * bytesWrite) / contentLength);
                    Message message=handler.obtainMessage();
                Log.i("info","进度  "+percent);
                message.arg1=percent;
                message.what=ConstantCode.GET_DOWNLOAD;
                handler.sendMessage(message);
            }
        });

        Request request = new Request.Builder().post(body).tag(context).url(URL + "bizVideoview/saveVideoview").build();
        //Request request = new Request.Builder().post(body).tag(context).url("http://192.168.1.212:8080/ctams/" + "bizVideoview/saveVideoview").build();

        okHttpUtils.request(request, CacheType.ONLY_NETWORK, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("info", "上传失败");
                handler.sendEmptyMessage(ConstantCode.POST_FAILED);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("info", "在model中上传成功");
                handler.sendEmptyMessage(ConstantCode.POST_SUCCESS);
            }
        });

    }

    /**
     * 办内工作安排
     */
    public void getOfficeWorkData(final Handler handler, int pageNo, Context context){
        params=new HashMap<>();
        params.put("pageNo",pageNo+"");
        url=getUrl(params,URL+"workschedule/listWorkscheduleMobile");
        Log.i("tag","获取的url"+url);
        Request request=new Request.Builder().url(url).tag(context).build();

//        okHttpUtils.request(request, CacheType.NETWORK_ELSE_CACHED, new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                Log.i("info","获取的办内工作安排"+response.body().string());
//            }
//        });

        okHttpUtils.request(request,CacheType.NETWORK_ELSE_CACHED,new JsonCallback<OfficeDailyBean>(){

            @Override
            public void onFailure(Call call, Exception e) {
                Log.i("tag","获取办内工作安排");
                handler.sendEmptyMessage(ConstantCode.POST_FAILED);
            }

            @Override
            public void onResponse(Call call, OfficeDailyBean object){
                Log.i("tag","获取办内工作安排"+object.getResultCode());
                Message message=handler.obtainMessage();
                message.obj=object;
                message.what=ConstantCode.POST_SUCCESS;
                handler.sendMessage(message);
            }
        });
    }
    public void getLeaderWorkSchedule(final Handler handler, int pageNo, Context context){
        params=new HashMap<>();
        params.put("pageNo",pageNo+"");
        url=getUrl(params,URL+"leaderschedule/listLeaderscheduleMobile");
        Log.i("info","获取的url"+url);
        Request request=new Request.Builder().tag(context).url(url).build();
        okHttpUtils.request(request, CacheType.NETWORK_ELSE_CACHED, new JsonCallback<LeaderDailyBean>() {
            @Override
            public void onFailure(Call call, Exception e) {
                Log.i("tag","获取领导工作安排shibai");
                handler.sendEmptyMessage(ConstantCode.POST_FAILED);
            }

            @Override
            public void onResponse(Call call, LeaderDailyBean object) throws IOException {
                Log.i("tag","获取领导工作安排"+object);

                Message message=handler.obtainMessage();
                message.obj=object;
                message.what=ConstantCode.POST_SUCCESS;
                handler.sendMessage(message);
            }
        });

    }
    /**
     * 请求用户的组织机构
     */
    public void getUnitInformation(final Handler handler, Context context){
        RequestBody body = new FormBody.Builder().add("peopleLoginname", SharedPreferencesHelper.getInstance().getUserName(context)).build();
        SharedPreferences preferences = context.getSharedPreferences("ip", Context.MODE_PRIVATE);
        Request request = new Request.Builder().url(URL + "people/showOnePeople").post(body).build();
        okHttpUtils.request(request, CacheType.NETWORK_ELSE_CACHED, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String result = response.body().string();
                    Log.i("tag", "----------"+result);
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String  submitName = jsonObject.getString("peopleName");
                        String  submitUnit = jsonObject.getString("peopleOrganization");
                        Map<String,String> map=new HashMap<String, String>();
                        map.put("name",submitName);
                        map.put("unit",submitUnit);
                        Message message=handler.obtainMessage();
                        message.obj=map;
                        message.what=0x9954;
                        handler.sendMessage(message);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                }
            }
        });
    }
    /**
     * 获取应用版本号
     */
    public void getPackageVersion(final Handler handler, Context context){
        final Request request = new Request.Builder().url(URL + "bizMobileapp/findAppVersion").build();
        okHttpUtils.request(request, CacheType.NETWORK_ELSE_CACHED, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("info","获取失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result=response.body().string();
                Log.i("info","获取的版本号"+result);
                Map<String,String> map=new HashMap<String, String>();
                try {
                    JSONObject object=new JSONObject(result);
                    String version=object.getString("version");
                    String appUrl=object.getString("appUrl");
                    map.put("version",version);
                    map.put("appUrl",appUrl);
                    Message message=handler.obtainMessage();
                    message.obj=map;
                    message.what=ConstantCode.POST_SUCCESS;
                    handler.sendMessage(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
    /**
     * 下载新版本
     */
    public void downLoadAPK(final Handler handler,String url, Context context) {
        Request request = new Request.Builder().tag(context).url(url).build();
        okHttpUtils.request(request, CacheType.NETWORK_ELSE_CACHED, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("info", "下载失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                File appDir = new File(Environment.getExternalStorageDirectory(), "word文档");
                if (!appDir.exists()) {
                    appDir.mkdir();
                }
                try {
                    is = response.body().byteStream();
                    long total = response.body().contentLength();
                    File file = new File(appDir, "ctams.apk");
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        Log.i("info", "获取的progress" + progress);
                        Message message = handler.obtainMessage();
                        message.what = ConstantCode.GET_DOWNLOAD;
                        message.arg1 = progress;
                        handler.sendMessage(message);
                    }
                    fos.flush();
                    Message message = handler.obtainMessage();
                    message.obj = file;
                    message.what = ConstantCode.POST_SUCCESS;
                    handler.sendMessage(message);
                    Log.i("info", "文件下载成功");
                } catch (Exception e) {
                    Log.i("info", "文件下载失败" + e.toString());
                } finally {
                    if (is != null) {
                        is.close();
                    }
                    if (fos != null) {
                        fos.close();
                    }
                }
            }
        });
    }
    /**
     * 上传事件信息
     */
    public void sendThings(final Handler handler, Context context, String header, String content, String dev){
        Map<String,String> map=SharedPreferencesHelper.getInstance().getLocation(context);
        String longitude=map.get("longitude");
        String latitude=map.get("latitude");
        Log.i("info","获取的经纬度"+longitude+"   "+latitude);
       RequestBody body=new MultipartBody.Builder().addFormDataPart("captainid",header).addFormDataPart("reportedcontent",content)
               .addFormDataPart("reportedlong",longitude).addFormDataPart("reportedlat",latitude).addFormDataPart("reportedsource",dev).build();
        Request request=new Request.Builder().post(body).tag(context).url(URL+"bizInformationreport/saveInforReport").build();
        okHttpUtils.request(request, CacheType.NETWORK_ELSE_CACHED, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(ConstantCode.POST_FAILED);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                handler.sendEmptyMessage(ConstantCode.POST_SUCCESS);
            }
        });
    }
    /**
     * 视频回传终端信息
     */
    public void updateVideoState(Context context,String username,String rtmpurl,String isOnline){
        RequestBody body=new MultipartBody.Builder().addFormDataPart("username",username).addFormDataPart("rtmpurl",rtmpurl)
                .addFormDataPart("isonline",isOnline).build();
        Request request=new Request.Builder().post(body).tag(context).url(URL+"apnUser/isOnline").build();
        okHttpUtils.request(request, CacheType.NETWORK_ELSE_CACHED, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("info","上传成功");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                Log.i("info","上传失败");
            }
        });
    }


    /**
     * 根据URL和相应参数进行get请求
     *
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
