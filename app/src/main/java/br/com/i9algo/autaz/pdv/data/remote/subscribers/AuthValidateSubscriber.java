package br.com.i9algo.autaz.pdv.data.remote.subscribers;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;

import br.com.i9algo.autaz.pdv.R;
import br.com.i9algo.autaz.pdv.data.remote.ResultDefault;
import br.com.i9algo.autaz.pdv.domain.interfaces.LoginInterface;
import br.com.i9algo.autaz.pdv.ui.base.BaseActivity;

import cn.pedant.SweetAlert.SweetAlertDialog;


/**
 * Created by aStraube on 30/06/2017.
 */
public class AuthValidateSubscriber extends DefaultSubscriber<ResultDefault> {

    private final String TAG;
    private SubscriberInterface _owner;

    public AuthValidateSubscriber(SubscriberInterface owner) {
        this._owner = owner;
        this.TAG = getClass().getSimpleName();


    }

    @Override
    public void onCompleted() {
        super.onCompleted();
    }

    @Override
    public void onError(Throwable e) {
        super.onError(e);

        if (e != null)
            Log.e(TAG, e.getMessage());

        //PreferencesRepository.setValue(AuthAttr.USER_API_TOKEN, "");
        //PreferencesRepository.setValue(AuthAttr.USER_PUBLIC_TOKEN, "");

        if (this._owner instanceof BaseActivity) {
            ((Activity)_owner.getContext()).runOnUiThread(new Runnable() {
                public void run() {
                    SweetAlertDialog pDialog = new SweetAlertDialog(_owner.getContext(), SweetAlertDialog.ERROR_TYPE);
                    pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                    pDialog.setTitle(R.string.err_working_offline_title);
                    pDialog.setTitleText(_owner.getContext().getString(R.string.err_working_offline_title));
                    String msg = _owner.getContext().getString(R.string.err_auth_invalid);
                    msg += "\n" + _owner.getContext().getString(R.string.err_working_offline_msg);
                    pDialog.setContentText(msg);
                    pDialog.setCancelable(false);

                    ((BaseActivity)_owner).setSweetDialog(pDialog);
                }
            });

        }
    }

    @Override
    public void onNext(ResultDefault t) {
        super.onNext(t);
        //Log.i("LoginSubscriber", "onLogin onNext");
        if (this._owner instanceof LoginInterface)
            ((LoginInterface)this._owner).onLoginSuccess(t);
    }
}
