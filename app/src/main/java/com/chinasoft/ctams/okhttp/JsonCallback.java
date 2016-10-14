package com.chinasoft.ctams.okhttp;

import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Call;

/**
 * Created by ly on 16/9/6.
 */
public abstract class JsonCallback<T> {
    public abstract void onFailure(Call call, Exception e);
    public abstract void onResponse(Call call, T object) throws IOException;

    public void onStart(){

    }
    public void onFinish(){

    }

    Type getType(){
        Type type = ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        if(type instanceof Class){
            return type;
        }else{
            return new TypeToken<T>(){}.getType();
        }
    }
}
