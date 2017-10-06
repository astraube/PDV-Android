package br.com.i9algo.autaz.pdv.data.local;

import br.com.i9algo.autaz.pdv.domain.models.Account;
import io.realm.Realm;

/**
 * Created by aStraube on 21/09/2017.
 */

public class AccountRealmRepository {

    private static final String TAG = AccountRealmRepository.class.getSimpleName();

    public static synchronized boolean isExisting(String token) {
        Realm realm = Realm.getDefaultInstance();
        Account result = realm.where(Account.class).equalTo(Account.getRouteKeyName(), token).findFirst();
        return (result != null);
    }

    public static synchronized long count() {
        Realm realm = Realm.getDefaultInstance();
        long result = realm.where(Account.class).count();
        return (result);
    }

    public static Account getFirst() {
        Realm realm = Realm.getDefaultInstance();
        Account model = null;
        try {
            model = realm.where(Account.class).findFirst();
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            //realm.close();
        }
        return model;
    }

    public static Account getById(String token) {
        Realm realm = Realm.getDefaultInstance();
        Account model = null;
        try {
            model = realm.where(Account.class).equalTo(Account.getRouteKeyName(), token).findFirst();
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            //realm.close();
        }
        return model;
    }

    public static synchronized void syncItem(final Account model) {
        Realm realm = Realm.getDefaultInstance();
        try {
            realm.beginTransaction();
            realm.insertOrUpdate(model); // insere ou atualiza, sem esperar um retorno
            realm.commitTransaction();
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            realm.close();
        }
    }

    public static synchronized void addItem(Account model) {
        //Log.w(TAG, "SQL - Adicionar Produto");

        Realm realm = Realm.getDefaultInstance();
        try {
            realm.beginTransaction();
            realm.insert(model);
            realm.commitTransaction();
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            realm.close();
        }
    }

    public static synchronized void removeItem(Account model) {
        //Log.w(TAG, "SQL - Remover Account");

        Realm realm = Realm.getDefaultInstance();
        try {
            realm.beginTransaction();
            model.deleteFromRealm();
            realm.commitTransaction();
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            realm.close();
        }
    }
}
