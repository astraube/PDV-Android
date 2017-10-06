package br.com.i9algo.autaz.pdv.data.remote.subscribers;

import android.util.Log;

import org.apache.commons.lang3.StringUtils;

import br.com.i9algo.autaz.pdv.R;
import br.com.i9algo.autaz.pdv.data.local.DeviceRealmRepository;
import br.com.i9algo.autaz.pdv.domain.models.inbound.DeviceWrapper;
import br.com.i9algo.autaz.pdv.domain.models.inbound.ResultStatusDefault;

/**
 * Created by aStraube on 30/06/2017.
 */
public class DeviceSubscriber extends DefaultSubscriber<DeviceWrapper> {

    private final String TAG;
    private SubscriberInterface _owner;

    public DeviceSubscriber(SubscriberInterface owner) {
        this._owner = owner;
        this.TAG = getClass().getSimpleName();
    }

    @Override
    public void onCompleted() {
        super.onCompleted();
        Log.v(TAG, "API WEB - onCompleted");

        //_owner.onSubscriberCompleted();
    }

    @Override
    public void onError(Throwable e) {
        super.onError(e);
        Log.e(TAG, "API WEB - onError");

        if (resultStatus != null) {
            if (StringUtils.isEmpty(resultStatus.title))
                resultStatus.title = "Informacoes do equipamento";

            if (StringUtils.isEmpty(resultStatus.message))
                resultStatus.message = _owner.getContext().getString(R.string.err_try_again);

        } else {
            resultStatus = new ResultStatusDefault();
            resultStatus.title = "Informacoes do equipamento";
            resultStatus.message = _owner.getContext().getString(R.string.err_try_again);
        }

        Log.e(TAG, "API WEB - " + resultStatus.message);
    }

    @Override
    public void onNext(DeviceWrapper t) {
        super.onNext(t);
        Log.v(TAG, "API WEB - onNext");
        //Log.v(TAG, "API WEB - " + t.getModel().getPublicToken());

        DeviceRealmRepository.syncItem(t.getModel());
    }
}
