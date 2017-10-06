package br.com.i9algo.autaz.pdv.data.local;

import br.com.i9algo.autaz.pdv.domain.models.Device;
import io.realm.Realm;

/**
 * Created by aStraube on 21/09/2017.
 */

public class DeviceRealmRepository {

    private static final String TAG = DeviceRealmRepository.class.getSimpleName();

    public static synchronized boolean isExisting(String token) {
        Realm realm = Realm.getDefaultInstance();
        Device result = realm.where(Device.class).equalTo(Device.getRouteKeyName(), token).findFirst();
        return (result != null);
    }

    public static synchronized long count() {
        Realm realm = Realm.getDefaultInstance();
        long result = realm.where(Device.class).count();
        return (result);
    }

    public static Device getFirst() {
        Realm realm = Realm.getDefaultInstance();
        Device model = null;
        try {
            model = realm.where(Device.class).findFirst();
        } catch (Exception e) {
            e.printStackTrace();

        }  finally {
            //realm.close();
        }
        return model;
    }

    public static Device getById(String token) {
        Realm realm = Realm.getDefaultInstance();
        Device model = null;
        try {
            model = realm.where(Device.class).equalTo(Device.getRouteKeyName(), token).findFirst();
        } catch (Exception e) {
            e.printStackTrace();

        }  finally {
            //realm.close();
        }
        return model;
    }

    public static synchronized void syncItem(final Device model) {
        Realm realm = Realm.getDefaultInstance();
        try {
            realm.beginTransaction();
            realm.insertOrUpdate(model); // insere ou atualiza, sem esperar um retorno
            realm.commitTransaction();
        } finally {
            realm.close();
        }
    }

    public static synchronized void addItem(Device model) {
        //Log.w(TAG, "SQL - Adicionar Produto");

        Realm realm = Realm.getDefaultInstance();
        try {
            realm.beginTransaction();
            realm.insert(model);
            realm.commitTransaction();
        } catch (Exception e) {
            e.printStackTrace();

        }  finally {
            realm.close();
        }
    }

    public static synchronized void removeItem(Device model) {
        //Log.w(TAG, "SQL - Remover Device");

        Realm realm = Realm.getDefaultInstance();
        try {
            realm.beginTransaction();
            model.deleteFromRealm();
            realm.commitTransaction();
        } catch (Exception e) {
            e.printStackTrace();

        }  finally {
            realm.close();
        }
    }
}
