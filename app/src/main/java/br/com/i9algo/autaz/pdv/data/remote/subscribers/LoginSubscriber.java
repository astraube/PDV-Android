package br.com.i9algo.autaz.pdv.data.remote.subscribers;

import android.graphics.Color;
import android.util.Log;

import org.apache.commons.lang3.StringUtils;

import br.com.i9algo.autaz.pdv.R;
import br.com.i9algo.autaz.pdv.data.local.UserRealmRepository;
import br.com.i9algo.autaz.pdv.domain.models.inbound.ResultStatusDefault;
import br.com.i9algo.autaz.pdv.domain.interfaces.LoginInterface;

import br.com.i9algo.autaz.pdv.domain.models.inbound.UserWrapper;

/**
 * Created by aStraube on 30/06/2017.
 */

public class LoginSubscriber extends DefaultSubscriber<UserWrapper> {

    private final String TAG;
    private SubscriberInterface _owner;

    public LoginSubscriber(SubscriberInterface owner) {
        this._owner = owner;
        this.TAG = getClass().getSimpleName();

        owner.setSweetProgress(owner.getContext().getString(R.string.process_login));
    }

    @Override
    public void onCompleted() {
        super.onCompleted();
        Log.v(TAG, "API WEB - onCompleted");
    }

    @Override
    public void onError(Throwable e) {
        super.onError(e);
        Log.e(TAG, "API WEB - onError");

        if (resultStatus != null) {
            if (StringUtils.isEmpty(resultStatus.title))
                resultStatus.title = _owner.getContext().getString(R.string.err_login_title);

            if (StringUtils.isEmpty(resultStatus.message))
                resultStatus.message = _owner.getContext().getString(R.string.err_try_again);

        } else {
            resultStatus = new ResultStatusDefault();
            resultStatus.title = _owner.getContext().getString(R.string.err_login_title);
            resultStatus.message = _owner.getContext().getString(R.string.err_login_msg);
        }

        if (this._owner instanceof LoginInterface)
            ((LoginInterface)this._owner).onLoginError(resultStatus);
    }

    @Override
    public void onNext(UserWrapper t) {
        super.onNext(t);
        Log.v(TAG, "API WEB - onNext");

        UserRealmRepository.syncItem(t.getModel());

        if (this._owner instanceof LoginInterface)
            ((LoginInterface)this._owner).onLoginSuccess(t);
    }
}
