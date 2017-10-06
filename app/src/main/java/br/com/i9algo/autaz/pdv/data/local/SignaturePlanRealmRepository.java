package br.com.i9algo.autaz.pdv.data.local;

import br.com.i9algo.autaz.pdv.domain.models.SignaturePlan;
import io.realm.Realm;

/**
 * Created by aStraube on 21/09/2017.
 */

public class SignaturePlanRealmRepository {

    private static final String TAG = SignaturePlanRealmRepository.class.getSimpleName();

    public static SignaturePlan getFirst() {
        Realm realm = Realm.getDefaultInstance();
        SignaturePlan model = null;
        try {
            model = realm.where(SignaturePlan.class).findFirst();
        } catch (Exception e) {
            e.printStackTrace();

        }  finally {
            //realm.close();
        }
        return model;
    }

    public static synchronized void syncItem(final SignaturePlan model) {
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
}
