package com.autazcloud.pdv.data.remote.repositoryes;

import android.graphics.Color;

import com.autazcloud.pdv.R;
import com.autazcloud.pdv.data.local.PreferencesRepository;
import com.autazcloud.pdv.data.local.ProductsRealmRepository;
import com.autazcloud.pdv.data.remote.subscribers.DefaultSubscriber;
import com.autazcloud.pdv.data.remote.subscribers.SubscriberInterface;
import com.autazcloud.pdv.domain.constants.AuthAttr;
import com.autazcloud.pdv.domain.models.inbound.ProductsArraylistWrapper;
import com.autazcloud.pdv.ui.base.BaseActivity;


import cn.pedant.SweetAlert.SweetAlertDialog;
import rx.schedulers.Schedulers;

/**
 * Created by aStraube on 10/07/2017.
 */

public class ProductsRepository {

    private final SubscriberInterface _owner;

    public ProductsRepository(final SubscriberInterface owner) {
        this._owner = owner;
    }

    public void onLoadProducts() {

        String apiToken = PreferencesRepository.getValue(AuthAttr.USER_API_TOKEN);
        String publicToken = PreferencesRepository.getValue(AuthAttr.USER_PUBLIC_TOKEN);

        if (apiToken.isEmpty() || publicToken.isEmpty()) {
            String msg = "Usuario nao foi identificado corretamente";
            _owner.onSubscriberError(new Throwable(msg), "Erro de credenciais", msg);
            return;
        }

        if (_owner.getContext() instanceof BaseActivity) {
            try {
                BaseActivity act = (BaseActivity)_owner.getContext();
                SweetAlertDialog pDialog = new SweetAlertDialog(act, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitle(R.string.txt_please_wait);
                pDialog.setContentText(act.getString(R.string.process_download_data_account));
                pDialog.setCancelable(false);
                act.setSweetDialog(pDialog);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            _owner.getApiService().getProducts(apiToken, publicToken)
                    .subscribeOn(Schedulers.io())
                    .subscribe(new DefaultSubscriber<ProductsArraylistWrapper>(){
                        @Override
                        public void onCompleted() {
                            super.onCompleted();

                            _owner.onSubscriberCompleted();
                        }

                        @Override
                        public void onError(Throwable e) {
                            //String msg = owner.getContext().getString(R.string.err_download_data_account);
                            //msg += " " + owner.getContext().getString(R.string.err_try_again);
                            _owner.onSubscriberError(e, null, null);
                            super.onError(e);
                        }

                        @Override
                        public void onNext(ProductsArraylistWrapper t) {
                            super.onNext(t);

                            ProductsRealmRepository.syncItems(t.data);

                            _owner.onSubscriberNext(t);
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
