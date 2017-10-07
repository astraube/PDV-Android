package br.com.i9algo.autaz.pdv.data.remote.repositoryes;

import android.util.Log;

import br.com.i9algo.autaz.pdv.data.local.UserRealmRepository;
import br.com.i9algo.autaz.pdv.data.remote.subscribers.DefaultSubscriber;
import br.com.i9algo.autaz.pdv.data.remote.subscribers.SubscriberInterface;
import br.com.i9algo.autaz.pdv.domain.models.User;
import br.com.i9algo.autaz.pdv.domain.models.inbound.ResultDefault;
import br.com.i9algo.autaz.pdv.domain.models.outbound.SaleApi;
import rx.schedulers.Schedulers;

/**
 * Created by aStraube on 10/07/2017.
 */

public class SaleRepository {

    private final String TAG;
    private final SubscriberInterface _owner;

    public SaleRepository(final SubscriberInterface owner) {
        this._owner = owner;
        this.TAG = getClass().getSimpleName();
    }

    public void storeSale(SaleApi sale) {

        User user = UserRealmRepository.getFirst();
        if (user == null || user.getPublicToken().isEmpty() || user.getApiToken().isEmpty()) {
            String msg = "Usuario nao foi identificado corretamente";
            _owner.onSubscriberError(new Throwable(msg), "Erro de credenciais", msg);
            return;
        }

        try {
            _owner.getApiService().storeSale(user.getApiToken(), user.getPublicToken(), sale)
                    .subscribeOn(Schedulers.io())
                    .subscribe(new DefaultSubscriber<ResultDefault>(){
                        @Override
                        public void onCompleted() {
                            super.onCompleted();
                            Log.v(TAG, "API WEB - onCompleted");
                            _owner.onSubscriberCompleted();
                        }

                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                            Log.e(TAG, "API WEB - onError");

                            //String msg = owner.getContext().getString(R.string.err_download_data_account);
                            //msg += " " + owner.getContext().getString(R.string.err_try_again);
                            _owner.onSubscriberError(e, null, null);
                        }

                        @Override
                        public void onNext(ResultDefault t) {
                            super.onNext(t);
                            Log.v(TAG, "API WEB - onNext");
                            Log.v(TAG, "API WEB - " + t.data);

                            _owner.onSubscriberNext(t);
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
