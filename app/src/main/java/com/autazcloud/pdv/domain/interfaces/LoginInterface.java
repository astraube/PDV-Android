package com.autazcloud.pdv.domain.interfaces;


import android.content.Context;

import com.autazcloud.pdv.data.remote.ResultDefault;
import com.autazcloud.pdv.data.remote.subscribers.SubscriberInterface;

import cn.pedant.SweetAlert.SweetAlertDialog;

public interface LoginInterface {
    public Context getContext();
	public SubscriberInterface getSubscriberInterface();
	public SweetAlertDialog getSweetDialog();
	public void setSweetDialog(SweetAlertDialog dialog);
	public void onSignUp();
	public void onLoginCancel();
	public boolean onLogin(String user, String pass);
	public void onLoginSuccess(ResultDefault object);
	public void onLogout();
}
