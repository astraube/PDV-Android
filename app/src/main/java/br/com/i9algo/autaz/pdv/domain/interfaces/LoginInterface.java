package br.com.i9algo.autaz.pdv.domain.interfaces;


import android.content.Context;

import br.com.i9algo.autaz.pdv.domain.models.inbound.ResultStatusDefault;
import br.com.i9algo.autaz.pdv.domain.models.inbound.UserWrapper;

public interface LoginInterface {
    public Context getContext();
	public void onSignUp();
	public void onLoginCancel();
	public void onLogin(String user, String pass);

	public void onLoginSuccess(UserWrapper object);
	public void onLoginError(ResultStatusDefault resultStatus);

	public void onLogout();
}
