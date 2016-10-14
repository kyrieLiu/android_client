package com.chinasoft.ctams.okhttp;



import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by ly on 16/9/6.
 */
public abstract class Callback implements okhttp3.Callback {
    public void onStart(){

    }
    public void onFinish(){

    }

    public abstract void onFailure(Call call, IOException e);

    public abstract void onResponse(Call call, Response response) throws IOException;

    //public void inProgress(float progress, long total , int id){};


}
