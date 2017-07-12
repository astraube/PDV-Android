package com.autazcloud.pdv.data.remote.subscribers;

import android.content.Context;
import android.support.annotation.NonNull;

import com.autazcloud.pdv.data.remote.service.ApiService;


/**
 * Created by aStraube on 08/07/2017.
 */

public interface SubscriberInterface {
    public Context getContext();
    public ApiService getApiService();
    public void onSubscriberCompleted();
    public void onSubscriberError(@NonNull Throwable e, final String title, final String msg);
    public void onSubscriberNext(Object t);
}
