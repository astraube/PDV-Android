package com.autazcloud.pdv.domain.interfaces;


import android.content.Context;

import com.autazcloud.pdv.data.remote.ResultDefault;

public interface LoginInterface {
    public Context getContext();
	public void onSignUp();
	public void onLoginCancel();
	public boolean onLogin(String user, String pass);
	public void onLoginSuccess(ResultDefault object);
	public void onLogout();
}
