package br.com.i9algo.autaz.pdv.data.local;

import br.com.i9algo.autaz.pdv.domain.models.User;
import io.realm.Realm;

/**
 * Created by aStraube on 21/09/2017.
 */

public class UserRealmRepository {

    private static final String TAG = UserRealmRepository.class.getSimpleName();

    public static synchronized boolean isExisting(String token) {
        Realm realm = Realm.getDefaultInstance();
        User result = realm.where(User.class).equalTo(User.getRouteKeyName(), token).findFirst();
        return (result != null);
    }

    public static synchronized long count() {
        Realm realm = Realm.getDefaultInstance();
        long result = realm.where(User.class).count();
        return (result);
    }

    public static User getFirst() {
        Realm realm = Realm.getDefaultInstance();
        User model = null;
        try {
            model = realm.where(User.class).findFirst();
        } catch (Exception e) {
            e.printStackTrace();

        }  finally {
            //realm.close();
        }
        return model;
    }

    public static User getById(String token) {
        Realm realm = Realm.getDefaultInstance();
        User model = null;
        try {
            model = realm.where(User.class).equalTo(User.getRouteKeyName(), token).findFirst();
        } catch (Exception e) {
            e.printStackTrace();

        }  finally {
            //realm.close();
        }
        return model;
    }

    public static synchronized void syncItem(final User model) {
        Realm realm = Realm.getDefaultInstance();
        try {
            realm.beginTransaction();
            realm.insertOrUpdate(model); // insere ou atualiza, sem esperar um retorno
            realm.commitTransaction();
        } catch (Exception e) {
            e.printStackTrace();

        }  finally {
            realm.close();
        }
    }

    public static synchronized void addItem(User model) {
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

    public static synchronized void removeItem(User model) {
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
