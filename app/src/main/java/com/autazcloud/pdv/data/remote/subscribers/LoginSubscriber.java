package com.autazcloud.pdv.data.remote.subscribers;

import android.graphics.Color;
import android.util.Log;

import com.autazcloud.pdv.R;
import com.autazcloud.pdv.domain.interfaces.LoginInterface;
import com.autazcloud.pdv.data.remote.ResultDefault;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by aStraube on 30/06/2017.
 */

public class LoginSubscriber extends DefaultSubscriber<ResultDefault> {

    private LoginInterface _owner;

    public LoginSubscriber(LoginInterface owner) {
        this._owner = owner;

        SweetAlertDialog pDialog = new SweetAlertDialog(owner.getContext(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitle(R.string.txt_please_wait);
        pDialog.setContentText(owner.getContext().getString(R.string.process_login));
        pDialog.setCancelable(false);
        owner.setSweetDialog(pDialog);
    }

    @Override
    public void onCompleted() {
        super.onCompleted();
    }

    @Override
    public void onError(Throwable e) {
        super.onError(e);
        // TODO - exibir o erro que o servidor retornou
        Log.e("LoginSubscriber - ", e.getMessage());
        Log.e("LoginSubscriber - ", e.getLocalizedMessage());

        _owner.getSubscriberInterface().onSubscriberError(e,
                _owner.getContext().getString(R.string.err_login_title),
                _owner.getContext().getString(R.string.err_login_msg));
    }

    @Override
    public void onNext(ResultDefault t) {
        super.onNext(t);
        //Log.i("LoginSubscriber", "onLogin onNext");
        this._owner.onLoginSuccess(t);
    }
}
