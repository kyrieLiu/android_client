package com.chinasoft.ctams.base;

import android.os.Bundle;

/**
 * Created by Administrator on 2016/6/16.
 */
public interface UIOperation {
    int getLayoutId();
    void initPage(Bundle savedInstanceState);
}
