package br.com.i9algo.autaz.pdv.data.remote.repositoryes;

import android.util.Log;

import br.com.i9algo.autaz.pdv.data.local.PreferencesRepository;
import br.com.i9algo.autaz.pdv.data.local.ProductsRealmRepository;
import br.com.i9algo.autaz.pdv.data.local.UserRealmRepository;
import br.com.i9algo.autaz.pdv.data.remote.subscribers.DefaultSubscriber;
import br.com.i9algo.autaz.pdv.data.remote.subscribers.SubscriberInterface;
import br.com.i9algo.autaz.pdv.domain.constants.AuthAttr;
import br.com.i9algo.autaz.pdv.domain.models.Product;
import br.com.i9algo.autaz.pdv.domain.models.User;
import br.com.i9algo.autaz.pdv.domain.models.inbound.ProductsArraylistWrapper;

import io.realm.RealmResults;
import rx.schedulers.Schedulers;

/**
 * Created by aStraube on 10/07/2017.
 */

public class ProductsRepository {

    private final String TAG;
    private final SubscriberInterface _owner;

    public ProductsRepository(final SubscriberInterface owner) {
        this._owner = owner;
        this.TAG = getClass().getSimpleName();
    }

    public void onLoadProducts(String lastUpdatedAt) {
        User user = UserRealmRepository.getFirst();
        if (user == null || user.getPublicToken().isEmpty() || user.getApiToken().isEmpty()) {
            String msg = "Usuario nao foi identificado corretamente";
            _owner.onSubscriberError(new Throwable(msg), "Erro de credenciais", msg);
            return;
        }

        try {
            _owner.getApiService().getProducts(user.getApiToken(), user.getPublicToken(), lastUpdatedAt)
                    .subscribeOn(Schedulers.io())
                    .subscribe(new DefaultSubscriber<ProductsArraylistWrapper>(){
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
                            Log.e(TAG, "API WEB - onError - " + resultStatus.message);

                            //String msg = owner.getContext().getString(R.string.err_download_data_account);
                            //msg += " " + owner.getContext().getString(R.string.err_try_again);
                            _owner.onSubscriberError(e, null, null);
                        }

                        @Override
                        public void onNext(ProductsArraylistWrapper t) {
                            super.onNext(t);
                            Log.v(TAG, "API WEB - onNext");

                            if (t.data != null && t.data.size() > 0)
                                ProductsRealmRepository.syncItems(t.data);

                            _owner.onSubscriberNext(t);
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
