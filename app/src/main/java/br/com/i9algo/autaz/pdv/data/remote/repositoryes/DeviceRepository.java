package br.com.i9algo.autaz.pdv.data.remote.repositoryes;

import android.util.Log;

import com.google.gson.Gson;

import br.com.i9algo.autaz.pdv.data.local.DeviceRealmRepository;
import br.com.i9algo.autaz.pdv.data.local.UserRealmRepository;
import br.com.i9algo.autaz.pdv.data.remote.subscribers.DefaultSubscriber;
import br.com.i9algo.autaz.pdv.data.remote.subscribers.DeviceSubscriber;
import br.com.i9algo.autaz.pdv.data.remote.subscribers.SubscriberInterface;
import br.com.i9algo.autaz.pdv.domain.models.Device;
import br.com.i9algo.autaz.pdv.domain.models.User;
import br.com.i9algo.autaz.pdv.domain.models.inbound.DeviceWrapper;
import rx.schedulers.Schedulers;

/**
 * Created by aStraube on 03/10/2017.
 *
 * @usage:
 * DeviceRepository dRepo = new DeviceRepository(this);
 * try {
 *      Device d = DeviceRealmRepository.getFirst();
 *      if (d == null || StringUtils.isEmpty(d.getPublicToken()))
 *          d = new Device(this);
 *
 *      dRepo.onDeviceStore(d);
 *
 * } catch (Exception e) {
 *      e.printStackTrace();
 * }
 *
 */
public class DeviceRepository {

    private String TAG = "DeviceRepository";

    private final SubscriberInterface _owner;

    public DeviceRepository(final SubscriberInterface owner) {
        this._owner = owner;
    }


    public void getByToken(String token) throws Exception {

        User user = UserRealmRepository.getFirst();
        if (user == null || user.getPublicToken().isEmpty() || user.getApiToken().isEmpty()) {
            String msg = "Usuario nao foi identificado corretamente";
            _owner.onSubscriberError(new Throwable(msg), "Erro de credenciais", msg);
            return;
        }

        try {

            _owner.getApiService().getByToken(user.getApiToken(), user.getPublicToken(), token)
                    .subscribeOn(Schedulers.io())
                    .subscribe(new DefaultSubscriber<DeviceWrapper>(){
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

                            _owner.onSubscriberError(e, null, null);
                        }

                        @Override
                        public void onNext(DeviceWrapper t) {
                            super.onNext(t);
                            Log.v(TAG, "API WEB - onNext");

                            DeviceRealmRepository.syncItem(t.data);

                            _owner.onSubscriberNext(t);
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     *
     * <p>
     * <pre>
     * {@code
     * DeviceRepository dRepo = new DeviceRepository(this);
     * try {
     *      Device d = DeviceRealmRepository.getFirst();
     *      if (d == null || StringUtils.isEmpty(d.getPublicToken()))
     *          d = new Device(this);
     *
     *      dRepo.onDeviceStore(d);
     *
     * } catch (Exception e) {
     *      e.printStackTrace();
     * }
     * }
     * </pre>
     * <p>
     * <p>
     *
     */
    public void onDeviceStore(Device device) throws Exception {
        User user = UserRealmRepository.getFirst();
        if (user == null || user.getPublicToken().isEmpty() || user.getApiToken().isEmpty()) {
            String msg = "Usuario nao foi identificado corretamente";
            _owner.onSubscriberError(new Throwable(msg), "Erro de credenciais", msg);
            return;
        }

        _owner.getApiService().storeDevice(user.getApiToken(), user.getPublicToken(), device)
                .subscribeOn(Schedulers.io())
                .subscribe(new DeviceSubscriber(_owner));
    }
}
