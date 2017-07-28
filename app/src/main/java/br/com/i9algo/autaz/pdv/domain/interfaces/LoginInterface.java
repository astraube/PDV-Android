package br.com.i9algo.autaz.pdv.domain.interfaces;


import android.content.Context;

import br.com.i9algo.autaz.pdv.data.remote.ResultDefault;

public interface LoginInterface {
    public Context getContext();
	public void onSignUp();
	public void onLoginCancel();
	public void onLogin(String user, String pass);
	public void onLoginSuccess(ResultDefault object);
	public void onLogout();
}
